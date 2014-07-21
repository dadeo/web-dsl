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
      td.htmlElementDescendants.each { span ->
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
        result << td.textContent.trim()
      }
    }
    result
  }

  def getObject() {
    def result = [:]
    def attribute
    process { row, column, td ->
      if (inRowRange(row, grid.size())) {
        if (column == 0) {
          attribute = td.textContent.trim()
        } else {
          result[WebDsl.camel(attribute)] = td.textContent.trim()
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
        String key = WebDsl.camel(td.textContent.trim())
        attributes[column] = options.names ? options.names[column] ?: key : key
      } else if (inRowRange(row - 1, grid.size() - 1)) {
        if (column == 0)
          results << [:]
        if (inColumnRange(column, grid.size()))
          results[-1][attributes[column]] = td.textContent.trim()
      }
    }
    results
  }

  private def verticalObjects(Map options) {
    def results = []
    int resultIndex = 0
    String key
    process { row, column, td ->
      BaseElementDsl elementDsl = factory.create(pageContainer, td)

      if (column == 0) {
        resultIndex = 0
        key = WebDsl.camel(elementDsl.text)
        if (options.names)
          key = options.names[row] ?: key
      } else if (inColumnRange(column - 1, grid[row].size() - 1)) {

        if (row == 0 && column != 0)
          results << [:]

        if (inRowRange(row, grid.size())) {
          results[resultIndex][key] = elementDsl.text
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
          map[columnNames[column]] = td.textContent.trim()
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

  boolean inRowRange(row, size) {
    gridOptions.rowRange ? new RowRange(gridOptions.rowRange, size).contains(row) : true
  }

  boolean inColumnRange(column, size) {
    gridOptions.columnRange ? new RowRange(gridOptions.columnRange, size).contains(column) : true
  }
}