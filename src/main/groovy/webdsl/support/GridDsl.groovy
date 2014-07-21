/**
 * Copyright to the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlSpan
import webdsl.WebDsl

import static webdsl.Orientation.VERTICAL


class GridDsl {
  def grid = []
  def gridOptions
  private PageContainer pageContainer
  private DslFactory factory
  private Closure textClosure = { it.text.trim() }
  private Closure camelCaseClosure = { WebDsl.camel(it.text.trim()) }


  GridDsl(PageContainer pageContainer, DslFactory factory, gridOptions = [:]) {
    this.pageContainer = pageContainer
    this.factory = factory
    this.gridOptions = gridOptions
  }

  def nextRow(td) {
    grid << [td]
  }

  def appendColumn(td) {
    grid[-1] << td
  }

  def getAs() {
    this
  }

  def getSpan() {
    def result = []
    process { row, column, td ->
      if (column == 0) {
        result << [:]
      }
      td.element.htmlElementDescendants.each { span ->
        if (span instanceof HtmlSpan && span.getAttribute("name")) {
          result[-1][span.getAttribute("name")] = span.getTextContent()
        }
      }
    }
    result
  }

  def getList() {
    def result = []
    process { row, column, td ->
      if (column == 0 && inRowRange(row, grid.size())) {
        result << td.text
      }
    }
    result
  }

  def getObject() {
    object([:])
  }

  def object(Map tableOptions) {
    def result = [:]
    String key
    process { int row, int column, td ->
      if (inRowRange(row, grid.size())) {
        if (column == 0) {
          key = extractKey(tableOptions, td, row)
        } else {
          result[key] = extractValue(tableOptions, td, key)
        }
      }
    }
    result
  }

  def getObjects() {
    objects([:])
  }

  def objects(Map tableOptions) {
    if (tableOptions.orientation == VERTICAL)
      verticalObjects(tableOptions)
    else
      horizontalObjects(tableOptions)
  }

  private def horizontalObjects(Map options) {
    def results = []
    def attributes = [:]
    process { row, column, td ->
      if (row == 0) {
        String key = extractKey(options, td, column)
        attributes[column] = key
      } else if (inRowRange(row - 1, grid.size() - 1)) {
        if (column == 0)
          results << [:]
        if (inColumnRange(column, grid.size())) {
          String key = attributes[column]
          results[-1][key] = extractValue(options, td, key)
        }
      }
    }
    results
  }

  private def verticalObjects(Map options) {
    def results = []
    int resultIndex = 0
    String key
    process { row, column, td ->
      if (column == 0) {
        resultIndex = 0
        key = extractKey(options, td, row)
      } else if (inColumnRange(column - 1, grid[row].size() - 1)) {

        if (row == 0 && column != 0)
          results << [:]

        if (inRowRange(row, grid.size())) {
          results[resultIndex][key] = extractValue(options, td, key)
        }

        resultIndex++
      }

    }
    results
  }

  def columns(String... columnNames) {
    def result = []
    def map = [:]
    def finalize = {
      (map.size()..<columnNames.size()).each { index -> map[columnNames[index]] = "" }
      result << map
    }
    def oldRow = -1
    process { row, column, td ->
      if (inRowRange(row, grid.size())) {
        if (oldRow != -1 && row != oldRow && column == 0) {
          finalize()
          map = [:]
        }
        if (column < columnNames.size() && inRowRange(row, grid.size())) {
          map[columnNames[column]] = td.text
        }
        oldRow = row
      }
    }
    finalize()
    result
  }

  def process(closure) {
    grid.eachWithIndex { columnList, row ->
      columnList.eachWithIndex { td, column ->
        closure(row, column, td)
      }
    }
  }

  private String extractKey(Map tableOptions, BaseElementDsl elementDsl, int index) {
    Closure keyExtractor = tableOptions.keyExtractor ?: camelCaseClosure

    String key = keyExtractor(elementDsl)

    if (tableOptions.names)
      key = tableOptions.names[index] ?: key

    key
  }

  private String extractValue(Map tableOptions, BaseElementDsl elementDsl, String key) {
    Closure valueExtractor = tableOptions.valueExtractors?.get(key) ?: textClosure
    valueExtractor(elementDsl)
  }

  private boolean inRowRange(row, size) {
    gridOptions.rowRange ? new RowRange(gridOptions.rowRange, size).contains(row) : true
  }

  private boolean inColumnRange(column, size) {
    gridOptions.columnRange ? new RowRange(gridOptions.columnRange, size).contains(column) : true
  }
}