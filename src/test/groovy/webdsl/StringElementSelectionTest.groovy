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
class StringElementSelectionTest {

  @Test
  void test_string_lookup_of_element_input_not_found() {
    html {
      input(name: "name", id: "id", type: 'text', value: 'MYVALUE')
    }

    webdsl { dsl ->
      try {
        dsl."badName"
        fail("should fail")
      } catch (MissingPropertyException e) {
        // expected
      }
    }
  }

  @Test
  void test_string_lookup_of_element_anchor_not_found() {
    html {
      a(id: "id", href: 'http://something', 'follow me')
    }

    webdsl { dsl ->
      try {
        dsl."http://nothing"
        fail("should fail")
      } catch (MissingPropertyException e) {
        // expected
      }
    }
  }

  @Test
  void test_string_lookup_of_element_by_name() {
    html {
      input(name: "search", id: "id", type: 'text', value: 'MYVALUE')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("input").element
    }
  }

  @Test
  void test_string_lookup_of_element_by_id() {
    html {
      input(name: "name", id: "search", type: 'text', value: 'MYVALUE')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("input").element
    }
  }

  @Test
  void test_string_lookup_of_element_by_value() {
    html {
      input(name: "name", id: "id", type: 'text', value: 'SEARCH')
    }

    webdsl { dsl ->
      assert dsl."SEARCH".element == $("input").element
    }
  }

  @Test
  void test_string_lookup_of_element_anchor_by_href() {
    html {
      a(href: 'search', 'follow me')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("a").element
    }
  }

  @Test
  void test_string_lookup_of_element_anchor_by_text() {
    html {
      a(href: 'http://something', 'search')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("a").element
    }
  }

  @Test
  void test_string_lookup_of_element_does_not_find_span_by_text() {
    html {
      span('search')
    }

    webdsl { dsl ->
      try {
        dsl."search"
        fail "should not find"
      } catch (MissingPropertyException e) {
        // expected
      }
    }
  }

  @Test
  void test_string_lookup_of_element_by_css_selector() {
    html {
      a(id: "search", href: 'http://something', 'follow me')
    }

    webdsl { dsl ->
      assert dsl."#search".element == $("a").element
    }
  }

  @Test
  void test_string_lookup_of_element_finds_id_before_css() {
    html {
      search('MY BOGUS VALUE')
      span(id: 'search', "please don't do this")
    }

    webdsl { dsl ->
      assert dsl."search".element == $("span").element
    }
  }

  @Test
  void test_string_lookup_of_element_finds_id_before_name() {
    html {
      input(name: 'search', type: 'text', value: 'MYVALUE')
      span(id: 'search', "please don't do this")
    }

    webdsl { dsl ->
      assert dsl."search".element == $("span").element
    }
  }

  @Test
  void test_string_lookup_of_element_finds_id_before_text() {
    html {
      a(id: "myId", href: 'http://something', 'search')
      a(id: "search", href: 'http://something', 'follow me')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("#search").element
    }
  }

  @Test
  void test_string_lookup_of_element_finds_name_before_text() {
    html {
      a(id: "myId", href: 'http://something', 'search')
      input(name: "search", type: 'text', value: 'MYVALUE')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("input").element
    }
  }

  @Test
  void test_string_lookup_of_element_finds_name_before_href() {
    html {
      a(href: 'search', 'follow me')
      input(name: 'search', type: 'text', value: 'MYVALUE')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("input").element
    }
  }

  @Test
  void test_string_lookup_of_element_by_href_before_text() {
    html {
      a(id: "myId", href: 'http://something', 'search')
      a(id: "id", href: 'search', 'follow me')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("[href=search]").element
    }
  }

  @Test
  void test_string_lookup_of_element_by_value_before_text() {
    html {
      a(id: "myId", href: 'http://something', 'search')
      input(name: 'myInput', type: 'text', value: 'search')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("input").element
    }
  }

  @Test
  void test_string_lookup_of_element_finds_by_text_before_css() {
    html {
      search('MY BOGUS VALUE')
      a(id: "myId", href: 'http://something', 'search')
    }

    webdsl { dsl ->
      assert dsl."search".element == $("a").element
    }
  }
}