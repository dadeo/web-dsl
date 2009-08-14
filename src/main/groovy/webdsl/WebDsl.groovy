package webdsl

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import webdsl.support.FormDsl
import webdsl.support.DslFactory
import webdsl.support.DslHelper
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.codehaus.groovy.runtime.metaclass.ClosureMetaMethod

class WebDsl {

  def final WebClient _webClient = new WebClient();
  private static final ThreadLocal container = new ThreadLocal()
  def HtmlPage page

  def bind = [:]
  private factory = new DslFactory()

  def _for(where) {
    container.set this
    setPage _webClient.getPage(where)
    this
  }

  def _do(closure) {
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

  protected void setPage(HtmlPage newPage) {
    page = newPage
    applyNewMetaClassWithDynamicMethods()
  }

  private def applyNewMetaClassWithDynamicMethods() {
    new DslHelper().addGetterMethodsForAll(page.allHtmlChildElements, ['id', 'name'], this)
  }

  private getTitle() {
    page.getTitleText()
  }

  private boolean exists(String elementName) {
    try {
      getProperty(elementName)
      return true
    } catch (MissingPropertyException e) {
      return false
    }
  }

  def methodMissing(String name, args) {
    switch (name) {
      case "for":
        return _for(* args)
      case "do":
        return _do(* args)
    }
    this[name].do args[0]
  }

  def propertyMissing(String name) {
    def possibleName = '$' + name
    if (hasProperty(possibleName)) {
      return getProperty(possibleName)
    }
    throw new MissingPropertyException(name, WebDsl)
  }

  def properties() {
    def result = []
    metaClass.methods.each {method ->
      if (method instanceof ClosureMetaMethod && !method.closure.parameterTypes) {
        result << DslHelper.fromGetter(method.name)
      }
    }
    result
  }

  static def camel(String string) {
    def buffer = new StringBuffer()

    int match = 0
    def matcher = string =~ /\b(.)(\w*)\s*/
    while (matcher.find()) {
      def toCase = match == 0 ? "toLowerCase" : "toUpperCase"
      matcher.appendReplacement(buffer, matcher.group(1)."$toCase"() + matcher.group(2).toLowerCase())
      ++match
    }
    matcher.appendTail(buffer)

    buffer.toString()
  }

  static def camel(Map map) {
    def result = [:]
    map.each {k, v ->
      result[camel(k)] = v
    }
    result
  }

  static def click(String string) {
    def dsl = container.get()
    def found = dsl.page.getAllHtmlChildElements().find {HtmlElement element ->
      element.getTextContent() == string || element.getAttribute("value") == string || element.getAttribute("href") == string
    }

    if (found) {
      dsl.setPage found.click()
    } else {
      getIntern(string).click()
    }
  }

  static def getText(String string) {
    getIntern(string).text
  }

  static def getValue(String string) {
    getIntern(string).value
  }

  static def getIntern(String name) {
    def dsl = container.get()
    if (dsl.hasProperty(name)) {
      return container.get().getProperty(name)
    } else {
      def possibleProperty = "\$${name}"
      if (dsl.hasProperty(possibleProperty)) {
        return container.get().getProperty(possibleProperty)
      } else {
        throw new RuntimeException("No element found for '$name'")
      }
    }
  }

}
