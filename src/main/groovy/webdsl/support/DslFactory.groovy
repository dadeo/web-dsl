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

import com.gargoylesoftware.htmlunit.html.*

class DslFactory {
  private registry = [
      (HtmlForm):FormDsl,
      (HtmlTable):TableDsl,
      (HtmlSelect):SelectDsl,
      (HtmlRadioButtonInput):RadioButtonDsl,
      (HtmlCheckBoxInput):CheckBoxDsl,
      (HtmlUnorderedList):ListDsl,
      (HtmlOrderedList):ListDsl,
      (HtmlTextInput):TextInputDsl,
      (HtmlPasswordInput):PasswordInputDsl,
      (HtmlHiddenInput):HiddenInputDsl,
      (HtmlTextArea):TextAreaDsl,
      (HtmlButtonInput):ButtonInputDsl,
      (HtmlSubmitInput):SubmitInputDsl,
      (HtmlResetInput):ResetInputDsl,
      (HtmlElement):ElementDsl
  ]

  def create(PageContainer pageContainer, element) {
    registry.findResult { Class elementClazz, Class dslClazz ->
      if(elementClazz.isInstance(element)) {
        dslClazz.newInstance([pageContainer, this, element] as Object[])
      }
    }
  }

  void register(Class dslClass, Class elementClass) {
    registry[elementClass] = dslClass
  }
}