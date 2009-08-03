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
    table.allHtmlChildElements.each { row ->
      if(row instanceof HtmlTableRow) {
        def map = [:]
        int i = 0
        row.allHtmlChildElements.each { td ->
          if(i < columnNames.size() && td instanceof HtmlTableDataCell) {
            map[columnNames[i]] = td.getTextContent()
            ++i
          }
        }
        (i..<columnNames.size()).each { index -> map[columnNames[index]] = "" }
        result << map
      }
    }
    result
  }
}