package webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlTable
import com.gargoylesoftware.htmlunit.html.HtmlTableRow
import com.gargoylesoftware.htmlunit.html.HtmlSpan
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell


class TableDsl {

  private HtmlTable table

  TableDsl(table) {
    this.table = table
  }

  def getName() {
    table.getAttribute("name")
  }

  def getBy() {
    this
  }

  def getSpan() {
    def result = []
    table.allHtmlChildElements.each { row ->
      if(row instanceof HtmlTableRow) {
        def map = [:]
        row.allHtmlChildElements.each { span ->
          if(span instanceof HtmlSpan && span.getAttribute("name")) {
            map[span.getAttribute("name")] = span.getTextContent()
          }
        }
        result << map
      }
    }
    result
  }

  def columns(columnNames) {
    def result = []
    def map = [:]
    def finalize = {
      (map.size()..<columnNames.size()).each { index -> map[columnNames[index]] = "" }
      result << map
    }
    process { row, column, content ->
      if(row != 0 && column == 0) {
        finalize()
        map = [:]
      }
      if(column < columnNames.size()) {
        map[columnNames[column]] = content
      }
    }
    finalize()
    result
  }

  def asObject() {
    def result = [:]
    def attribute
    process { row, column, content ->
      if(column == 0) {
        attribute = content
      } else {
        result[attribute] = content
      }
    }
    result
  }

  def list(options) {
    def selectColumn = options?.column ?: 0
    def result = []
    process { row, column, content ->
      if(column == selectColumn && (!options?.offset || row >= options.offset)) {
        result << content
      }
    }
    result
  }

  def process(closure) {
    int rowIndex = 0
    table.allHtmlChildElements.each { row ->
      if(row instanceof HtmlTableRow) {
        int columnIndex = 0
        row.allHtmlChildElements.each { td ->
          if(td instanceof HtmlTableDataCell) {
            closure rowIndex, columnIndex, td.textContent.trim()
            ++columnIndex
          }
        }
        ++rowIndex
      }
    }
  }
}