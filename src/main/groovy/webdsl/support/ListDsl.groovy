package webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlListItem


class ListDsl extends ElementDsl {
  ListDsl(pageContainer, element) {
    super(pageContainer, element)
  }

  @Override
  def getValue() {
    def result = []
    element.allHtmlChildElements.each { child ->
      if(child instanceof HtmlListItem) {
        result << child.getTextContent()
      }
    }
    result
  }

}