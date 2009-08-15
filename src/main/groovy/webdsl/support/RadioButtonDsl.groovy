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


class RadioButtonDsl extends ElementDsl {
  RadioButtonDsl(pageContainer, element) {
    super(pageContainer, element)
  }

  @Override
  def getText() {
      findLabelFor(element.getAttribute("id"))?.getTextContent() ?: ""
  }

  def getChecked() {
    element.isDefaultChecked()
  }

  @Override
  def tableValue(attributeName) {
    if (attributeName == "name") {
      return checked ? text : null
    } else {
      return checked
    }
  }

}