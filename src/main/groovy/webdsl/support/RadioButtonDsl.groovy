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

import com.gargoylesoftware.htmlunit.html.DomNode
import com.gargoylesoftware.htmlunit.html.HtmlElement

class RadioButtonDsl extends ElementDsl {
  RadioButtonDsl(pageContainer, DslFactory factory, element) {
    super(pageContainer, factory, element)
  }

  def getChecked() {
    element.isChecked()
  }

  void setChecked(boolean checked) {
    pageContainer.page = element.setChecked(checked)
  }

  def getValue() {
    if (checked)
      super.value
    else {
      BaseElementDsl formDsl = closest('form')
      DomNode containingNode = formDsl ? formDsl.element : pageContainer.page
      containingNode.getHtmlElementDescendants().find { it.getAttribute('name') == name && it.checked }?.getAttribute('value')
    }
  }

  @Override
  def setValue(value) {
    def clickIt = { HtmlElement e, boolean makeChecked ->
      if(makeChecked && !e.isChecked()) {
        pageContainer.page = e.click()
      } else if (!makeChecked) {
        pageContainer.page = e.setChecked(false)
      }
    }

    if(value instanceof Boolean) {
      clickIt element, value
    } else {
      BaseElementDsl formDsl = closest('form')
      DomNode containingNode = formDsl ? formDsl.element : pageContainer.page
      if(value)
        containingNode.getHtmlElementDescendants().find { it.getAttribute('name') == name && it.valueAttribute == value }.each { clickIt it, true }
      else
        containingNode.getHtmlElementDescendants().findAll { it.getAttribute('name') == name}.each { clickIt it, false }
    }


//    if(value instanceof Boolean) {
//      element.setChecked(value)
//    } else {
//      BaseElementDsl formDsl = closest('form')
//      DomNode containingNode = formDsl ? formDsl.element : pageContainer.page
//      if(value)
//        containingNode.getHtmlElementDescendants().find { it.getAttribute('name') == name && it.valueAttribute == value }?.setChecked(true)
//      else
//        containingNode.getHtmlElementDescendants().findAll { it.getAttribute('name') == name}*.setChecked(false)
//    }
  }

  @Override
  def tableValue(attributeName) {
    if (attributeName == "name") {
      return checked ? value : null
    } else {
      return checked
    }
  }

  List<RadioButtonDsl> getGroup() {
    BaseElementDsl formDsl = closest('form')
    DomNode containingNode = formDsl ? formDsl.element : pageContainer.page
    containingNode.getHtmlElementDescendants()
                  .findAll { it.getAttribute('name') == name}
                  .collect { factory.create(pageContainer, it) }
  }
}