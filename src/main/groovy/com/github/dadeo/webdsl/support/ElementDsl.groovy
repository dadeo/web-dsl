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
package com.github.dadeo.webdsl.support

import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlLabel


class ElementDsl extends BaseElementDsl {

  ElementDsl(pageContainer, DslFactory factory, element) {
    super(pageContainer, factory, element)
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
    untrimmedText.trim().replaceAll(/\s+/, ' ')
  }

  def getUntrimmedText() {
    element.getTextContent()
  }

  def setText(String text) {
    element.setTextContent(text)
  }

  def getValue() {
    text
  }

  def getUntrimmedValue() {
    untrimmedText
  }

  def setValue(value) {
    element.setTextContent(value)
  }

  def getChildren() {
    new ChildrenDsl().children(pageContainer, element)
  }

  def children(options) {
    new ChildrenDsl().children(pageContainer, element, options)
  }

  def getLabel() {
    findLabelFor(id)?.getTextContent() ?: ""
  }

  def findLabelFor(what) {
    if (!(what instanceof String)) {
      what = what.getAttribute("id")
    }
    pageContainer.page.htmlElementDescendants.find {element ->
      element instanceof HtmlLabel && element.getAttribute("for") == what
    }
  }

  def tableValue(attributeName) {
    value
  }

}