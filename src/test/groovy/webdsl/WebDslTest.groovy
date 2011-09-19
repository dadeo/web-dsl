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
    web.do {
      def oldClient = webClient
      openNewClient("http://localhost:$PORT/cloud.html")
      assertNotSame oldClient, webClient
      assertEquals "Cloud", title
    }
  }

  void test_exists() {
    web.do {
      assertTrue exists('namedRainbow')

      assertFalse exists('namedMain')

      namedRainbow.click()

      assertFalse exists('namedRainbow')

      assertTrue exists('namedMain')
    }
  }

  void test_title() {
    web.do {
      assertEquals "Main Page 1", title
    }
  }

  void test_click_anchor_by_name() {
    web.do {
      assertTrue namedRainbow instanceof ElementDsl
      assertEquals "the rainbow", namedRainbow.text
      namedRainbow.click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_click_anchor_by_id() {
    web.do {
      assertTrue rainbow instanceof ElementDsl
      rainbow.click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_click_element_by_id_as_dynamic_string() {
    web.do {
      assertTrue rainbow instanceof ElementDsl
      "rain${'bow'}".click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_click_anchor_by_href() {
    web.do {
      "rainbow.html".click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_click_anchor_by_text() {
    web.do {
      "the rainbow".click()
    }
    assertEquals "Rainbow Page", web.title
  }

  void test_text_from_line_item() {
    web.do {
      assertEquals "message 3", message3.text
    }
  }

  void test_text_from_div() {
    web.do {
      assertEquals "message 0", message0.text
    }
  }

  void test_textOfChildren() {
    def expected = ["message 0", "message 1message 2message 3", "message 1", "message 2", "message 3"]
    web.do {
      bind.actual = errors.children.text
    }
    assertEquals expected.size(), web.bind.actual.size()
    assertEquals expected, web.bind.actual
  }

  void test_textOfChildren_of_type() {
    def expected = ["message 1", "message 2", "message 3"]
    web.do {
      bind.actual = errors.children(type: "li").text
    }
    assertEquals expected.size(), web.bind.actual.size()
    assertEquals expected, web.bind.actual
  }

  void test_textOfChildren_of_types() {
    def expected = ["message 0", "message 1", "message 2", "message 3"]
    web.do {
      bind.actual = errors.children(types: ["li", "div"]).text
    }
    assertEquals expected.size(), web.bind.actual.size()
    assertEquals expected, web.bind.actual
  }

  void test_form_defaults_to_first_form_on_page() {
    web.do {
      form {
        submit
      }
    }
    assertEquals "TestServlet form0", web.title
    assertEquals "form0", server.path
  }

  void test_form_defaults_to_first_form_on_page_with_do() {
    web.do {
      form.do {
        submit
      }
    }
    assertEquals "TestServlet form0", web.title
    assertEquals "form0", server.path
  }

  void test_form_by_id() {
    web.do {
      form1 {
        submit
      }
    }
    assertEquals "TestServlet form1", web.title
    assertEquals "form1", server.path
  }

  void test_form_by_name_getter() {
    web.do {
      namedForm2 {
        submit
      }
    }
    assertEquals "TestServlet form2", web.title
    assertEquals "form2", server.path
  }

  void test_form_has_do() {
    web.do {
      namedForm2.do {
        submit
      }
    }
    assertEquals "TestServlet form2", web.title
    assertEquals "form2", server.path
  }

  void test_click_button_by_name() {
    web.do {
      namedSubmit1.click()
    }
    assertEquals "TestServlet form0", web.title
    assertEquals "form0", server.path
    assertTrue server.params.containsKey('namedSubmit1')
  }

  void test_click_button_by_id() {
    web.do {
      submit1.click()
    }
    assertEquals "form0", server.path
    assertTrue server.params.containsKey('namedSubmit1')
  }

  void test_click_button_by_value() {
    web.do {
      'Submit 1'.click()
    }
    assertEquals "form0", server.path
    assertTrue server.params.containsKey('namedSubmit1')
  }

  void test_click_checkbox_by_name() {
    web.do {
      namedCheckbox1.click()
      namedCheckbox2.click()
      form.submit
    }
    assertEquals "form0", server.path
    assertEquals "Checkbox 1", server.params.namedCheckbox1[0]
    assertEquals null, server.params.namedCheckbox2
  }

  void test_click_checkbox_by_id() {
    web.do {
      checkbox1.click()
      checkbox2.click()
      form.submit
    }
    assertEquals "form0", server.path
    assertEquals "Checkbox 1", server.params.namedCheckbox1[0]
    assertEquals null, server.params.namedCheckbox2
  }

  void test_checkbox_setValue_to_opposite_values() {
    web.do {
      checkbox1.value = true
      checkbox2.value = false
      form.submit
    }
    assertEquals "Checkbox 1", server.params.namedCheckbox1[0]
    assertEquals null, server.params.namedCheckbox2
  }

  void test_checkbox_setValue_defaulted_as_false_set_to_false() {
    web.do {
      checkbox1.value = false
      form.submit
    }
    assertEquals null, server.params.namedCheckbox1
  }

  void test_checkbox_setValue_defaulted_as_true_set_to_true() {
    web.do {
      checkbox2.value = true
      form.submit
    }
    assertEquals "Checkbox 2", server.params.namedCheckbox2[0]
  }

  void test_form_text_by_id() {
    web.do {
      assertEquals 'a default value', nameId.value
      nameId.value = 'henry'
      form.submit
    }
    assertEquals "henry", server.params.name[0]
  }

  void test_form_text_by_name() {
    web.do {
      assertTrue name instanceof ElementDsl
      assertEquals 'a default value', name.value
      name.value = 'henry'
      form.submit
    }
    assertEquals "henry", server.params.name[0]
  }

  void test_form_text_by_name_as_string() {
    web.do {
      "name".value = 'henry'
      form.submit
    }
    assertEquals "henry", server.params.name[0]
  }

  void test_form_text_by_name_as_gstring() {
    web.do {
      "na${'me'}".value = 'henry'
      form.submit
    }
    assertEquals "henry", server.params.name[0]
  }

  void test_form_text_changes_updates_page_tracking() {
    web.do {
      textToEcho.value = 'abcd'
      assert echoedText.value == 'abcd'
    }
  }

  void test_form_select_by_id() {
    web.do {
      assertTrue auto instanceof SelectDsl
      assert 'volvo' == autoId.value
      autoId.value = 'audi'
      form.submit
    }
    assertEquals "audi", server.params.auto[0]
  }

  void test_form_select_by_name() {
    web.do {
      assertTrue auto instanceof SelectDsl
      assert 'volvo' == auto.value
      auto.value = 'audi'
      form.submit
    }
    assertEquals "audi", server.params.auto[0]
  }

  void test_form_select__multiple_options() {
    web.do {
      assert ['volvo'] == auto2.values
      auto2.values = ['audi', 'mercedes', 'saab']
      assert auto2.values == ['saab', 'mercedes', 'audi']
      form3.submit
    }
    assert ['saab', 'mercedes', 'audi'] == server.params.auto2
  }

  void test_form_select__multiple_options__assign_empty_list() {
    web.do {
      assert ['volvo'] == auto2.values
      auto2.values = []
      assert auto2.values == []
      form3.submit
    }
    assertNull server.params.auto2
  }

  void test_form_select__multiple_options__deselectAll() {
    web.do {
      assert ['volvo'] == auto2.values
      auto2.deselectAll()
      assert auto2.values == []
      form3.submit
    }
    assertNull server.params.auto2
  }

  void test_form_select__multiple_options__set__not_supported() {
    shouldFail {
      web.auto.values = ['audi', 'mercedes', 'saab']
    }
  }

  void test_form_select__multiple_options__supported__value_called() {
    web.do {
      auto2.values = ['audi', 'mercedes', 'saab']
      assert auto2.value == ['saab', 'mercedes', 'audi']
    }
  }

  void test_form_select__multiple_options__supported__no_selection() {
    web.do {
      auto2.values = []
      assert auto2.value == null
      assert auto2.values == []
      assert auto2.selectedOptions == []
    }
  }

  void test_form_select__multiple_options__unsupported__no_selection() {
    web.do {
      auto.values = []
      assert auto.value == null
      assert auto.values == []
      assert auto.selectedOptions == []
    }
  }

  void test_form_select__multiple_options__get__not_supported() {
    assert web.auto.values == ['volvo']
  }

  void test_form_select_get_option_values() {
    web.do {
      assert auto.options.value == ['volvo', 'saab', 'mercedes', 'audi']
    }
  }

  void test_form_select_get_option_text() {
    web.do {
      assert auto.options.text == ['Volvo', 'Saab', 'Mercedes', 'Audi']
    }
  }

  void test_form_select_get_selectedOption_values() {
    web.do {
      auto2.values = ['mercedes', 'volvo']
      assert auto2.selectedOptions.value == ['volvo', 'mercedes']
    }
  }

  void test_form_select_get_selectedOption_text() {
    web.do {
      auto2.values = ['mercedes', 'volvo']
      assert auto2.selectedOptions.text == ['Volvo', 'Mercedes']
    }
  }

  void test_fillInWith() {
    web.do {
      form {
        fillInWith([name: 'henry', auto: 'audi', checkbox1: true])
        submit
      }
    }
    assertEquals "henry", server.params.name[0]
    assertEquals "audi", server.params.auto[0]
    assertEquals "Checkbox 1", server.params.namedCheckbox1[0]
  }

  void test_fillInWith_allows_extra_values_in_map() {
    web.do {
      form {
        fillInWith([name: 'henry', other: "xxxx"])
        submit
      }
    }
    assertEquals "henry", server.params.name[0]
  }

  void test_values() {
    Map actual
    web.do {
      form {
        actual = values()
      }
    }
    assertEquals([name: "a default value", auto: "volvo", namedCheckbox1: false, namedCheckbox2: true, namedCheckbox3: true, radio1: "radio 1 value 3"], actual)
  }

  void test_valuesById() {
    Map actual
    web.do {
      actual = form.valuesById()
    }
    assertEquals([nameId: "a default value", autoId: "volvo", checkbox1: false, checkbox2: true, checkbox3: true, radio1_1: false, radio1_2: false, radio1_3: true], actual)
  }

  void test_values_with_multiple_select() {
    web.do {
      auto2.values = ['volvo', 'saab']
      Map actual = form3.values()
      assertEquals([auto2: ["volvo", "saab"]], actual)
    }
  }

  void test_radio_with_label() {
    web.do {
      assert "radio content 3" == radio1_3.label
    }
  }

  void test_radio_with_no_label() {
    web.do {
      assert "" == radio1_1.label
    }
  }

  void test_radio() {
    web.do {
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
    assertEquals 'radio 1 value 2', server.params.radio1[0]
  }

  void test_table() {
    web.do {
      assertTrue table1 instanceof TableDsl
      def expected = [[firstName: "pinky", lastName: "jones"], [firstName: "john", lastName: "doe"]]
      assertEquals expected, table1.by.span
    }
  }

  void test_table_column() {
    web.do {
      def expected = [[first: "pinky", last: "jones"], [first: "john", last: "doe"]]
      assertEquals expected, table1.by.columns('first', 'last')
    }
  }

  void test_table_columns_not_all_columns_requested() {
    web.do {
      def expected = [[first: "pinky"], [first: "john"]]
      assertEquals expected, table1.by.columns('first')
    }
  }

  void test_table_columns_extra_columns_requested() {
    web.do {
      def expected = [[first: "pinky", last: "jones", ssn: ""], [first: "john", last: "doe", ssn: ""]]
      assertEquals expected, table1.by.columns('first', 'last', 'ssn')
    }
  }

  void test_table_as_objects_with_names() {
    web.do {
      def expected = [
          [first: "pinky", last: "jones1"],
          [first: "winky", last: "jones2"],
          [first: "dinky", last: "jones3"],
          [first: "linky", last: "jones4"],
          [first: "stinky", last: "jones5"],
      ]
      assertEquals expected, table3.as.objects('first', 'last')
    }
  }

  void test_table_as_object() {
    web.do {
      def expected = ["firstName": "john", "lastName": "doe", "ssn": "555-55-5555"]
      assertEquals expected, table2.as.object
    }
  }

  void test_table_as_objects() {
    web.do {
      def expected = [
          [firstName: "pinky", lastName: "jones1"],
          [firstName: "winky", lastName: "jones2"],
          [firstName: "dinky", lastName: "jones3"],
          [firstName: "linky", lastName: "jones4"],
          [firstName: "stinky", lastName: "jones5"],
      ]
      assertEquals expected, table3.as.objects
    }
  }

  void test_table_as_objects_with_offset() {
    def result
    web.do {
      result = table4(offset: 1).as.objects
    }
    def expected = [
        [firstName: "pinky", lastName: "jones1"],
        [firstName: "winky", lastName: "jones2"],
    ]
    assertEquals expected, result
  }

  void test_table_list() {
    def result
    web.do {
      result = table3.as.list
    }
    def expected = ["first name", "pinky", "winky", "dinky", "linky", "stinky"]
    assertEquals expected, result
  }

  void test_table_list_offset() {
    def result
    web.do {
      result = table3(offset: 2).as.list
    }
    def expected = ["winky", "dinky", "linky", "stinky"]
    assertEquals expected, result
  }

  void test_table_list_column() {
    def result
    web.do {
      result = table3(column: 1).as.list
    }
    def expected = ["last name", "jones1", "jones2", "jones3", "jones4", "jones5"]
    assertEquals expected, result
  }

  void test_table_list_column_and_offset() {
    def result
    web.do {
      result = table3(column: 1, offset: 1).as.list
    }
    def expected = ["jones1", "jones2", "jones3", "jones4", "jones5"]
    assertEquals expected, result
  }

  void test_table_process() {
    web.do {
      def result = []
      table2.process {row, column, td ->
        result << [rowIndex: row, columnIndex: column, content: td.textContent.trim()]
      }
      def expected = [
          [rowIndex: 0, columnIndex: 0, content: "First Name"],
          [rowIndex: 0, columnIndex: 1, content: "john"],
          [rowIndex: 1, columnIndex: 0, content: "Last Name"],
          [rowIndex: 1, columnIndex: 1, content: "doe"],
          [rowIndex: 2, columnIndex: 0, content: "SSN"],
          [rowIndex: 2, columnIndex: 1, content: "555-55-5555"],
      ]
      assertEquals expected, result
    }
  }

  void test_list_unordered() {
    def actual
    web.do {
      actual = myUnorderedList.value
    }
    assertEquals(['item 1', 'item 2', 'item 3', 'item 4', 'item 5'], actual)
  }

  void test_list_ordered() {
    def actual
    web.do {
      actual = myOrderedList.value
    }
    assertEquals(['item 1', 'item 2', 'item 3'], actual)
  }

  void test_properties() {
    web.do {
      def props = properties()
      assertTrue props.contains('auto')
      assertTrue props.contains('table1')
      assertTrue props.contains('table2')
      assertTrue props.contains('table3')
      assertTrue props.contains('form1')
      assertTrue props.contains('radio1')
      assertTrue props.contains('radio1_1')
    }
  }

  void test_ids_starting_with_upper_case_letters() {
    web.do {
      assertEquals "upper", upper.text
      assertEquals "UPPER", Upper.text
    }
  }

  void test_intern() {
    web.do {
      assertEquals "upper", "upper".intern.text
      assertEquals "UPPER", "Upper".intern.text
    }
  }

  void test_string_value() {
    web.do {
      assertEquals 'a default value', "nameId".value
    }
  }

  void test_gstring_value() {
    web.do {
      assertEquals 'a default value', "name${"Id"}".value
    }
  }

  void test_string_text() {
    web.do {
      assertEquals "upper", "upper".text
      assertEquals "UPPER", "Upper".text
    }
  }

  void test_gstring_text() {
    web.do {
      assertEquals "upper", "uppe${'r'}".text
      assertEquals "UPPER", "Upper".text
    }
  }

  void test_tagName() {
    web.do {
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
    web.do {
      assert multiline.text == "multi-line text"
    }
  }

  void test_untrimmedText() {
    web.do {
      assert multiline.untrimmedText == "\n    multi-line text\n"
    }
  }

  void test_value_is_trimmed() {
    web.do {
      assert valueWithSpaces.value == "abc"
    }
  }

  void test_untrimmedValue() {
    web.do {
      assert valueWithSpaces.untrimmedValue == " abc "
    }
  }
}