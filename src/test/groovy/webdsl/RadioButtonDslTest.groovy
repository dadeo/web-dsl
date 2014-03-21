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
import webdsl.support.RadioButtonDsl

@Mixin(NonServerMixin)
class RadioButtonDslTest {

  @Test
  void test_instance_type() {
    html """
      <form>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female'>Female
      </form>
    """

    webdsl {
      assert $("[name='sex']") instanceof RadioButtonDsl
      assert sex instanceof RadioButtonDsl
    }
  }

  @Test
  void test_getValue_no_selection() {
    html """
      <form>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female'>Female
      </form>
    """

    webdsl {
      assert $("[name='sex']").value == null
    }
  }

  @Test
  void test_getValue_selection() {
    html """
      <form>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      assert $("[name='sex']").value == 'female'
    }
  }


  @Test
  void test_getValue_outside_of_a_form__no_selection() {
    html """
      <input type='radio' name='sex' value='male'>Male
      <br/>
      <input type='radio' name='sex' value='female'>Female
    """

    webdsl {
      assert $("[name='sex']").value == null
    }
  }

  @Test
  void test_getValue_outside_of_a_form___selection() {
    html """
      <input type='radio' name='sex' value='male'>Male
      <br/>
      <input type='radio' name='sex' value='female' checked>Female
    """

    webdsl {
      assert $("[name='sex']").value == 'female'
    }
  }

  @Test
  void test_getValue_multiple_forms__second_form_no_selection() {
    html """
      <form id='form1'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>

      <form id='form2'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female'>Female
      </form>
    """

    webdsl {
      assert $("#form1 [name='sex']").value == 'female'
      assert $("#form2 [name='sex']").value == null
    }
  }

  @Test
  void test_getValue_multiple_forms__second_form_has_selection() {
    html """
      <form id='form1'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>

      <form id='form2'>
        <input type='radio' name='sex' value='male' checked>Male
        <br/>
        <input type='radio' name='sex' value='female'>Female
      </form>
    """

    webdsl {
      assert $("#form1 [name='sex']").value == 'female'
      assert $("#form2 [name='sex']").value == 'male'
    }
  }

  @Test
  void test_setValue_valid_value() {
    html """
      <form>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      $("[name='sex']").value = 'male'
      assert $("[name='sex']").value == 'male'
    }
  }

  @Test
  void test_setValue_null_deselects_all() {
    html """
      <form>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      $("[name='sex']").value = null
      assert $("[name='sex']").value == null
    }
  }

  @Test
  void test_setValue_true() {
    html """
      <form>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female'>Female
      </form>
    """

    webdsl {
      $("[value='female']").value = true
      assert $("[name='sex']").value == 'female'
    }
  }

  @Test
  void test_setValue_false() {
    html """
      <form>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      $("[value='female']").value = false
      assert $("[name='sex']").value == null
    }
  }

  @Test
  void test_setValue_outside_of_a_form_valid_value() {
    html """
      <input type='radio' name='sex' value='male'>Male
      <br/>
      <input type='radio' name='sex' value='female' checked>Female
    """

    webdsl {
      $("[name='sex']").value = 'male'
      assert $("[name='sex']").value == 'male'
    }
  }

  @Test
  void test_setValue_outside_of_a_form_null_deselects_all() {
    html """
      <input type='radio' name='sex' value='male'>Male
      <br/>
      <input type='radio' name='sex' value='female' checked>Female
    """

    webdsl {
      $("[name='sex']").value = null
      assert $("[name='sex']").value == null
    }
  }

  @Test
  void test_setValue_does_not_affect_other_forms_inputs__modify_first_form_input() {
    html """
      <form id='form1'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>

      <form id='form2'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      assert $("#form1 [name='sex']").value == 'female'
      assert $("#form2 [name='sex']").value == 'female'

      $("#form1 [name='sex']").value = 'male'

      assert $("#form1 [name='sex']").value == 'male'
      assert $("#form2 [name='sex']").value == 'female'
    }
  }

  @Test
  void test_setValue_does_not_affect_other_forms_inputs__modify_second_form_input() {
    html """
      <form id='form1'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>

      <form id='form2'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      assert $("#form1 [name='sex']").value == 'female'
      assert $("#form2 [name='sex']").value == 'female'

      $("#form2 [name='sex']").value = 'male'

      assert $("#form1 [name='sex']").value == 'female'
      assert $("#form2 [name='sex']").value == 'male'
    }
  }

  @Test
  void test_form_values() {
    html """
      <form>
        <input type='radio' name='sex' id='sexMale' value='male'>Male
        <br/>
        <input type='radio' name='sex' id='sexFemale' value='female' checked>Female
      </form>
    """

    webdsl {
      assert $("form").values() == [sex: 'female']
    }
  }

  @Test
  void test_form_valuesById() {
    html """
      <form>
        <input type='radio' name='sex' id='sexMale' value='male'>Male
        <br/>
        <input type='radio' name='sex' id='sexFemale' value='female' checked>Female
      </form>
    """

    webdsl {
      assert $("form").valuesById() == [sexMale: false, sexFemale: true]
    }
  }

  @Test
  void test_form_fillInWith_by_name() {
    html """
      <form>
        <input type='radio' name='sex' id='sexMale' value='male'>Male
        <br/>
        <input type='radio' name='sex' id='sexFemale' value='female'>Female
      </form>
    """

    webdsl {
      $('form').fillInWith([sex: 'female'])
      assert $("form").valuesById() == [sexMale: false, sexFemale: true]
    }
  }

  @Test
  void test_form_fillInWith_by_id() {
    html """
      <form>
        <input type='radio' name='sex' id='sexMale' value='male'>Male
        <br/>
        <input type='radio' name='sex' id='sexFemale' value='female'>Female
      </form>
    """

    webdsl {
      $('form').fillInWith([sexFemale: true])
      assert $("form").valuesById() == [sexMale: false, sexFemale: true]
    }
  }

  @Test
  void test_group_inside_a_form() {
    html """
      <form>
        <input type='radio' name='sex' id='sexMale' value='male'>Male
        <br/>
        <input type='radio' name='sex' id='sexFemale' value='female'>Female
      </form>
    """

    webdsl {
      RadioButtonDsl element = $('[name="sex"]')
      List<RadioButtonDsl> group = element.group

      assert group.size() == 2
      assert group[0] instanceof RadioButtonDsl
      assert group[0].attr('value') == 'male'
      assert group[1] instanceof RadioButtonDsl
      assert group[1].attr('value') == 'female'
    }
  }

  @Test
  void test_group_outside_a_form() {
    html """
      <input type='radio' name='sex' id='sexMale' value='male'>Male
      <br/>
      <input type='radio' name='sex' id='sexFemale' value='female'>Female
    """

    webdsl {
      RadioButtonDsl element = $('[name="sex"]')
      List<RadioButtonDsl> group = element.group

      assert group.size() == 2
      assert group[0] instanceof RadioButtonDsl
      assert group[0].attr('value') == 'male'
      assert group[1] instanceof RadioButtonDsl
      assert group[1].attr('value') == 'female'
    }
  }

  @Test
  void test_group_multiple_forms() {
    html """
      <form id='form1'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>

      <form id='form2'>
        <input type='radio' name='sex' value='male'>Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      RadioButtonDsl element = $('#form2 [name="sex"]')
      List<RadioButtonDsl> group = element.group

      assert group.size() == 2
      assert group[0] instanceof RadioButtonDsl
      assert group[0].attr('value') == 'male'
      assert group[1] instanceof RadioButtonDsl
      assert group[1].attr('value') == 'female'
    }
  }

  @Test
  void test_click_fires_onclick_event() {
    js '''
      function handler() {
        document.getElementsByTagName("p")[0].innerHTML = 'page updated'
      };
    '''

    html """
      <p>page not updated</p>

      <form>
        <input type='radio' name='sex' value='male' onclick="handler();">Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      assert $('p').text == 'page not updated'
      $('input[value=male]').click()
      assert $('p').text == 'page updated'
    }
  }

  @Test
  void test_value_fires_onclick_event() {
    js '''
      function handler() {
        document.getElementsByTagName("p")[0].innerHTML = 'page updated'
      };
    '''

    html """
      <p>page not updated</p>

      <form>
        <input type='radio' name='sex' value='male' onclick="handler();">Male
        <br/>
        <input type='radio' name='sex' value='female' checked>Female
      </form>
    """

    webdsl {
      assert $('p').text == 'page not updated'
      $('[name=sex]').value = 'male'
      assert $('p').text == 'page updated'
    }
  }

  @Test
  void test_value_does_not_fire_onclick_event_when_value_does_not_change() {
    js '''
      function handler() {
        document.getElementsByTagName("p")[0].innerHTML = 'page updated'
      };
    '''

    html """
      <p>page not updated</p>

      <form>
        <input type='radio' name='sex' value='male' checked onclick="handler();">Male
        <br/>
        <input type='radio' name='sex' value='female'>Female
      </form>
    """

    webdsl {
      assert $('p').text == 'page not updated'
      $('[name=sex]').value = 'male'
      assert $('p').text == 'page not updated'
    }
  }

  @Test
  void test_click_fires_onclick_event_when_value_does_not_change() {
    js '''
      function handler() {
        document.getElementsByTagName("p")[0].innerHTML = 'page updated'
      };
    '''

    html """
      <p>page not updated</p>

      <form>
        <input type='radio' name='sex' value='male' checked onclick="handler();">Male
        <br/>
        <input type='radio' name='sex' value='female'>Female
      </form>
    """

    webdsl {
      assert $('p').text == 'page not updated'
      $('[name=sex]').click()
      assert $('p').text == 'page updated'
    }
  }
}