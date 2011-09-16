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

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.codehaus.groovy.runtime.GStringImpl
import webdsl.support.DslFactory
import webdsl.support.FormDsl
import webdsl.support.SelectorDsl

class WebDsl {

  def WebClient webClient
  private static final ThreadLocal container = new ThreadLocal()
  def HtmlPage page

  def bind = [:]
  private factory = new DslFactory()

  WebDsl() {
    initWebClient()
  }

  def _for(where) {
    container.set this
    setPage webClient.getPage(where)
    this
  }

  def _do(closure) {
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
  }

  def form(closure) {
    form.do closure
  }

  def getForm() {
    new FormDsl(this, page.getForms()[0])
  }

  protected void setPage(HtmlPage newPage) {
    page = newPage
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
    def element = createDslForElement(name)
    if(element) return factory.create(this, element)
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
    page.allHtmlChildElements.find {
      it.getAttribute('id') == name || it.getAttribute('name') == name
    }
  }

  def findSelectorsFor(name) {
    def result = new SelectorDsl(this, factory)
    page.body.children.each { element ->
      if(element.tagName == name) {
        result << element
      }
    }
    result
  }

  def properties() {
    def result = []
    page.allHtmlChildElements.each {
      String id = it.getAttribute('id')
      if(id) result << id

      String name = it.getAttribute('name')
      if(name) result << name
    }
    result
  }

  List<HtmlElement> findElementsByNameOrId(String nameOrId) {
    page.allHtmlChildElements.findAll {
      it.getAttribute('id') == nameOrId || it.getAttribute('name') == nameOrId
    }
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
    def found = dsl.page.tabbableElements.find {HtmlElement element ->
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
    target.each {k, v ->
      def newKey = mappings[k]
      result[newKey ?: k] = v
    }
    result
  }

  static def getIntern(String name) {
    container.get()."$name"
  }

}
