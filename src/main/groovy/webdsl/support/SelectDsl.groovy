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


class SelectDsl extends ElementDsl {
  SelectDsl(pageContainer, element) {
    super(pageContainer, element)
  }

  private createOptions = { [value: it.getValueAttribute(), text: it.text]}

  @Override
  def getValue() {
    def selected = values
    if(!selected) {
      null
    } else if (!element.isMultipleSelectEnabled() || selected.size() == 1) {
      selected[0]
    } else {
      selected
    }
  }

  @Override
  def setValue(value) {
    setValues([value])
  }

  def getValues() {
    selectedOptions.value
  }

  def setValues(List<String> values) {
    if(values.size() > 1 && !element.isMultipleSelectEnabled())
      throw new RuntimeException("html select element does not have multiple select enabled")

    pageContainer.page = element.selectedOptions.inject(null) { page, option -> option.setSelected(false) }
    if(values)
      pageContainer.page = values.inject(null) { page, value -> element.getOptionByValue(value).setSelected(true) }
  }

  def tableValue(attributeName) {
    value
  }

  def getOptions() {
    element.getOptions().collect createOptions
  }

  def getSelectedOptions() {
    element.getSelectedOptions().collect createOptions
  }

  def deselectAll() {
    setValues([])
  }
}