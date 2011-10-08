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

import com.gargoylesoftware.htmlunit.html.HtmlTable
import com.gargoylesoftware.htmlunit.html.HtmlTableRow
import com.gargoylesoftware.htmlunit.html.HtmlSpan
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell
import webdsl.WebDsl


class TableDsl extends BaseElementDsl {
  private HtmlTable table

  TableDsl(pageContainer, table) {
    super(pageContainer, table)
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

  def process(closure) {
    int rowIndex = 0
    table.htmlElementDescendants.each { row ->
      if(row instanceof HtmlTableRow) {
        int columnIndex = 0
        row.htmlElementDescendants.each { td ->
          if(td instanceof HtmlTableDataCell) {
            closure rowIndex, columnIndex, td
            ++columnIndex
          }
        }
        ++rowIndex
      }
    }
  }

  def "do"(options) {
    def grid = new GridDsl()
    def startIndex = options?.offset ?: 0
    def startColumn = options?.column ?: 0
    process { row, column, td ->
      if(row < startIndex) {
        // do nothing
      } else if (column < startColumn) {
        // do nothing
      } else if (column == startColumn) {
        grid.nextRow td
      } else {
        grid.appendColumn td
      }
    }
    grid
  }

  def propertyMissing(String name) {
    if(name == "tbody" || name == "thead") {
      super.propertyMissing(name)
    } else {
      super.propertyMissing("tbody")[0][name]
    }
  }

}