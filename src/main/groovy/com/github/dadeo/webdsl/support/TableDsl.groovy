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
package com.github.dadeo.webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlTable

class TableDsl extends BaseElementDsl {
  private HtmlTable table

  TableDsl(pageContainer, DslFactory factory, table) {
    super(pageContainer, factory, table)
    this.table = table
  }

  def getName() {
    table.getAttribute("name")
  }

  def getBy() {
    "do"()
  }

  def getAs() {
    "do"()
  }

  def process(Closure closure) {
    process null, closure
  }

  def process(Map options, Closure closure) {
    def startIndex = options?.offset ?: 0
    def startColumn = options?.column ?: 0
    int rowIndex = 0
    if(startIndex < table.rows.size()) {
      table.rows[startIndex..-1].each { row ->
        int columnIndex = 0
        row.cells[startColumn..-1].each { td ->
          closure rowIndex, columnIndex, factory.create(pageContainer, td)
          ++columnIndex
        }
        ++rowIndex
      }
    }
  }

  def "do"(options = [:]) {
    def grid = new GridDsl(pageContainer, factory, options)
    process options, { row, column, td ->
      if (column == 0) {
        grid.nextRow td
      } else {
        grid.appendColumn td
      }
    }
    grid
  }

  def propertyMissing(String name) {
    if (name == "tbody" || name == "thead") {
      super.propertyMissing(name)
    } else {
      super.propertyMissing("tbody")[0][name]
    }
  }

}