package webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlLabel


class ElementDsl {
  def HtmlElement element
  def pageContainer
  def factory = new DslFactory()

  ElementDsl(pageContainer, element) {
    this.element = element
    this.pageContainer = pageContainer
  }

  HtmlElement getTarget() {
    element
  }

  def click() {
    pageContainer.page = element.click()
  }

  def getId() {
    element.getAttribute("id")
  }

  def getName() {
    element.getAttribute("name")
  }

  def getText() {
    element.getTextContent()
  }

  def getValue() {
    element.getAttribute("value")
  }

  def setValue(value) {
    element.setAttribute("value", value)
  }

  def getChildren() {
    element.allHtmlChildElements.collect { factory.create(pageContainer, it) }
  }

  def children(options) {
    def result = []
    element.getAllHtmlChildElements().each {HtmlElement child ->
      if (options?.type && !isType(options.type, child)) return
      if (options?.types && !isInTypes(options.types, child)) return
      result << factory.create(pageContainer, child)
    }
    result
  }

  private boolean isInTypes(types, element) {
    types.find { isType it, element }
  }

  private boolean isType(type, HtmlElement element) {
    element.tagName == type
  }

  def findLabelFor(what) {
    if (!(what instanceof String)) {
      what = what.getAttribute("id")
    }
    pageContainer.getForm().target.allHtmlChildElements.find {element ->
      element instanceof HtmlLabel && element.getAttribute("for") == what
    }
  }

  def tableValue(attributeName) {
    value
  }
}