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
package com.github.dadeo.webdsl

import com.github.dadeo.webdsl.support.ElementDsl
import org.junit.Test

@Mixin(ServerMixin)
class WebDslServerTest {

    @Test
    void test_openNewClient() {
        webdsl {
            def oldClient = webClient
            openNewClient("http://localhost:$PORT/cloud.html")
            assert !oldClient.is(webClient)
            assert title == "Cloud"
        }
    }

    @Test
    void test_exists_navigation() {
        webdsl {
            assert exists('namedRainbow')

            assert !exists('namedMain')

            namedRainbow.click()

            assert !exists('namedRainbow')

            assert exists('namedMain')
        }
    }

    @Test
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

    @Test
    void test_exists_false() {
        webdsl {
            assert !exists('table5')
            assert !exists('form4')
            assert !exists('orderedList')
        }
    }

    @Test
    void test_title() {
        webdsl {
            assert title == "Main Page 1"
        }
    }

    @Test
    void test_click_anchor_by_name() {
        webdsl {
            assert namedRainbow instanceof ElementDsl
            assert namedRainbow.text == "the rainbow"
            namedRainbow.click()
            assert title == "Rainbow Page"
        }
    }

    @Test
    void test_click_anchor_by_id() {
        webdsl {
            assert rainbow instanceof ElementDsl
            rainbow.click()
            assert title == "Rainbow Page"
        }
    }

    @Test
    void test_click_element_by_id_as_dynamic_string() {
        webdsl {
            assert rainbow instanceof ElementDsl
            "rain${'bow'}".click()
            assert title == "Rainbow Page"
        }
    }

    @Test
    void test_click_anchor_by_href() {
        webdsl {
            "rainbow.html".click()
            assert title == "Rainbow Page"
        }
    }

    @Test
    void test_click_anchor_by_text() {
        webdsl {
            "the rainbow".click()
            assert title == "Rainbow Page", web
        }
    }

    @Test
    void test_text_from_line_item() {
        webdsl {
            assert message3.text == "message 3"
        }
    }

    @Test
    void test_text_from_div() {
        webdsl {
            assert message0.text == "message 0"
        }
    }

    @Test
    void test_form_defaults_to_first_form_on_page() {
        webdsl {
            form {
                submit
            }
            assert title == "TestServlet form0"
            assert server.path == "form0"
        }
    }

    @Test
    void test_form_defaults_to_first_form_on_page_with_do() {
        webdsl {
            form.do {
                submit
            }
            assert title == "TestServlet form0"
            assert server.path == "form0"
        }
    }

    @Test
    void test_form_by_id() {
        webdsl {
            form1 {
                submit
            }
            assert title == "TestServlet form1"
            assert server.path == "form1"
        }
    }

    @Test
    void test_form_by_name_getter() {
        webdsl {
            namedForm2 {
                submit
            }
            assert title == "TestServlet form2"
            assert server.path == "form2"
        }
    }

    @Test
    void test_form_has_do() {
        webdsl {
            namedForm2.do {
                submit
            }
            assert title == "TestServlet form2"
            assert server.path == "form2"
        }
    }

    @Test
    void test_click_button_by_name() {
        webdsl {
            namedSubmit1.click()
            assert title == "TestServlet form0"
            assert server.path == "form0"
            assert server.params.containsKey('namedSubmit1')
        }
    }

    @Test
    void test_click_button_by_id() {
        webdsl {
            submit1.click()
            assert server.path == "form0"
            assert server.params.containsKey('namedSubmit1')
        }
    }

    @Test
    void test_click_button_by_value() {
        webdsl {
            'Submit 1'.click()
        }
        assert server.path == "form0"
        assert server.params.containsKey('namedSubmit1')
    }

    @Test
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

    @Test
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

    @Test
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

    @Test
    void test_checkbox_setValue_defaulted_as_false_set_to_false() {
        webdsl {
            checkbox1.value = false
            form.submit
            assert !server.params.namedCheckbox1
        }
    }

    @Test
    void test_checkbox_setValue_defaulted_as_true_set_to_true() {
        webdsl {
            checkbox2.value = true
            form.submit
            assert server.params.namedCheckbox2[0] == "Checkbox 2"
        }
    }

    @Test
    void test_form_text_by_id() {
        webdsl {
            assert nameId.value == 'a default value'
            nameId.value = 'henry'
            form.submit
            assert server.params.name[0] == "henry"
        }
    }

    @Test
    void test_form_text_by_name() {
        webdsl {
            assert name instanceof ElementDsl
            assert name.value == 'a default value'
            name.value = 'henry'
            form.submit
            assert server.params.name[0] == 'henry'
        }
    }

    @Test
    void test_form_text_by_name_as_string() {
        webdsl {
            "name".value = 'henry'
            form.submit
        }
        assert server.params.name[0] == 'henry'
    }

    @Test
    void test_form_text_by_name_as_gstring() {
        webdsl {
            "na${'me'}".value = 'henry'
            form.submit
            assert server.params.name[0] == 'henry'
        }
    }

    @Test
    void test_form_text_changes_updates_page_tracking() {
        webdsl {
            textToEcho.value = 'abcd'
            assert echoedText.value == 'abcd'
        }
    }

    @Test
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

    @Test
    void test_fillInWith_allows_extra_values_in_map() {
        webdsl {
            form {
                fillInWith([name: 'henry', other: "xxxx"])
                submit
            }
            assert server.params.name[0] == 'henry'
        }
    }

    @Test
    void test_values() {
        Map actual
        webdsl {
            form {
                actual = values()
            }
        }
        assert actual == [name: "a default value", auto: "Volvo", namedCheckbox1: false, namedCheckbox2: true, namedCheckbox3: true, radio1: "radio 1 value 3"]
    }

    @Test
    void test_valuesById() {
        webdsl {
            Map actual = form.valuesById()
            assert actual == [nameId: "a default value", autoId: "Volvo", checkbox1: false, checkbox2: true, checkbox3: true, radio1_1: false, radio1_2: false, radio1_3: true], actual
        }
    }

    @Test
    void test_values_with_multiple_select() {
        webdsl {
            auto2.value = ['Volvo', 'Saab']
            Map actual = form3.values()
            assert actual == [auto2: ["Volvo", "Saab"]]
        }
    }

    @Test
    void test_radio_with_label() {
        webdsl {
            assert "radio content 3" == radio1_3.label
        }
    }

    @Test
    void test_radio_with_no_label() {
        webdsl {
            assert "" == radio1_1.label
        }
    }

    @Test
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

    @Test
    void test_list_unordered() {
        def actual
        webdsl {
            actual = myUnorderedList.value
        }
        assert actual == ['item 1', 'item 2', 'item 3', 'item 4', 'item 5']
    }

    @Test
    void test_list_ordered() {
        def actual
        webdsl {
            actual = myOrderedList.value
        }
        assert actual == ['item 1', 'item 2', 'item 3']
    }

    @Test
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

    @Test
    void test_ids_starting_with_upper_case_letters() {
        webdsl {
            assert upper.text == 'upper'
            assert Upper.text == 'UPPER'
        }
    }

    @Test
    void test_intern() {
        webdsl {
            assert "upper".intern.text == 'upper'
            assert "Upper".intern.text == 'UPPER'
        }
    }

    @Test
    void test_string_value() {
        webdsl {
            assert "nameId".value == 'a default value'
        }
    }

    @Test
    void test_gstring_value() {
        webdsl {
            assert "name${"Id"}".value == 'a default value'
        }
    }

    @Test
    void test_string_text() {
        webdsl {
            assert "upper".text == 'upper'
            assert "Upper".text == 'UPPER'
        }
    }

    @Test
    void test_gstring_text() {
        webdsl {
            assert "uppe${'r'}".text == 'upper'
            assert "Upper".text == 'UPPER'
        }
    }

    @Test
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

    @Test
    void test_text_is_trimmed() {
        webdsl {
            assert multiline.text == "multi-line text"
        }
    }

    @Test
    void test_untrimmedText() {
        webdsl {
            assert multiline.untrimmedText == "\n    multi-line   text\n"
        }
    }

    @Test
    void test_value_is_trimmed() {
        webdsl {
            assert valueWithSpaces.value == "abc"
        }
    }

    @Test
    void test_untrimmedValue() {
        webdsl {
            assert valueWithSpaces.untrimmedValue == " abc "
        }
    }

    @Test
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