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
package webdsl

import com.gargoylesoftware.htmlunit.CollectingAlertHandler
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.DomText
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.codehaus.groovy.runtime.GStringImpl
import webdsl.support.ChildrenDsl
import webdsl.support.DslFactory
import webdsl.support.FormDsl
import webdsl.support.SelectorDsl
import webdsl.support.css.selector.CssSelector
import webdsl.support.css.selector.CssSelectorParser

class WebDsl {
  private static final ThreadLocal container = new ThreadLocal()

  WebClient webClient
  HtmlPage page
  def bind = [:]

  private boolean factoryResets = true
  private DslFactory factory = new DslFactory()
  private List<String> alerts = []

  WebDsl() {
    initWebClient()
  }

  WebDsl(String url) {
    this()
    init(url)
  }

  WebDsl init(String url) {
    container.set this
    setPage webClient.getPage(url)
    this
  }

  WebDsl _for(String url) {
    init(url)
  }

  def _do(closure) {
    if (factoryResets) {
      factory = new DslFactory()
    }

    closure.delegate = this
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    use(WebDsl) {
      closure()
    }
  }

  def openNewClient(where) {
    initWebClient()
    _for(where)
  }

  private def initWebClient() {
    webClient = new WebClient()
    webClient.setAjaxController(new NicelyResynchronizingAjaxController())
    webClient.alertHandler = new CollectingAlertHandler(alerts)
  }

  def form(closure) {
    form.do closure
  }

  def getForm() {
    new FormDsl(this, factory, page.getForms()[0])
  }

  protected void setPage(HtmlPage newPage) {
    page = newPage
  }

  private getTitle() {
    page.getTitleText()
  }

  def back() {
    webClient.currentWindow.getHistory().back()
    page = webClient.currentWindow.enclosedPage
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
    def element = createDslForElement(name)
    if (element) return factory.create(this, element)
    def possibleName = '$' + name
    if (metaClass.hasProperty(this, possibleName)) {
      return getProperty(possibleName)
    }
    def selectors = findSelectorsFor(name)
    if (selectors) {
      return selectors
    }
    throw new MissingPropertyException(name, WebDsl)
  }

  private createDslForElement(String name) {
    page.htmlElementDescendants.find {
      it.getAttribute('id') == name || it.getAttribute('name') == name
    }
  }

  def findSelectorsFor(name) {
    def result = new SelectorDsl(this, factory)
    page.body.children.each { element ->
      if (element.class != DomText && element.tagName == name) {
        result << element
      }
    }
    if (!result.selected) throw new MissingPropertyException(name, this.class)
    result
  }

  def properties() {
    def result = []
    page.htmlElementDescendants.each {
      String id = it.getAttribute('id')
      if (id) result << id

      String name = it.getAttribute('name')
      if (name) result << name
    }
    result
  }

  List<HtmlElement> findElementsByNameOrId(String nameOrId) {
    page.getElementsByIdAndOrName(nameOrId)
  }

  def getChildren() {
    new ChildrenDsl().children(this, page)
  }

  def children(options) {
    new ChildrenDsl().children(this, page, options)
  }

  def handle(Class elementClass) {
    [
        with: { dslClass ->
          factory.register(dslClass, elementClass)
        }
    ]
  }

  def $(selector) {
    def dslElements = $$(selector)
    dslElements[0]
  }

  def $$(selector) {
    CssSelector cssSelector = new CssSelectorParser().parse(selector)


    def dslElements = cssSelector.select(page)
                                 .unique()
                                 .sort(elementSortOrder())
                                 .collect { factory.create(this, it) }

    dslElements
  }

  List<String> getAlerts() {
    alerts
  }

  private Closure elementSortOrder() {
    int counter = 0
    Map<HtmlElement, Integer> sortOrder = ((HtmlPage) page).htmlElementDescendants.toList().collectEntries { [it, counter++] }
    return { HtmlElement it -> sortOrder[it] }
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
    map.each { k, v ->
      result[camel(k)] = v
    }
    result
  }

  static def click(String string) {
    def dsl = container.get()
    def found = dsl.page.tabbableElements.find { HtmlElement element ->
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

  static def getText(GStringImpl string) {
    getIntern(string).text
  }

  static def getValue(String string) {
    getIntern(string).value
  }

  static def getValue(GStringImpl string) {
    getIntern(string).value
  }

  static def setValue(String string, value) {
    getIntern(string).value = value
  }

  static def setValue(GStringImpl string, value) {
    getIntern(string).value = value
  }

  static def map(Map target, mappings) {
    def result = [:]
    target.each { k, v ->
      def newKey = mappings[k]
      result[newKey ?: k] = v
    }
    result
  }

  static def getIntern(String name) {
    container.get()."$name"
  }

}
