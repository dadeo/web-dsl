package webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlForm
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput
import com.gargoylesoftware.htmlunit.html.HtmlLabel


class FormDsl {
  private HtmlForm form
  private pageContainer
  private target
  private factory = new DslFactory()

  FormDsl(pageContainer, form) {
    this.pageContainer = pageContainer
    this.target = form
    this.form = form
  }

  def _do(closure) {
    closure.delegate = this
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure()
  }

  def getSubmit() {
    pageContainer.page = form.getAllHtmlChildElements().find {it.getAttribute("type") == "submit"}.click()
  }

  public Map values() {
    def result = grabValuesFor("name")

    result
  }

  public Map valuesById() {
    def result = grabValuesFor("id")
    result
  }

  private Map grabValuesFor(attributeName) {
    def result = [:]
    form.allHtmlChildElements.each {element ->
      def item = factory.create(pageContainer, element)
      def attributeValue = item[attributeName]
      if (attributeValue && !(element instanceof HtmlSubmitInput)) {
        result[attributeValue] = item.tableValue(attributeName)
      }
    }
    result
  }

  def findLabelFor(what) {
    form.allHtmlChildElements.find {element ->
      element instanceof HtmlLabel && element.getAttribute("for") == what
    }
  }

  def methodMissing(String name, args) {
    if(name == "do") {
      return _do(*args)
    }
  }
}