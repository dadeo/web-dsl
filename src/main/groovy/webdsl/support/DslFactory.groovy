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
import com.gargoylesoftware.htmlunit.html.HtmlTable
import com.gargoylesoftware.htmlunit.html.HtmlSelect
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList
import com.gargoylesoftware.htmlunit.html.HtmlInput


class DslFactory {
  def create(pageContainer, element) {
    if (element instanceof HtmlForm) {
      return new FormDsl(pageContainer, this, element)
    } else if (element instanceof HtmlTable) {
      return new TableDsl(pageContainer, this, element)
    } else if (element instanceof HtmlSelect) {
      return new SelectDsl(pageContainer, this, element)
    } else if (element instanceof HtmlRadioButtonInput) {
      return new RadioButtonDsl(pageContainer, this, element)
    } else if (element instanceof HtmlCheckBoxInput) {
      return new CheckBoxDsl(pageContainer, this, element)
    } else if (element instanceof HtmlUnorderedList || element instanceof HtmlOrderedList) {
      return new ListDsl(pageContainer, this, element)
    } else if (element instanceof HtmlInput) {
      return new InputDsl(pageContainer, this, element)
    }
    new ElementDsl(pageContainer, this, element)
  }
}