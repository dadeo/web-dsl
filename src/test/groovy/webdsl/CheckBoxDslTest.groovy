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
import webdsl.support.CheckBoxDsl

@Mixin(NonServerMixin)
class CheckBoxDslTest {

  @Test
  void test_instance_type() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car">I have a car
      </form>
    """

    webdsl {
      assert $("[name='bike']") instanceof CheckBoxDsl
      assert car instanceof CheckBoxDsl
    }
  }

  @Test
  void test_click() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
      </form>
    """

    webdsl {
      assert $("[name=bike]").value == false

      $('[name=bike]').click()

      assert $("[name=bike]").value == true

      $('[name=bike]').click()

      assert $("[name=bike]").value == false
    }
  }

  @Test
  void test_getValue_not_checked() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
      </form>
    """

    webdsl {
      assert $("[name='bike']").value == false
    }
  }

  @Test
  void test_getValue_checked() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike" checked>I have a bike<br>
      </form>
    """

    webdsl {
      assert $("[name='bike']").value == true
    }
  }


  @Test
  void test_getValue_outside_of_a_form() {
    html """
      <input type="checkbox" name="bike" value="Bike">I have a bike<br>
      <input type="checkbox" name="car" value="Car" checked>I have a car
    """

    webdsl {
      assert $("[name='bike']").value == false
      assert $("[name='car']").value == true
    }
  }

  @Test
  void test_getValue_multiple_forms() {
    html """
      <form id='form1'>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>

      <form id='form2'>
        <input type="checkbox" name="bike" value="Bike" checked>I have a bike<br>
        <input type="checkbox" name="car" value="Car">I have a car
      </form>
    """

    webdsl {
      assert $("#form1 [name='bike']").value == false
      assert $("#form1 [name='car']").value == true

      assert $("#form2 [name='bike']").value == true
      assert $("#form2 [name='car']").value == false
    }
  }

  @Test
  void test_setValue_null_is_false() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      $("[name='car']").value = null
      assert $("[name='car']").value == false
    }
  }

  @Test
  void test_setValue_true() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      $("[name='bike']").value = true
      assert $("[name='bike']").value == true
    }
  }

  @Test
  void test_setValue_false() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      $("[name='car']").value = false
      assert $("[name='car']").value == false
    }
  }

  @Test
  void test_setValue_outside_of_a_form_null_is_false() {
    html """
      <input type="checkbox" name="bike" value="Bike">I have a bike<br>
      <input type="checkbox" name="car" value="Car" checked>I have a car
    """

    webdsl {
      $("[name='car']").value = null
      assert $("[name='car']").value == false
    }
  }

  @Test
  void test_setValue_does_not_affect_other_forms_inputs__modify_first_form_input() {
    html """
      <form id='form1'>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>

      <form id='form2'>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      assert $("#form1 [name='bike']").value == false
      assert $("#form2 [name='bike']").value == false

      $("#form1 [name='bike']").value = true

      assert $("#form1 [name='bike']").value == true
      assert $("#form2 [name='bike']").value == false
    }
  }

  @Test
  void test_setValue_does_not_affect_other_forms_inputs__modify_second_form_input() {
    html """
      <form id='form1'>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>

      <form id='form2'>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      assert $("#form1 [name='bike']").value == false
      assert $("#form2 [name='bike']").value == false

      $("#form2 [name='bike']").value = true

      assert $("#form1 [name='bike']").value == false
      assert $("#form2 [name='bike']").value == true
    }
  }

  @Test
  void test_form_values() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      assert $("form").values() == [bike: false, car: true]
    }
  }

  @Test
  void test_form_valuesById() {
    html """
      <form>
        <input type="checkbox" id="bikeId" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" id="carId" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      assert $("form").valuesById() == [bikeId: false, carId: true]
    }
  }

  @Test
  void test_form_fillInWith_by_name() {
    html """
      <form>
        <input type="checkbox" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      $('form').fillInWith([bike: true, car: false])
      assert $("form").values() == [bike: true, car: false]
    }
  }

  @Test
  void test_form_fillInWith_by_id() {
    html """
      <form>
        <input type="checkbox" id="bikeId" name="bike" value="Bike">I have a bike<br>
        <input type="checkbox" id="carId" name="car" value="Car" checked>I have a car
      </form>
    """

    webdsl {
      $('form').fillInWith([bikeId: true, carId: false])
      assert $("form").values() == [bike: true, car: false]
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

      <input type="checkbox" id="bikeId" name="bike" value="Bike" onclick="handler();">I have a bike<br>
    """

    webdsl {
      assert $('p').text == 'page not updated'
      $('input').click()
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

      <input type="checkbox" id="bikeId" name="bike" value="Bike" onclick="handler();">I have a bike<br>
    """

    webdsl {
      assert $('p').text == 'page not updated'
      $('input').value = true
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

      <input type="checkbox" id="bikeId" name="bike" value="Bike" onclick="handler();">I have a bike<br>
    """

    webdsl {
      assert $('p').text == 'page not updated'
      $('input').value = false
      assert $('p').text == 'page not updated'
    }
  }
}