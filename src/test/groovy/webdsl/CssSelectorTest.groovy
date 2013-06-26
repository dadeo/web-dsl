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


class CssSelectorTest extends AbstractNonServerTest {

  void test_select_by_tag_name_single_element() {
    html {
      p('a')
      div('b')
    }

    webdsl {
      assert $('p').text == 'a'
      assert $$('p').text == ['a']
    }
  }

  void test_select_by_tag_name_multiple_elements() {
    html {
      div('hello')
      p('xxx')
      div(class: "whocares", 'world')
    }

    webdsl {
      assert $('div').text == 'hello'
      assert $$('div').text == ['hello', 'world']
    }
  }

  void test_select_by_tag_name_ignores_class() {
    html {
      p(class: 'main', 'a')
      div('b')
    }

    webdsl {
      assert $('p').text == 'a'
      assert $$('p').text == ['a']
    }
  }

  void test_select_by_tag_name_not_found() {
    html {
      div('hello')
      div('world')
    }

    webdsl {
      assert $('p') == null
      assert $$('p') == []
    }
  }

  void test_select_by_class_single_element() {
    html {
      p('a')
      p('b')
      p('c')
      div(class: 'selected', 'd')
    }

    webdsl {
      assert $('.selected').text == 'd'
      assert $$('.selected').text == ['d']
    }
  }

  void test_select_by_class_multiple_elements() {
    html {
      p('a')
      p(class: 'selected', 'b')
      p('c')
      div(class: 'selected', 'd')
    }

    webdsl {
      assert $('.selected').text == 'b'
      assert $$('.selected').text == ['b', 'd']
    }
  }

  void test_select_by_class_not_found() {
    html {
      p('a')
      p(class: 'other', 'b')
      p('c')
      div(class: 'other2', 'd')
    }

    webdsl {
      assert $('.selected') == null
      assert $$('.selected') == []
    }
  }

  void test_select_by_tagName_and_class_single_element() {
    html {
      p('a')
      p(class: 'selected', 'b')
      p('c')
      div(class: 'selected', 'd')
    }

    webdsl {
      assert $('p.selected').text == 'b'
      assert $$('p.selected').text == ['b']
    }
  }

  void test_select_by_tagName_and_class_multiple_elements() {
    html {
      p('a')
      p(class: 'selected', 'b')
      p('c')
      div(class: 'selected', 'd')
      p(class: 'selected', 'e')
    }

    webdsl {
      assert $('p.selected').text == 'b'
      assert $$('p.selected').text == ['b', 'e']
    }
  }

  void test_select_by_tagName_and_class_not_found() {
    html {
      p('a')
      p('b')
      p('c')
      div(class: 'selected', 'd')
    }

    webdsl {
      assert $('p.selected') == null
      assert $$('p.selected') == []
    }
  }

  void test_select_by_id_single_element() {
    html {
      p('a')
      p('b')
      p(id: 'myP', 'c')
    }

    webdsl {
      assert $('#myP').text == 'c'
      assert $$('#myP').text == ['c']
    }
  }

  void test_select_by_id_multiple_elements() {
    html {
      p(id: 'myP', 'a')
      p('b')
      p(id: 'myP', 'c')
    }

    webdsl {
      assert $('#myP').text == 'a'
      assert $$('#myP').text == ['a', 'c']
    }
  }

  void test_select_by_id_ignores_class() {
    html {
      p('a')
      p('b')
      p(id: 'myP', class: 'selected', 'c')
    }

    webdsl {
      assert $('#myP').text == 'c'
      assert $$('#myP').text == ['c']
    }
  }

  void test_select_by_id_not_found() {
    html {
      p('a')
      p('b')
      p('c')
    }

    webdsl {
      assert $('#myP') == null
      assert $$('#myP') == []
    }
  }

  void test_select_by_id_and_class_single_element() {
    html {
      p('a')
      p(id: 'myP', class: 'main', 'b')
      p(id: 'myP', class: 'main2', 'c')
    }

    webdsl {
      assert $('#myP.main').text == 'b'
      assert $$('#myP.main').text == ['b']
    }
  }

  void test_select_by_id_and_class_multiple_elements() {
    html {
      p('a')
      p(id: 'myP', class: 'main', 'b')
      p(id: 'myP', class: 'main', 'c')
      p('d')
    }

    webdsl {
      assert $('#myP.main').text == 'b'
      assert $$('#myP.main').text == ['b', 'c']
    }
  }

  void test_select_by_id_and_class_not_found() {
    html {
      p('a')
      p(id: 'myP', 'b')
      p('c')
    }

    webdsl {
      assert $('#myP.main') == null
      assert $$('#myP.main') == []
    }
  }

  void test_select_multiple_by_id_then_class() {
    html {
      div('a') {
        div(class: 'owner', '1')
      }
      div(id: 'myP', 'b') {
        div(class: 'owner', '2')
        div(class: 'owner', '3')
        div(class: 'owner', '4')
        div {
          div(class: 'owner', '5')
        }
      }
      div('c') {
        div(class: 'owner', '6')
      }
    }

    webdsl {
      assert $('#myP .owner').text == '2'
      assert $$('#myP .owner').text == ['2', '3', '4', '5']
    }
  }

  void test_select_tag_with_attribute_by_name() {
    html {
      div(select: 'me', 'foo')
      div('bar')
      p(select: 'me', 'bak')
      div(select: 'meToo', 'baz')
      div {
        div('foz')
        div(select: 'me', 'fox')
      }
    }

    webdsl {
      assert $('div[select]').text == 'foo', 'baz'
      assert $$('div[select]').text == ['foo', 'baz', 'fox']
    }
  }

  void test_select_tag_with_attribute_by_value() {
    html {
      div(select: 'me', 'foo')
      div('bar')
      div(select: 'meToo', 'baz')
      p(select: 'me', 'bak')
      div {
        div('foz')
        div(select: 'me', 'fox')
      }
    }

    webdsl {
      assert $('div[select="me"]').text == 'foo'
      assert $$('div[select="me"]').text == ['foo', 'fox']
    }
  }

  void test_select_by_attribute_value() {
    html {
      div(select: 'me', 'foo')
      div('bar')
      div(select: 'meToo', 'baz')
      p(select: 'me', 'bak')
      div {
        div('foz')
        div(select: 'me', 'fox')
      }
    }

    webdsl {
      assert $('[select="me"]').text == 'foo'
      assert $$('[select="me"]').text == ['foo', 'bak', 'fox']
    }
  }

  void test_select_by_attribute_begins_with() {
    html {
      div(select: 'me', 'foo')
      div('bar')
      div(select: 'meToo', 'baz')
      p(select: 'me', 'bak')
      div {
        div('foz')
        div(select: 'me', 'fox')
      }
    }

    webdsl {
      assert $('[select^="me"]').text == 'foo'
      assert $$('[select^="me"]').text == ['foo', 'baz', 'bak', 'fox']
    }
  }

  void test_select_by_attribute_ends_with() {
    html {
      div(select: 'me', 'foo')
      div('bar')
      div(select: 'meToo', 'baz')
      p(select: 'me', 'bak')
      div {
        div('foz')
        div(select: 'youToo', 'fox')
      }
    }

    webdsl {
      assert $('[select$="Too"]').text == 'baz'
      assert $$('[select$="Too"]').text == ['baz', 'fox']
    }
  }

  void test_select_by_attribute_value_contains_with() {
    html {
      div(select: 'me', 'foo')
      div('bar')
      div(select: 'meToo', 'baz')
      p(select: 'me', 'bak')
      div {
        div(select: 'beTty', 'foz')
        div(select: 'youToo', 'fox')
      }
    }

    webdsl {
      assert $('[select*="eT"]').text == 'baz'
      assert $$('[select*="eT"]').text == ['baz', 'foz']
    }
  }

  void test_select_by_attribute_value_list_contains_with() {
    html {
      div(select: 'this', 'foo')
      div('bar')
      div(select: 'this or that', 'baz')
      p(select: 'that', 'bak')
      div {
        div(select: 'that this or', 'foz')
        div(select: 'that or this', 'fox')
      }
    }

    webdsl {
      assert $('[select~="this"]').text == 'foo'
      assert $$('[select~="this"]').text == ['foo', 'baz', 'foz', 'fox']
    }
  }

  void test_select_by_attribute_starts_with_hyphenated() {
    html {
      div(select: 'en', 'foo')
      div('bar')
      div(select: 'en-', 'baz')
      p(select: 'end', 'bak')
      div {
        div(select: 'entry', 'foz')
        div(select: 'en-this', 'fox')
      }
    }

    webdsl {
      assert $('[select|="en"]').text == 'foo'
      assert $$('[select|="en"]').text == ['foo', 'baz', 'fox']
    }
  }

  void test_select_relationship_or() {
    html {
      div(select: 'en', 'foo')
      div('bar')
      div(select: 'en-', 'baz')
      p(select: 'end', 'bak')
      div {
        div(select: 'entry', 'foz')
        div(select: 'en-this', 'fox')
      }
      b(select: 'entry', 'bolded value')
    }

    webdsl {
      assert $("b,[select='end'],[select='entry']").text == 'bak'
      assert $$("b,[select='end'],[select='entry']").text == ['bak', 'foz', 'bolded value']
    }
  }

  void test_select_relationship_follows() {
    html {
      a('a')
      b('b1')
      c('c')
      b('b2')
      a('a')
      b('b3')
    }

    webdsl {
      assert $("a+b").text == 'b1'
      assert $$("a+b").text == ['b1', 'b3']
    }
  }

  void test_select_relationship_follows_complex() {
    html {
      a {
        b {
          c {
            d {
              e {
                f('f1')
              }
            }
          }
          d {
            e {
              f('f2')
              g('g1')
            }
          }
        }
      }
      a {
        c('c1')
        d {
          f('f3')
        }
        c('c2')
        d('d1')
      }
      e {
        c('c3')
        d {
          f('f4')
        }
      }
      c('c4')
      d {
        f('f5')
      }
    }

    webdsl {
      assert $("a c+d f").text == 'f2'
      assert $$("a c+d f").text == ['f2', 'f3']
    }
  }

  void test_select_relationship_child() {
    html {
      a {
        b('b1')
        c('c1')
        b('b2')
        b('b3')
        c('c2')
        d {
          b('b4')
        }
      }
    }

    webdsl {
      assert $("a>b").text == 'b1'
      assert $$("a>b").text == ['b1', 'b2', 'b3']
    }
  }

  void test_select_relationship_child_complex() {
    html {
      a {
        b('b1')
        d('d1')
        b {
          c {
            d('d2')
          }
          d('d3')
        }
        d('d4')
      }
      b {
        d('d5')
      }
    }

    webdsl {
      assert $("a>b d").text == 'd2'
      assert $$("a>b d").text == ['d2', 'd3']
    }
  }

  void test_select_relationship_multiple_simple() {
    html {
      a {
        b('b1')
        c('c1')
        d('d1')
        b('b2')
        d('d2')
        c('c2')
        d('d3')
        d('d4')
      }
      b('b3')
      c('c3')
      d('d5')
    }

    webdsl {
      assert $("a>b,c+d").text == 'b1'
      assert $$("a>b,c+d").text == ['b1', 'd1', 'b2', 'd3', 'd5']
    }
  }

  void test_select_relationship_precededBySibling_simple() {
    html {
      a {
        b('b1')
      }
      b('b2')
      c('c1')
      b('b3')
      d {
        b('b4')
      }
    }

    webdsl {
      assert $("a~b").text == 'b2'
      assert $$("a~b").text == ['b2', 'b3']
    }
  }

  void test_select_relationship_precededBySibling_complex() {
    html {
      a {
        b {
          c('c1')
          d('d1')
        }
      }
      b {
        c('c2')
        d('d2')
      }
      c('c3')
      b {
        c {
          d('d3')
        }
      }
      c {
        b {
          d('d4')
        }
      }
    }

    webdsl {
      assert $("a~b d").text == 'd2'
      assert $$("a~b d").text == ['d2', 'd3']
    }
  }

}