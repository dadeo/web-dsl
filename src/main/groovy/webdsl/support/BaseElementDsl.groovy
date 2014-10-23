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
package webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration
import webdsl.support.css.selector.CssSelector
import webdsl.support.css.selector.CssSelectorParser
import webdsl.support.css.selector.InsideCssSelector

class BaseElementDsl {
  HtmlElement element
  PageContainer pageContainer
  DslFactory factory

  BaseElementDsl(PageContainer pageContainer, DslFactory factory, element) {
    this.pageContainer = pageContainer
    this.factory = factory
    this.element = element
  }

  String asText() {
    element.asText()
  }

  CSSStyleDeclaration getStyle() {
    element.scriptObject.currentStyle
  }

  String attr(name) {
    getAttribute(name)
  }

  String getAttribute(name) {
    element.getAttribute(name)
  }

  Map<String, String> getAttributes() {
    element.attributesMap.collectEntries { k, v -> [k, v.textContent] }
  }

  void setAttribute(String name, String value) {
    element.setAttribute(name, value)
    clearScriptObjectIfEventAttribute(name)
  }

  void modifyAttribute(String name, Closure<String> modifier) {
    element.setAttribute(name, modifier(element.getAttribute(name)))
    clearScriptObjectIfEventAttribute(name)
  }

  boolean hasClass(String className) {
    element.getAttribute('class').split(/\s+/).contains(className)
  }

  boolean hasAttribute(String attributeName) {
    element.attributes.find { k, v -> k == attributeName }
  }

  boolean isDisabled() {
    hasAttribute('disabled')
  }

  String getTagName() {
    element.tagName
  }

  void insertBefore(Closure closure) {
    HtmlElement newElement = new PageElementBuilder().build(pageContainer.page, closure)
    element.insertBefore(newElement)
  }

  void insertAfter(Closure closure) {
    HtmlElement newElement = new PageElementBuilder().build(pageContainer.page, closure)
    if (element.nextSibling)
      element.nextSibling.insertBefore(newElement)
    else
      element.parentNode.appendChild(newElement)
  }

  void prependChild(Closure closure) {
    HtmlElement newElement = new PageElementBuilder().build(pageContainer.page, closure)
    if (element.firstChild)
      element.firstChild.insertBefore(newElement)
    else
      element.appendChild(newElement)
  }

  void appendChild(Closure closure) {
    HtmlElement newElement = new PageElementBuilder().build(pageContainer.page, closure)
    element.appendChild(newElement)
  }

  String asXml() {
    element.asXml()
  }

  BaseElementDsl $(String cssSelector) {
    pageContainer.$(cssSelector, element)
  }

  List<BaseElementDsl> $$(String cssSelector) {
    pageContainer.$$(cssSelector, element)
  }

  def closest(String cssSelectorPattern) {
    CssSelector selector = new CssSelectorParser().parse(cssSelectorPattern)

    if (selector instanceof InsideCssSelector)
      closestSimple(selector.cssSelectors[0])
    else
      closestComplex(selector)
  }

  protected def closestSimple(CssSelector selector) {
    HtmlElement candidate = element.parentNode
    while (candidate && !selector.select(candidate)) {
      candidate = candidate.parentNode != pageContainer.page ? candidate.parentNode : null
    }

    if (candidate)
      factory.create(pageContainer, candidate)
  }

  protected def closestComplex(CssSelector selector) {
    List candidates = selector.select(pageContainer.page)

    HtmlElement candidate = element.parentNode

    while (candidate && !candidates.find { it == candidate }) {
      candidate = candidate.parentNode != pageContainer.page ? candidate.parentNode : null
    }

    if (candidate)
      factory.create(pageContainer, candidate)
  }

  protected void clearScriptObjectIfEventAttribute(String name) {
    if (name.startsWith('on'))
      element.scriptObject = null
  }
}