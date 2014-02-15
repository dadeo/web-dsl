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

class BaseElementDslTest extends AbstractNonServerTest {

  void test_hasClass() {
    html {
      div(id: "my-div", class: 'look-a-class')
    }

    webdsl {
      assert $('#my-div').hasClass('look-a-class')
    }
  }

  void test_hasClass_not_the_same_class() {
    html {
      div(id: "my-div", class: 'look-a-class')
    }

    webdsl {
      assertFalse $('#my-div').hasClass('look-another-class')
    }
  }

  void test_hasClass_does_not_have_a_class() {
    html {
      div(id: "my-div")
    }

    webdsl {
      assertFalse $('#my-div').hasClass('look-a-class')
    }
  }

  void test_hasClass_multiple_classes() {
    html {
      div(id: "my-div", class: 'one-class two-class three-class blue-class')
    }

    webdsl {
      assert $('#my-div').hasClass('one-class')
      assert $('#my-div').hasClass('two-class')
      assert $('#my-div').hasClass('three-class')
      assert $('#my-div').hasClass('blue-class')
    }
  }


  void test_hasAttribute() {
    html {
      div(id: "my-div", class: 'look-a-class')
    }

    webdsl {
      assert $('#my-div').hasAttribute('class')
      assert $('#my-div').hasAttribute('id')
    }
  }

  void test_hasAttribute_empty_attribute() {
    html {
      div(id: "my-div", class: 'look-a-class', disabled: '', checked: '')
    }

    webdsl {
      assert $("#my-div").hasAttribute('disabled')
      assert $("#my-div").hasAttribute('checked')
    }
  }

  void test_hasAttribute_attribute_with_no_value() {
    html("<div id='my-div' class='look-a-class' disabled checked></div>")

    webdsl {
      assert $("#my-div").hasAttribute('disabled')
      assert $("#my-div").hasAttribute('checked')
    }
  }

  void test_hasAttribute_no_attribute() {
    html {
      div(id: "my-div")
    }

    webdsl {
      assertFalse $("#my-div").hasAttribute('disabled')
      assertFalse $("#my-div").hasAttribute('style')
    }
  }
}