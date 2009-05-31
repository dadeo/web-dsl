package webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlForm
import com.gargoylesoftware.htmlunit.html.HtmlTable
import com.gargoylesoftware.htmlunit.html.HtmlSelect
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList


class DslFactory {
  def create(pageContainer, element) {
    if (element instanceof HtmlForm) {
      return new FormDsl(pageContainer, element)
    } else if (element instanceof HtmlTable) {
      return new TableDsl(element)
    } else if (element instanceof HtmlSelect) {
      return new SelectDsl(pageContainer, element)
    } else if (element instanceof HtmlRadioButtonInput) {
      return new RadioButtonDsl(pageContainer, element)
    } else if (element instanceof HtmlCheckBoxInput) {
      return new CheckBoxDsl(pageContainer, element)
    } else if (element instanceof HtmlUnorderedList || element instanceof HtmlOrderedList) {
      return new ListDsl(pageContainer, element)
    }
    new ElementDsl(pageContainer, element)
  }
}