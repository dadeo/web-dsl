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

@Mixin(NonServerMixin)
class BaseElementDslTest {

  @Test
  void test_getAttribute() {
    html {
      div(id: "my-div", class: 'look-a-class') {}
    }

    webdsl {
      assert $('#my-div').getAttribute('id') == 'my-div'
      assert $('#my-div').getAttribute('class') == 'look-a-class'
    }
  }

  @Test
  void test_getAttributes() {
    html {
      div(id: "my-div", class: 'look-a-class') {}
    }

    webdsl {
      assert $('#my-div').attributes == [id: 'my-div', class: 'look-a-class']
    }
  }

  @Test
  void test_setAttribute() {
    html {
      div(id: "my-div", class: 'look-a-class') {}
    }

    webdsl {
      assert $('#my-div').getAttribute('class') == 'look-a-class'

      $('#my-div').setAttribute('class', 'LOOK_A_CLASS')

      assert $('#my-div').getAttribute('class') == 'LOOK_A_CLASS'
    }
  }


  @Test
  void test_setAttribute_resets_cached_scripts() {
    html """
      <input id="ok" onclick="setTo('yo');" type="button" value="ok"/>
      <input id="eventText" type="text" value=""/>

      <script>
        function setTo(what) {
          document.getElementById('eventText').value = what;
        }
      </script>
    """

    webdsl {
      ok.click()
      assert eventText.value == 'yo'

      ok.setAttribute('onclick', "setTo('yo dog');")
      ok.click()

      assert eventText.value == 'yo dog'
    }
  }

  @Test
  void test_modifyAttribute() {
    html {
      div(id: "my-div", class: 'look-a-class') {}
    }

    webdsl {
      assert $('#my-div').getAttribute('class') == 'look-a-class'

      $('#my-div').modifyAttribute('class', { it.toUpperCase() })

      assert $('#my-div').getAttribute('class') == 'LOOK-A-CLASS'
    }
  }

  @Test
  void test_modifyAttribute_resets_cached_scripts() {
    html """
      <input id="ok" onclick="setTo('yo');" type="button" value="ok"/>
      <input id="eventText" type="text" value=""/>

      <script>
        function setTo(what) {
          document.getElementById('eventText').value = what;
        }
      </script>
    """

    webdsl {
      ok.click()
      assert eventText.value == 'yo'

      ok.modifyAttribute('onclick', { it.replaceAll('yo', 'yo dog') })
      ok.click()

      assert eventText.value == 'yo dog'
    }
  }

  @Test
  void test_hasClass() {
    html {
      div(id: "my-div", class: 'look-a-class') {}
    }

    webdsl {
      assert $('#my-div').hasClass('look-a-class')
    }
  }

  @Test
  void test_hasClass_not_the_same_class() {
    html {
      div(id: "my-div", class: 'look-a-class') {}
    }

    webdsl {
      assert !$('#my-div').hasClass('look-another-class')
    }
  }

  @Test
  void test_hasClass_does_not_have_a_class() {
    html {
      div(id: "my-div") {}
    }

    webdsl {
      assert !$('#my-div').hasClass('look-a-class')
    }
  }

  @Test
  void test_hasClass_multiple_classes() {
    html {
      div(id: "my-div", class: 'one-class two-class three-class blue-class') {}
    }

    webdsl {
      assert $('#my-div').hasClass('one-class')
      assert $('#my-div').hasClass('two-class')
      assert $('#my-div').hasClass('three-class')
      assert $('#my-div').hasClass('blue-class')
    }
  }

  @Test
  void test_hasClass_does_not_match_on_partials() {
    html {
      div(id: "my-div", class: 'one-class two-class three-class blue-class') {}
    }

    webdsl {
      assert !$('#my-div').hasClass('class')
    }
  }

  @Test
  void test_hasAttribute() {
    html {
      div(id: "my-div", class: 'look-a-class') {}
    }

    webdsl {
      assert $('#my-div').hasAttribute('class')
      assert $('#my-div').hasAttribute('id')
    }
  }

  @Test
  void test_hasAttribute_empty_attribute() {
    html {
      div(id: "my-div", class: 'look-a-class', disabled: '', checked: '') {}
    }

    webdsl {
      assert $("#my-div").hasAttribute('disabled')
      assert $("#my-div").hasAttribute('checked')
    }
  }

  @Test
  void test_hasAttribute_attribute_with_no_value() {
    html("<div id='my-div' class='look-a-class' disabled checked></div>")

    webdsl {
      assert $("#my-div").hasAttribute('disabled')
      assert $("#my-div").hasAttribute('checked')
    }
  }

  @Test
  void test_hasAttribute_attribute_are_not_case_sensitive() {
    html("<div id='my-div' class='look-a-class' DISABLED CHECKED></div>")

    webdsl {
      assert $("#my-div").hasAttribute('disabled')
      assert $("#my-div").hasAttribute('checked')
    }
  }

  @Test
  void test_hasAttribute_no_attribute() {
    html {
      div(id: "my-div") {}
    }

    webdsl {
      assert !$("#my-div").hasAttribute('disabled')
      assert !$("#my-div").hasAttribute('style')
    }
  }

  @Test
  void test_isDisabled_true() {
    html {
      div(id: "my-div", disabled: 'true') {}
    }

    webdsl {
      assert $("div").isDisabled()
    }
  }

  @Test
  void test_isDisabled_upper_case_true() {
    html {
      div(id: "my-div", DISABLED: 'DISABLED') {}
    }

    webdsl {
      assert $("div").isDisabled()
    }
  }

  @Test
  void test_isDisabled_false() {
    html {
      div(id: "my-div") {}
    }

    webdsl {
      assert !$("div").isDisabled()
    }
  }

  @Test
  void test_insertBefore_first_element() {
    html {
      div('b', id: 'target')
      div('c')
    }

    webdsl {
      assert $$('div').text == ['b', 'c']

      $('#target').insertBefore { div('a') }

      assert $$('div').text == ['a', 'b', 'c']
    }
  }

  @Test
  void test_insertBefore_last_element() {
    html {
      div('a')
      div('c', id: 'target')
    }

    webdsl {
      assert $$('div').text == ['a', 'c']

      $('#target').insertBefore { div('b') }

      assert $$('div').text == ['a', 'b', 'c']
    }
  }

  @Test
  void test_insertAfter_first_element() {
    html {
      div('a', id: 'target')
      div('c')
    }

    webdsl {
      assert $$('div').text == ['a', 'c']

      $('#target').insertAfter { div('b') }

      assert $$('div').text == ['a', 'b', 'c']
    }
  }

  @Test
  void test_insertAfter_last_element() {
    html {
      div('a')
      div('b', id: 'target')
    }

    webdsl {
      assert $$('div').text == ['a', 'b']

      $('#target').insertAfter { div('c') }

      assert $$('div').text == ['a', 'b', 'c']
    }
  }

  @Test
  void test_prependChild_children_exist() {
    html {
      div(id: 'target') {
        div('a2')
        div('a3')
      }
      div('b')
    }

    webdsl {
      assert $$('div').text == ['a2a3', 'a2', 'a3', 'b']

      $('#target').prependChild { div('a1') }

      assert $$('div').text == ['a1a2a3', 'a1', 'a2', 'a3', 'b']
    }
  }

  @Test
  void test_prependChild_no_children_exist() {
    html {
      div(id: 'target') {}

      div('b')
    }

    webdsl {
      assert $$('div').text == ['', 'b']

      $('#target').prependChild { div('a') }

      assert $$('div').text == ['a', 'a', 'b']
    }
  }

  @Test
  void test_appendChild_children_exist() {
    html {
      div(id: 'target') {
        div('a1')
        div('a2')
      }
      div('b')
    }

    webdsl {
      assert $$('div').text == ['a1a2', 'a1', 'a2', 'b']

      $('#target').appendChild { div('a3') }

      assert $$('div').text == ['a1a2a3', 'a1', 'a2', 'a3', 'b']
    }
  }

  @Test
  void test_appendChild_no_children_exist() {
    html {
      div(id: 'target') {}

      div('b')
    }

    webdsl {
      assert $$('div').text == ['', 'b']

      $('#target').appendChild { div('a') }

      assert $$('div').text == ['a', 'a', 'b']
    }
  }

  @Test
  void test_asXml() {
    html {
      div('b')
    }

    webdsl {
      assert $('div').asXml().replaceAll('\r\n', '\n') == """<div>
                                                             |  b
                                                             |</div>
                                                             |""".stripMargin()
                                                                 .replaceAll('\r\n', '\n')
    }
  }

  @Test
  void test_asText() {
    html {
      div(id: 'my-div') {
        span('my')
        span(' ')
        span('text')
      }
    }

    webdsl {
      assert $('#my-div').asText() == 'my text'
    }
  }

  @Test
  void test_closest_direct_descendent() {
    html {
      div(id: 'div0') {}

      div(id: 'div1') {
        div(id: 'div2') {
          div(id: 'div3') {
            div(id: 'target') {}
          }
        }
      }

      div(id: 'div4') {}
    }

    webdsl {
      assert $('#target').closest('div').id == 'div3'
    }
  }

  @Test
  void test_closest_nested_descendant() {
    html {
      div(id: 'div0') {}

      div(id: 'div1') {
        div(id: 'div2') {
          span(id: 'span1') {
            span(id: 'span2') {
              span(id: 'target') {}
            }
          }
        }
      }

      div(id: 'div3') {}
    }

    webdsl {
      assert $('#target').closest('div').id == 'div2'
    }
  }

  @Test
  void test_closest_css_selector_class() {
    html {
      div(id: 'div0', class: 'one') {}

      div(id: 'div1', class: 'one') {
        div(id: 'div2') {
          span(id: 'span1') {
            span(id: 'span2') {
              span(id: 'target') {}
            }
          }
        }
      }

      div(id: 'div3', class: 'one') {}
    }

    webdsl {
      assert $('#target').closest('.one').id == 'div1'
    }
  }

  @Test
  void test_closest_css_selector_class__not_an_ancestor() {
    html {
      div(id: 'div0', class: 'one') {}

      div(id: 'div1') {
        div(id: 'div2') {
          span(id: 'span1') {
            span(id: 'span2') {
              span(id: 'target') {}
            }
          }
        }
      }

      div(id: 'div3', class: 'one') {}
    }

    webdsl {
      assert $('#target').closest('.one') == null
    }
  }

  @Test
  void test_closest_css_selector_id() {
    html {
      div(id: 'div0', class: 'one') {}

      div(id: 'div1', class: 'one') {
        div(id: 'div2') {
          span(id: 'span1') {
            span(id: 'span2') {
              span(id: 'target') {}
            }
          }
        }
      }

      div(id: 'div3', class: 'one') {}
    }

    webdsl {
      assert $('#target').closest('#div1').id == 'div1'
    }
  }

  @Test
  void test_closest_css_selector_id__not_an_ancestor() {
    html {
      div(id: 'div0', class: 'one') {}

      div(id: 'div1', class: 'one') {
        div(id: 'div2') {
          span(id: 'span1') {
            span(id: 'span2') {
              span(id: 'target') {}
            }
          }
        }
      }

      div(id: 'div3', class: 'one') {}
    }

    webdsl {
      assert $('#target').closest('#div0') == null
    }
  }

  @Test
  void test_closest_css_selector_child() {
    html {
      div(id: 'div0', class: 'one') {}

      div(id: 'div1', class: 'one') {
        div(id: 'div2') {
          span(id: 'span1') {
            span(id: 'span2') {
              span(id: 'target') {}
            }
          }
        }
      }

      div(id: 'div3', class: 'one') {}
    }

    webdsl {
      assert $('#target').closest('div>span').id == 'span1'
    }
  }

  @Test
  void test_closest_css_selector_sibling() {
    html {
      div(id: 'div1', class: 'one') {
        div(id: 'div2') {
          span(id: 'span1') {}
          span(id: 'span2') {
            span(id: 'span3') {
              span(id: 'target') {}
            }
          }
        }
      }
    }

    webdsl {
      assert $('#target').closest('span~span').id == 'span2'
    }
  }

  @Test
  void test_closest_css_selector_tag__not_found() {
    html {
      div(id: 'div1') {
        div(id: 'div2') {
          span(id: 'span1') {
            span(id: 'span2') {
              span(id: 'target') {}
            }
          }
        }
      }
    }

    webdsl {
      assert $('#target').closest('p') == null
    }
  }

  @Test
  void test_find_child_by_css_selector_found() {
    html {
      div(id: 'div1') {
        span 'text 1', class: 'target'
      }
      div(id: 'div2') {
        span 'text 2', class: 'target'
      }
    }

    webdsl {
      assert $('#div2').$('.target').value == 'text 2'
    }
  }

  @Test
  void test_find_child_by_css_selector_not_found() {
    html {
      div(id: 'div1') {
      }
    }

    webdsl {
      assert $('#div1').$('.target') == null
    }
  }

  @Test
  void test_find_children_by_css_selector_found() {
    html {
      div(id: 'div1') {
        span 'text 1', class: 'target'
      }
      div(id: 'div2') {
        span 'text 2.1', class: 'target'
        span 'text 2.2', class: 'target'
      }
    }

    webdsl {
      assert $('#div2').$$('.target').value == ['text 2.1', 'text 2.2']
    }
  }

  @Test
  void test_find_children_by_css_selector_not_found() {
    html {
      div(id: 'div1') {
      }
    }

    webdsl {
      assert $('#div1').$$('.target') == []
    }
  }

}