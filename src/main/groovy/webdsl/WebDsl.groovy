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

import com.gargoylesoftware.htmlunit.*
import com.gargoylesoftware.htmlunit.html.DomText
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.util.NameValuePair
import org.codehaus.groovy.runtime.GStringImpl
import webdsl.support.*
import webdsl.support.css.selector.CssSelector
import webdsl.support.css.selector.CssSelectorParser

class WebDsl implements PageContainer {
  private static final ThreadLocal container = new ThreadLocal()

  WebClient webClient
  Page page

  private boolean factoryResets = true
  private DslFactory factory = new DslFactory()
  private List<String> alerts = []

  WebDsl(Options options = new Options()) {
    this((WebConnection) null, options)
  }

  WebDsl(WebConnection webConnection, Options options = new Options()) {
    initWebClient(webConnection, options)
  }

  WebDsl(String url, WebConnection webConnection, Options options = new Options(), Closure customizer = null) {
    this(webConnection, options)
    customizer?.call(this)
    init(url)
  }

  WebDsl(String url, Options options = new Options(), Closure customizer = null) {
    this(options)
    customizer?.call(this)
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
    initWebClient(webClient.webConnection, new Options())
    _for(where)
  }

  private def initWebClient(WebConnection webConnection, Options options) {
    this.webClient = new WebClient(options.browserVersion)
    if (webConnection)
      this.webClient.webConnection = webConnection
    this.webClient.setAjaxController(new NicelyResynchronizingAjaxController())
    this.webClient.alertHandler = new CollectingAlertHandler(alerts)
    this.webClient.options.useInsecureSSL = options.useInsecureSSL
    this.webClient.options.throwExceptionOnFailingStatusCode = options.throwExceptionOnFailingStatusCode
    this.webClient.options.printContentOnFailingStatusCode = options.printContentOnFailingStatusCode
    this.webClient.options.javaScriptEnabled = options.javaScriptEnabled
  }

  def form(closure) {
    form.do closure
  }

  def getForm() {
    new FormDsl(this, factory, htmlPage.getForms()[0])
  }

  void setPage(Page newPage) {
    page = newPage
  }

  HtmlPage getHtmlPage() {
    assert page instanceof HtmlPage, page.class
    (HtmlPage) page
  }

  private getTitle() {
    htmlPage.getTitleText()
  }

  def back() {
    webClient.currentWindow.getHistory().back()
    page = webClient.currentWindow.enclosedPage
  }

  boolean exists(String elementName) {
    try {
      getProperty(elementName)
      true
    } catch (MissingPropertyException e) {
      false
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
    throw new MissingPropertyException(name, WebDsl)
  }

  private createDslForElement(String name) {
    htmlPage.htmlElementDescendants.find {
      it.getAttribute('id') == name || it.getAttribute('name') == name
    }
  }

  def properties() {
    def result = []
    htmlPage.htmlElementDescendants.each {
      String id = it.getAttribute('id')
      if (id) result << id

      String name = it.getAttribute('name')
      if (name) result << name
    }
    result
  }

  List<HtmlElement> findElementsByNameOrId(String nameOrId) {
    htmlPage.getElementsByIdAndOrName(nameOrId)
  }

  def getChildren() {
    new ChildrenDsl().children(this, htmlPage)
  }

  def children(options) {
    new ChildrenDsl().children(this, htmlPage, options)
  }

  def handle(Class elementClass) {
    [
        with: { dslClass ->
          factory.register(dslClass, elementClass)
        }
    ]
  }

  BaseElementDsl $(String selector, target = htmlPage) {
    def dslElements = $$(selector, target)
    dslElements[0]
  }

  List<BaseElementDsl> $$(String selector, target = htmlPage) {
    CssSelector cssSelector = new CssSelectorParser().parse(selector)


    def dslElements = cssSelector.select(target)
                                 .unique()

    if (dslElements.size() > 1)
      dslElements.sort(elementSortOrder())

    dslElements.collect { factory.create(this, it) }
  }

  List<String> getAlerts() {
    alerts
  }

  void setJavaScriptEnabled(boolean value) {
    webClient.options.javaScriptEnabled = value
  }

  boolean isJavaScriptEnabled() {
    webClient.options.javaScriptEnabled
  }

  void enableJavaScript() {
    javaScriptEnabled = true
  }

  void disableJavaScript() {
    javaScriptEnabled = false
  }

  void httpGet(URL url, Map<String, Object> params = [:]) {
    httpGet url.toString(), params
  }

  void httpGet(String url, Map<String, Object> params = [:]) {
    http HttpMethod.GET, url, params
  }

  void httpPost(URL url, Map<String, Object> params) {
    httpPost url.toString(), params
  }

  void httpPost(String url, Map<String, Object> params) {
    http HttpMethod.POST, url, params
  }

  void http(HttpMethod method, String url, Map<String, Object> params) {
    WebRequest request = new WebRequest(new URL(url), method)
    request.requestParameters = params.collect { k, v -> new NameValuePair(k, v?.toString() ?: '') }
    page = webClient.getPage(webClient.currentWindow, request)
  }

  private Closure elementSortOrder() {
    int counter = 0
    Map<HtmlElement, Integer> sortOrder = ((HtmlPage) page).htmlElementDescendants.collectEntries { [it, counter++] }
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
    def found = dsl.htmlPage.tabbableElements.find { HtmlElement element ->
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

  boolean getThrowExceptionOnFailingStatusCode() {
    webClient.options.throwExceptionOnFailingStatusCode
  }

  void setThrowExceptionOnFailingStatusCode(boolean throwException) {
    webClient.options.throwExceptionOnFailingStatusCode = throwException
  }

  boolean getPrintContentOnFailingStatusCode() {
    webClient.options.printContentOnFailingStatusCode
  }

  void setPrintContentOnFailingStatusCode(boolean throwException) {
    webClient.options.printContentOnFailingStatusCode = throwException
  }

  static class Options {
    BrowserVersion browserVersion = BrowserVersion.default
    boolean javaScriptEnabled = true
    boolean printContentOnFailingStatusCode = false
    boolean throwExceptionOnFailingStatusCode = false
    boolean useInsecureSSL = true
  }
}
