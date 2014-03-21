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

import org.junit.Test
import webdsl.support.SelectDsl

@Mixin(NonServerMixin)
class SelectDslTest {

  @Test
  void test_instance_type() {
    html {
      form {
        select(id: 'autos', name: 'Autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos") instanceof SelectDsl
      assert autos instanceof SelectDsl
      assert Autos instanceof SelectDsl
    }
  }

  @Test
  void test_getValue_no_option_selected__not_multiple_select() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").value == 'Volvo'
    }
  }

  @Test
  void test_getValue_option_selected__not_multiple_select() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").value == 'Saab'
    }
  }

  @Test
  void test_getValue_multiple_options_selected__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', selected: 'true', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").value == ['Saab', 'Audi']
    }
  }

  @Test
  void test_setValue_option_selected__by_name__not_multiple_select() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").value = 'Audi'
      assert $("#autos").value == 'Audi'
    }
  }

  @Test
  void test_setValue_multiple_items__by_name__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").value = ['Audi', 'Volvo']
      assert $("#autos").value == ['Volvo', 'Audi']
    }
  }

  @Test
  void test_setValue_multiple_items__by_name__not_multiple_select_should_fail() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      try {
        $("#autos").value = ['Audi', 'Volvo']
        fail('should fail above')
      } catch (e) {
        assert e.message == 'html select element does not have multiple select enabled'
      }
    }
  }

  @Test
  void test_setValue_option_selected__by_value() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").value = 'audi'
      assert $("#autos").value == 'Audi'
    }
  }

  @Test
  void test_setValue_multiple__by_value__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").value = ['audi', 'volvo']
      assert $("#autos").value == ['Volvo', 'Audi']
    }
  }

  @Test
  void test_setValue_to_empty_list__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").value = []
      assert $("#autos").value == []
    }
  }

  @Test
  void test_setValue_to_null__not_multiple_select() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      try {
        $("#autos").value = null
        fail('should fail above')
      } catch (e) {
        assert e.message == 'unable to deselect option, html select element does not have multiple select enabled'
      }
    }
  }

  @Test
  void test_setValue_to_null__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").value = null
      assert $("#autos").value == []
    }
  }

  @Test
  void test_setValue_multiple_items__by_value__not_multiple_select_should_fail() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      try {
        $("#autos").value = ['audi', 'volvo']
        fail('should fail above')
      } catch (e) {
        assert e.message == 'html select element does not have multiple select enabled'
      }
    }
  }

  @Test
  void test_getText() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").text == ['Volvo', 'Saab', 'Audi']
    }
  }

  @Test
  void test_getOptions() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").options.text == ['Volvo', 'Saab', 'Audi']
      assert $("#autos").options.value == ['volvo', 'saab', 'audi']
      assert $("#autos").options.selected == [false, true, false]
    }
  }

  @Test
  void test_getSelectedOptions__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', selected: 'true', 'Volvo')
          option(value: 'saab', 'Saab')
          option(value: 'audi', selected: 'true', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").selectedOptions.text == ['Volvo', 'Audi']
      assert $("#autos").selectedOptions.value == ['volvo', 'audi']
      assert $("#autos").selectedOptions.selected == [true, true]
    }
  }

  @Test
  void test_getSelectedOptions_nothing_selected__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").selectedOptions == []
    }
  }

  @Test
  void test_getSelectedOptions_nothing_selected__not_multiple_select() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").selectedOptions.text == ['Volvo']
      assert $("#autos").selectedOptions.value == ['volvo']
      assert $("#autos").selectedOptions.selected == [true]
    }
  }

  @Test
  void test_getOptions__select_option__not_multiple_select() {
    html {
      form {
        select(id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").options.find { it.text == 'Audi' }.select()
      assert $("#autos").value == 'Audi'
      assert $("#autos").selectedOptions.size() == 1
    }
  }

  @Test
  void test_getOptions__select_option__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("#autos").options.find { it.text == 'Audi' }.select()
      assert $("#autos").value == ['Saab', 'Audi']
    }
  }

  @Test
  void test_getOptions_deselect_only_option__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").options.find { it.text == 'Saab' }.deselect()
      assert $("#autos").value == []
    }
  }

  @Test
  void test_getOptions_deselect_one_option__multiple_select() {
    html {
      form {
        select(id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', selected: 'true', 'Saab')
          option(value: 'audi', selected: 'true', 'Audi')
        }
      }
    }

    webdsl {
      $("#autos").options.find { it.text == 'Saab' }.deselect()
      assert $("#autos").value == ['Audi']
    }
  }

  @Test
  void test_form_values__not_multiple_select() {
    html {
      form {
        select(name: 'Autos', id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("form").values() == [Autos: 'Saab']
    }
  }

  @Test
  void test_form_values__multiple_select() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("form").values() == [Autos: ['Saab']]
    }
  }

  @Test
  void test_form_valuesById__not_multiple_select() {
    html {
      form {
        select(name: 'Autos', id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("form").valuesById() == [autos: 'Saab']
    }
  }

  @Test
  void test_form_valuesById__multiple_select() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      assert $("form").valuesById() == [autos: ['Saab']]
    }
  }

  @Test
  void test_form_fillInWith_by_name__multiple_select__with_multiple_values() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("form").fillInWith([Autos: ['Volvo', 'Audi']])
      assert $("form").values() == [Autos: ['Volvo', 'Audi']]
    }
  }

  @Test
  void test_form_fillInWith_by_name__multiple_select__with_single_value() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("form").fillInWith([Autos: 'Audi'])
      assert $("form").values() == [Autos: ['Audi']]
    }
  }

  @Test
  void test_form_fillInWith_by_name__not_multiple_select() {
    html {
      form {
        select(name: 'Autos', id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }
  }

  @Test
  void test_form_fillInWith_by_id__multiple_select__with_multiple_values() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("form").fillInWith([autos: ['Volvo', 'Audi']])
      assert $("form").values() == [Autos: ['Volvo', 'Audi']]
    }
  }

  @Test
  void test_form_fillInWith_by_id__multiple_select__with_single_value() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("form").fillInWith([autos: 'Audi'])
      assert $("form").values() == [Autos: ['Audi']]
    }
  }

  @Test
  void test_form_fillInWith_by_id__not_multiple_select() {
    html {
      form {
        select(name: 'Autos', id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      $("form").fillInWith([autos: 'Audi'])
      assert $("form").values() == [Autos: 'Audi']
    }
  }

  @Test
  void test_deselect__not_multiple_select__should_fail() {
    html {
      form {
        select(name: 'Autos', id: 'autos') {
          option(value: 'volvo', 'Volvo')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi')
        }
      }
    }

    webdsl {
      try {
        $("#autos").deselectAll()
        fail('should fail above')
      } catch (e) {
        assert e.message == 'deselect is not available, html select element does not have multiple select enabled'
      }
    }
  }

  @Test
  void test_deselect__multiple_select() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo', selected: 'true')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi', selected: 'true')
        }
      }
    }

    webdsl {
      $("#autos").deselectAll()
      assert $("#autos").value == []
    }
  }

  @Test
  void test_setValue_value_not_found() {
    html {
      form {
        select(name: 'Autos', id: 'autos', multiple: 'true') {
          option(value: 'volvo', 'Volvo', selected: 'true')
          option(value: 'saab', 'Saab', selected: 'true')
          option(value: 'audi', 'Audi', selected: 'true')
        }
      }
    }

    webdsl {
      try {
        $("#autos").value = 'VolvoII'
        fail('not expected to pass')
      } catch (RuntimeException e) {
        assert e.message == '''Unable to find Option(name: 'VolvoII') or Option(text: 'VolvoII') in Select(id: 'autos', name: 'Autos').'''
      }
    }
  }


}