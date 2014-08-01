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
import com.gargoylesoftware.htmlunit.html.HtmlOption


class SelectDsl extends ElementDsl {
  SelectDsl(pageContainer, DslFactory factory, element) {
    super(pageContainer, factory, element)
  }

  private createOptions = {
    HtmlOption option = it

    [
        value: option.getValueAttribute(),
        text: option.text,
        selected: option.isSelected(),
        select: { option.selected = true },
        deselect: { option.selected = false }
    ]
  }

  @Override
  def getValue() {
    def selectedOptions = getSelectedOptions()
    if(element.isMultipleSelectEnabled())
      selectedOptions.text
    else
      selectedOptions[0]?.text
  }

  List<String> getText() {
    options.text
  }

  @Override
  def setValue(value) {
    setValues([value].flatten().grep { it != null })
  }

  def setValues(List<String> values) {
    if (!values.size() && !element.isMultipleSelectEnabled())
      throw new RuntimeException("unable to deselect option, html select element does not have multiple select enabled")

    if (values.size() > 1 && !element.isMultipleSelectEnabled())
      throw new RuntimeException("html select element does not have multiple select enabled")

    pageContainer.page = element.selectedOptions.inject(null) { page, option -> option.setSelected(false) }
    if (values)
      pageContainer.page = values.inject(null) { page, value ->
        def option = findOptionByValue(element, value) ?: findOptionByText(element, value)
        if (!option) throw new RuntimeException("Unable to find Option(name: '$value') or Option(text: '$value') in Select(id: '$id', name: '$name').")
        option.setSelected(true)
      }
  }

  protected def findOptionByValue(HtmlElement element, String value) {
    try {
      element.getOptionByValue(value)
    } catch (e) {
      // allowed
    }
  }

  protected def findOptionByText(HtmlElement element, String value) {
    try {
      element.getOptionByText(value)
    } catch (e) {
      // allowed
    }
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

  void deselectAll() {
    if(element.isMultipleSelectEnabled())
      setValues([])
    else
      throw new RuntimeException('deselect is not available, html select element does not have multiple select enabled')
  }
}