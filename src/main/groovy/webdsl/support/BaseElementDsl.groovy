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

class BaseElementDsl {
  HtmlElement element
  def pageContainer
  DslFactory factory

  BaseElementDsl(pageContainer, DslFactory factory, element) {
    this.pageContainer = pageContainer
    this.factory = factory
    this.element = element
  }

  def propertyMissing(String name) {
    def selectors = findSelectorsFor(name)
    if (selectors.size()) {
      return selectors
    }
    throw new MissingPropertyException(name, ElementDsl)
  }

  def findSelectorsFor(name) {
    def result = new SelectorDsl(pageContainer, factory)
    element.children.each { element ->
      if (element.metaClass.hasProperty(element, "tagName") && element.tagName == name) {
        result << element
      }
    }
    result
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

  boolean hasClass(String className) {
    element.getAttribute('class').contains(className)
  }

  boolean hasAttribute(String attributeName) {
    element.attributes.find { k, v -> k == attributeName }
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

}