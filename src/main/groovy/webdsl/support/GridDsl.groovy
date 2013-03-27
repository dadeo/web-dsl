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

import webdsl.WebDsl
import com.gargoylesoftware.htmlunit.html.HtmlSpan


class GridDsl {
  def grid = []
  def options

  GridDsl(options = [:]) {
    this.options = options
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
      if(column == 0) {
        result << [:]
      }
      td.htmlElementDescendants.each { span ->
        if(span instanceof HtmlSpan && span.getAttribute("name")) {
          result[-1][span.getAttribute("name")] = span.getTextContent()
        }
      }
    }
    result
  }

  def getList() {
    def result = []
    process { row, column, td ->
      if(column == 0 && inRowRange(row, grid.size())){
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
        if(column == 0) {
          attribute = td.textContent.trim()
        } else {
          result[WebDsl.camel(attribute)] = td.textContent.trim()
        }
      }
    }
    result
  }

  def getObjects() {
    objects()
  }

  def objects(String... names) {
    def results = []
    def attributes = [:]
    process { row, column, td ->
      if (row == 0) {
        attributes[column] = names ? names[column] : td.textContent.trim()
      } else if (inRowRange(row - 1, grid.size() - 1)){
        if(column == 0) {
          results << [:]
        }
        results[-1][WebDsl.camel(attributes[column])] = td.textContent.trim()
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
        if(oldRow != -1 && row != oldRow && column == 0) {
          finalize()
          map = [:]
        }
        if(column < columnNames.size() && inRowRange(row, grid.size())) {
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
    options.rowRange ? new RowRange(options.rowRange, size).contains(row) : true
  }
}