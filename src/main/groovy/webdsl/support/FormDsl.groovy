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

import com.gargoylesoftware.htmlunit.html.HtmlForm
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput
import com.gargoylesoftware.htmlunit.html.HtmlLabel


class FormDsl extends BaseElementDsl {
  private HtmlForm form
  private pageContainer
  private target
  private factory = new DslFactory()

  FormDsl(pageContainer, form) {
    super(pageContainer, form)
    this.pageContainer = pageContainer
    this.target = form
    this.form = form
  }

  def _do(closure) {
    closure.delegate = this
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    closure()
  }

  def getSubmit() {
    pageContainer.page = form.htmlElementDescendants.find {it.getAttribute("type") == "submit"}.click()
  }

  public Map values() {
    def result = grabValuesFor("name")

    result
  }

  public Map valuesById() {
    def result = grabValuesFor("id")
    result
  }

  private Map grabValuesFor(attributeName) {
    def result = [:]
    form.htmlElementDescendants.each {element ->
      def item = factory.create(pageContainer, element)
      def attributeValue = item[attributeName]
      if (attributeValue && !(element instanceof HtmlSubmitInput)) {
        result[attributeValue] = item.tableValue(attributeName)
      }
    }
    result
  }

  def fillInWith(Map values) {
    pageContainer.do {
      values.each {k, v ->
        if(exists(k)) {
          k.value = v
        }
      }
    }
  }

  def findLabelFor(what) {
    form.htmlElementDescendants.find {element ->
      element instanceof HtmlLabel && element.getAttribute("for") == what
    }
  }

  def methodMissing(String name, args) {
    if(name == "do") {
      return _do(*args)
    }
    throw new MissingMethodException(name, FormDsl, args)
  }

}