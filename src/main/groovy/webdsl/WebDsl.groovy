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
import com.gargoylesoftware.htmlunit.html.HtmlAnchor
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import com.gargoylesoftware.htmlunit.util.NameValuePair
import groovy.transform.CompileStatic
import org.codehaus.groovy.runtime.GStringImpl
import webdsl.support.BaseElementDsl
import webdsl.support.DslFactory
import webdsl.support.FormDsl
import webdsl.support.PageContainer
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

  @CompileStatic
  WebDsl init(String url) {
    container.set this
    setPage webClient.getPage(url)
    this
  }

  @CompileStatic
  WebDsl _for(String url) {
    init(url)
  }

  def _do(Closure closure) {
    if (factoryResets) {
      factory = new DslFactory()
    }

    closure.delegate = this
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    use(WebDsl) {
      closure()
    }
  }

  @CompileStatic
  def openNewClient(String where) {
    initWebClient(webClient.webConnection, new Options())
    _for(where)
  }

  @CompileStatic
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

  @CompileStatic
  def form(Closure closure) {
    form._do closure
  }

  @CompileStatic
  FormDsl getForm() {
    new FormDsl(this, factory, htmlPage.getForms()[0])
  }

  @CompileStatic
  void setPage(Page newPage) {
    page = newPage
  }

  @CompileStatic
  HtmlPage getHtmlPage() {
    assert page instanceof HtmlPage, page.class
    (HtmlPage) page
  }

  @CompileStatic
  String getTitle() {
    htmlPage.getTitleText()
  }

  @CompileStatic
  def back() {
    webClient.currentWindow.getHistory().back()
    page = webClient.currentWindow.enclosedPage
  }

  @CompileStatic
  def exists(String elementName, Closure trueClosure = { it }, Closure falseClosure = { false }) {
    try {
      def element = getProperty(elementName)
      trueClosure(element)
    } catch (MissingPropertyException e) {
      falseClosure()
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

  @CompileStatic
  def propertyMissing(String name) {
    def element = findElementByPropertyValues(name)
    if (element) return factory.create(this, element)

    def possibleName = '$' + name
    if (metaClass.hasProperty(this, possibleName)) {
      return getProperty(possibleName)
    }
    throw new MissingPropertyException(name, WebDsl)
  }

  @CompileStatic
  private findElementByPropertyValues(String searchString) {
    def name = null
    def value = null
    def href = null
    def text = null

    def element = htmlPage.htmlElementDescendants.find { HtmlElement element ->
      if (element.getAttribute('id') == searchString)
        return true

      if (!name && element.getAttribute('name') == searchString)
        name = element

      if (!value && element.getAttribute("value") == searchString)
        value = element

      if (!href && element.getAttribute("href") == searchString)
        href = element

      if (!text && (element instanceof HtmlAnchor) && element.getTextContent() == searchString)
        text = element

      false
    }

    element ?: name ?: value ?: href ?: text ?: $(searchString)
  }

  @CompileStatic
  List<String> properties() {
    List<String> result = []
    htmlPage.htmlElementDescendants.each { HtmlElement it ->
      String id = it.getAttribute('id')
      if (id) result << id

      String name = it.getAttribute('name')
      if (name) result << name
    }
    result
  }

  def handle(Class elementClass) {
    [
        with: { dslClass ->
          factory.register(dslClass, elementClass)
        }
    ]
  }

  @CompileStatic
  BaseElementDsl $(String selector, target = htmlPage) {
    def dslElements = $$(selector, target)
    dslElements[0]
  }

  @CompileStatic
  List<? extends BaseElementDsl> $$(String selector, target = htmlPage) {
    CssSelector cssSelector = new CssSelectorParser().parse(selector)


    List<? extends BaseElementDsl> dslElements = cssSelector.select(target)
                                                            .unique().toList()

    if (dslElements.size() > 1)
      dslElements.sort(true, elementSortOrder())

    dslElements.collect { factory.create(this, it) }.toList()
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

  static String camel(String string) {
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

  static Map<String, ?> camel(Map<String, ?> map) {
    def result = [:]
    map.each { k, v ->
      result[camel(k)] = v
    }
    result
  }

  static def click(String string) {
    getIntern(string).click()
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

  static def map(Map target, Map mappings) {
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
