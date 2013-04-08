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
package webdsl

import webdsl.support.TableDsl
import webdsl.support.ElementDsl
import webdsl.support.SelectDsl

class WebDslTest extends AbstractServerTest {

  void test_openNewClient() {
    webdsl {
      def oldClient = webClient
      openNewClient("http://localhost:$PORT/cloud.html")
      assert !oldClient.is(webClient)
      assert title == "Cloud"
    }
  }

  void test_exists_navigation() {
    webdsl {
      assert exists('namedRainbow')

      assert !exists('namedMain')

      namedRainbow.click()

      assert !exists('namedRainbow')

      assert exists('namedMain')
    }
  }

  void test_exists_true() {
    webdsl {
      assert exists('form0')
      assert exists('table')
      assert exists('table4')
      assert exists('myOrderedList')
      assert exists('errors')
      assert exists('rainbow')
      assert exists('auto2Id')
      assert exists('textToEcho')
    }
  }

  void test_exists_false() {
    webdsl {
      assert !exists('table5')
      assert !exists('form4')
      assert !exists('orderedList')
    }
  }

  void test_title() {
    webdsl {
      assert title == "Main Page 1"
    }
  }

  void test_click_anchor_by_name() {
    webdsl {
      assert namedRainbow instanceof ElementDsl
      assert namedRainbow.text == "the rainbow"
      namedRainbow.click()
      assert title == "Rainbow Page"
    }
  }

  void test_click_anchor_by_id() {
    webdsl {
      assert rainbow instanceof ElementDsl
      rainbow.click()
      assert title == "Rainbow Page"
    }
  }

  void test_click_element_by_id_as_dynamic_string() {
    webdsl {
      assert rainbow instanceof ElementDsl
      "rain${'bow'}".click()
      assert title == "Rainbow Page"
    }
  }

  void test_click_anchor_by_href() {
    webdsl {
      "rainbow.html".click()
      assert title == "Rainbow Page"
    }
  }

  void test_click_anchor_by_text() {
    webdsl {
      "the rainbow".click()
      assert title == "Rainbow Page", web
    }
  }

  void test_text_from_line_item() {
    webdsl {
      assert message3.text == "message 3"
    }
  }

  void test_text_from_div() {
    webdsl {
      assert message0.text == "message 0"
    }
  }

  void test_form_defaults_to_first_form_on_page() {
    webdsl {
      form {
        submit
      }
      assert title == "TestServlet form0"
      assert server.path == "form0"
    }
  }

  void test_form_defaults_to_first_form_on_page_with_do() {
    webdsl {
      form.do {
        submit
      }
      assert title == "TestServlet form0"
      assert server.path == "form0"
    }
  }

  void test_form_by_id() {
    webdsl {
      form1 {
        submit
      }
      assert title == "TestServlet form1"
      assert server.path == "form1"
    }
  }

  void test_form_by_name_getter() {
    webdsl {
      namedForm2 {
        submit
      }
      assert title == "TestServlet form2"
      assert server.path == "form2"
    }
  }

  void test_form_has_do() {
    webdsl {
      namedForm2.do {
        submit
      }
      assert title == "TestServlet form2"
      assert server.path == "form2"
    }
  }

  void test_click_button_by_name() {
    webdsl {
      namedSubmit1.click()
      assert title == "TestServlet form0"
      assert server.path == "form0"
      assert server.params.containsKey('namedSubmit1')
    }
  }

  void test_click_button_by_id() {
    webdsl {
      submit1.click()
      assert server.path == "form0"
      assert server.params.containsKey('namedSubmit1')
    }
  }

  void test_click_button_by_value() {
    webdsl {
      'Submit 1'.click()
    }
    assert server.path == "form0"
    assert server.params.containsKey('namedSubmit1')
  }

  void test_click_checkbox_by_name() {
    webdsl {
      namedCheckbox1.click()
      namedCheckbox2.click()

      form.submit

      assert server.path == "form0", server.path
      assert server.params.namedCheckbox1[0] == "Checkbox 1"
      assert !server.params.namedCheckbox2
    }
  }

  void test_click_checkbox_by_id() {
    webdsl {
      checkbox1.click()
      checkbox2.click()

      form.submit

      assert server.path == "form0", server.path
      assert server.params.namedCheckbox1[0] == "Checkbox 1"
      assert !server.params.namedCheckbox2
    }
  }

  void test_checkbox_setValue_to_opposite_values() {
    webdsl {
      checkbox1.value = true
      checkbox2.value = false

      form.submit

      assert server.path == "form0", server.path
      assert server.params.namedCheckbox1[0] == "Checkbox 1"
      assert !server.params.namedCheckbox2
    }
  }

  void test_checkbox_setValue_defaulted_as_false_set_to_false() {
    webdsl {
      checkbox1.value = false
      form.submit
      assert !server.params.namedCheckbox1
    }
  }

  void test_checkbox_setValue_defaulted_as_true_set_to_true() {
    webdsl {
      checkbox2.value = true
      form.submit
      assert server.params.namedCheckbox2[0] == "Checkbox 2"
    }
  }

  void test_form_text_by_id() {
    webdsl {
      assert nameId.value == 'a default value'
      nameId.value = 'henry'
      form.submit
      assert server.params.name[0] == "henry"
    }
  }

  void test_form_text_by_name() {
    webdsl {
      assert name instanceof ElementDsl
      assert name.value == 'a default value'
      name.value = 'henry'
      form.submit
      assert server.params.name[0] == 'henry'
    }
  }

  void test_form_text_by_name_as_string() {
    webdsl {
      "name".value = 'henry'
      form.submit
    }
    assert server.params.name[0] == 'henry'
  }

  void test_form_text_by_name_as_gstring() {
    webdsl {
      "na${'me'}".value = 'henry'
      form.submit
      assert server.params.name[0] == 'henry'
    }
  }

  void test_form_text_changes_updates_page_tracking() {
    webdsl {
      textToEcho.value = 'abcd'
      assert echoedText.value == 'abcd'
    }
  }

  void test_form_select_by_id() {
    webdsl {
      assert auto instanceof SelectDsl
      assert 'volvo' == autoId.value
      autoId.value = 'audi'
      form.submit
      assert server.params.auto[0] == 'audi'
    }
  }

  void test_form_select_by_name() {
    webdsl {
      assert auto instanceof SelectDsl
      assert 'volvo' == auto.value
      auto.value = 'audi'
      form.submit
      assert server.params.auto[0] == 'audi'
    }
  }

  void test_form_select__multiple_options() {
    webdsl {
      assert ['volvo'] == auto2.values
      auto2.values = ['audi', 'mercedes', 'saab']
      assert auto2.values == ['saab', 'mercedes', 'audi']
      form3.submit
      assert ['saab', 'mercedes', 'audi'] == server.params.auto2
    }
  }

  void test_form_select__multiple_options__assign_empty_list() {
    webdsl {
      assert ['volvo'] == auto2.values
      auto2.values = []
      assert auto2.values == []
      form3.submit
      assert !server.params.auto2
    }
  }

  void test_form_select__multiple_options__deselectAll() {
    webdsl {
      assert ['volvo'] == auto2.values
      auto2.deselectAll()
      assert auto2.values == []
      form3.submit
      assert !server.params.auto2
    }
  }

  void test_form_select__multiple_options__set__not_supported() {
    shouldFail {
      webdsl {
        auto.values = ['audi', 'mercedes', 'saab']
      }
    }
  }

  void test_form_select__multiple_options__supported__value_called() {
    webdsl {
      auto2.values = ['audi', 'mercedes', 'saab']
      assert auto2.value == ['saab', 'mercedes', 'audi']
    }
  }

  void test_form_select__multiple_options__supported__no_selection() {
    webdsl {
      auto2.values = []
      assert auto2.value == null
      assert auto2.values == []
      assert auto2.selectedOptions == []
    }
  }

  void test_form_select__multiple_options__unsupported__no_selection() {
    webdsl {
      auto.values = []
      assert auto.value == null
      assert auto.values == []
      assert auto.selectedOptions == []
    }
  }

  void test_form_select__multiple_options__get__not_supported() {
    webdsl {
      assert auto.values == ['volvo']
    }
  }

  void test_form_select_get_option_values() {
    webdsl {
      assert auto.options.value == ['volvo', 'saab', 'mercedes', 'audi']
    }
  }

  void test_form_select_get_option_text() {
    webdsl {
      assert auto.options.text == ['Volvo', 'Saab', 'Mercedes', 'Audi']
    }
  }

  void test_form_select_get_selectedOption_values() {
    webdsl {
      auto2.values = ['mercedes', 'volvo']
      assert auto2.selectedOptions.value == ['volvo', 'mercedes']
    }
  }

  void test_form_select_get_selectedOption_text() {
    webdsl {
      auto2.values = ['mercedes', 'volvo']
      assert auto2.selectedOptions.text == ['Volvo', 'Mercedes']
    }
  }

  void test_form_select_set_value_by_label_text() {
    webdsl {
      auto.value = 'Volvo'
      assert 'volvo' == auto.value
    }
  }

  void test_form_select_set_value_by_label_text_not_found() {
    try {
      webdsl {
        auto.value = 'VolvoII'
        assert 'volvo' == auto.value
      }
      fail('not expected to pass')
    } catch (RuntimeException e) {
      assert e.message == '''Unable to find Option(name: 'VolvoII') or Option(text: 'VolvoII') in Select(id: 'autoId', name: 'auto').'''
    }
  }

  void test_fillInWith() {
    webdsl {
      form {
        fillInWith([name: 'henry', auto: 'audi', checkbox1: true])
        submit
      }

      assert server.params.name[0] == 'henry'
      assert server.params.auto[0] == 'audi'
      assert server.params.namedCheckbox1[0] == 'Checkbox 1'
    }
  }

  void test_fillInWith_allows_extra_values_in_map() {
    webdsl {
      form {
        fillInWith([name: 'henry', other: "xxxx"])
        submit
      }
      assert server.params.name[0] == 'henry'
    }
  }

  void test_values() {
    Map actual
    webdsl {
      form {
        actual = values()
      }
    }
    assert actual == [name: "a default value", auto: "volvo", namedCheckbox1: false, namedCheckbox2: true, namedCheckbox3: true, radio1: "radio 1 value 3"]
  }

  void test_valuesById() {
    webdsl {
      Map actual = form.valuesById()
      assert actual == [nameId: "a default value", autoId: "volvo", checkbox1: false, checkbox2: true, checkbox3: true, radio1_1: false, radio1_2: false, radio1_3: true], actual
    }
  }

  void test_values_with_multiple_select() {
    webdsl {
      auto2.values = ['volvo', 'saab']
      Map actual = form3.values()
      assert actual == [auto2: ["volvo", "saab"]]
    }
  }

  void test_radio_with_label() {
    webdsl {
      assert "radio content 3" == radio1_3.label
    }
  }

  void test_radio_with_no_label() {
    webdsl {
      assert "" == radio1_1.label
    }
  }

  void test_radio() {
    webdsl {
      assert !radio1_1.checked
      assert !radio1_2.checked
      assert radio1_3.checked
      assert "radio 1 value 3" == radio1.value

      radio1_2.click()

      assert !radio1_1.checked
      assert radio1_2.checked
      assert !radio1_3.checked
      assert "radio 1 value 2" == radio1.value

      form.submit
    }
    assert server.params.radio1[0] == 'radio 1 value 2'
  }

  void test_table_as_objects_with_offset() {
    def result
    webdsl {
      result = table4(offset: 1).as.objects
    }
    def expected = [
        [firstName: "pinky", lastName: "jones1"],
        [firstName: "winky", lastName: "jones2"],
    ]
    assert result == expected
  }

  void test_table_list_offset() {
    def result
    webdsl {
      result = table3(offset: 2).as.list
    }
    def expected = ["winky", "dinky", "linky", "stinky"]
    assert result == expected
  }

  void test_table_list_column_and_offset() {
    def result
    webdsl {
      result = table3(column: 1, offset: 1).as.list
    }
    def expected = ["jones1", "jones2", "jones3", "jones4", "jones5"]
    assert result == expected
  }

  void test_list_unordered() {
    def actual
    webdsl {
      actual = myUnorderedList.value
    }
    assert actual == ['item 1', 'item 2', 'item 3', 'item 4', 'item 5']
  }

  void test_list_ordered() {
    def actual
    webdsl {
      actual = myOrderedList.value
    }
    assert actual == ['item 1', 'item 2', 'item 3']
  }

  void test_properties() {
    webdsl {
      def props = properties()
      assert props.contains('auto')
      assert props.contains('table1')
      assert props.contains('table2')
      assert props.contains('table3')
      assert props.contains('form1')
      assert props.contains('radio1')
      assert props.contains('radio1_1')
    }
  }

  void test_ids_starting_with_upper_case_letters() {
    webdsl {
      assert upper.text == 'upper'
      assert Upper.text == 'UPPER'
    }
  }

  void test_intern() {
    webdsl {
      assert "upper".intern.text == 'upper'
      assert "Upper".intern.text == 'UPPER'
    }
  }

  void test_string_value() {
    webdsl {
      assert "nameId".value == 'a default value'
    }
  }

  void test_gstring_value() {
    webdsl {
      assert "name${"Id"}".value == 'a default value'
    }
  }

  void test_string_text() {
    webdsl {
      assert "upper".text == 'upper'
      assert "Upper".text == 'UPPER'
    }
  }

  void test_gstring_text() {
    webdsl {
      assert "uppe${'r'}".text == 'upper'
      assert "Upper".text == 'UPPER'
    }
  }

  void test_tagName() {
    webdsl {
      assert errors.tagName == "div"
      assert table1.tagName == "table"
      assert myUnorderedList.tagName == "ul"
      assert myOrderedList.tagName == "ol"
      assert form0.tagName == "form"
      assert nameId.tagName == "input"
      assert autoId.tagName == "select"
      assert checkbox1.tagName == "input"
      assert radio1_1.tagName == "input"
      assert submit1.tagName == "input"
    }
  }

  void test_text_is_trimmed() {
    webdsl {
      assert multiline.text == "multi-line text"
    }
  }

  void test_untrimmedText() {
    webdsl {
      assert multiline.untrimmedText == "\n    multi-line text\n"
    }
  }

  void test_value_is_trimmed() {
    webdsl {
      assert valueWithSpaces.value == "abc"
    }
  }

  void test_untrimmedValue() {
    webdsl {
      assert valueWithSpaces.untrimmedValue == " abc "
    }
  }

  void test_back() {
    webdsl {
      assert title == "Main Page 1"
      rainbow.click()
      assert title == "Rainbow Page"
      back()
      assert title == "Main Page 1"
    }
  }
}