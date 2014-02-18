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
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.w3c.dom.Element

class PageElementBuilder extends BuilderSupport {
  private HtmlPage page

  HtmlElement build(HtmlPage page, Closure closure) {
    this.page = page
    Closure c = closure.clone()
    c.delegate = this
    c()
  }

  @Override
  protected void setParent(Object parent, Object child) {
    if(parent instanceof Element)
      parent.appendChild(child)
  }

  @Override
  protected Object createNode(Object name) {
    createNode(name, [:], null)
  }

  @Override
  protected Object createNode(Object name, Object value) {
    createNode(name, [:], value)
  }

  @Override
  protected Object createNode(Object name, Map attributes) {
    createNode(name, attributes, null)
  }

  @Override
  protected Object createNode(Object name, Map attributes, Object value) {
    Element element = page.createElement(name)
    if(value)
      element.textContent = value
    attributes.each { k, v -> element.setAttribute(k, v) }
    element
  }
}