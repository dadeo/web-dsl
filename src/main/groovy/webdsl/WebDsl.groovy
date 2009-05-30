package webdsl

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import webdsl.support.FormDsl
import webdsl.support.DslFactory
import webdsl.support.DslHelper
import com.gargoylesoftware.htmlunit.html.HtmlPage

class WebDsl {

  private final WebClient _webClient = new WebClient();
  private static final ThreadLocal container = new ThreadLocal()
  private HtmlPage page

  def bind = [:]
  private factory = new DslFactory()

  def "for"(where) {
    container.set this
    setPage _webClient.getPage(where)
    this
  }

  def "do"(closure) {
    closure.delegate = this
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    use(WebDsl) {
      closure()
    }
  }

  def form(closure) {
    form.do closure
  }

  def getForm() {
    new FormDsl(this, page.getForms()[0])
  }

  protected setPage(newPage) {
    page = newPage
    applyNewMetaClassWithDynamicMethods()
  }

  private def applyNewMetaClassWithDynamicMethods() {
    def emc = new ExpandoMetaClass(WebDsl, false, true)
    page.allHtmlChildElements.each {element ->
      def dsl = factory.create(this, element)
      DslHelper.addGetterMethodsFor(["id", "name"], {-> dsl }, element, emc)
    }
    emc.initialize()
    this.metaClass = emc
  }

  private getTitle() {
    page.getTitleText()
  }

  def methodMissing(String name, args) {
    this[name].do args[0]
  }

  static def click(String string) {
    def found = container.get().page.getAllHtmlChildElements().find {HtmlElement element ->
      element.getTextContent() == string || element.getAttribute("value") == string || element.getAttribute("href") == string
    }

    if (found) {
      container.get().setPage found.click()
    } else {
      throw new RuntimeException("No element found for '$string'")
    }
  }

}
