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

import webdsl.support.SelectorDsl
import webdsl.support.BaseElementDsl


class DslSelectorTest extends AbstractServerTest {

  protected String defaultPage() {
    return "selector"
  }

  void test_divs() {
    web.do {
      assertEquals 3, div.size()
      assertEquals 2, div[1].div.size()
      assertEquals "2.1", div[1].div[0].text
      assertEquals "2.2", div[1].div[1].text
      assertEquals "2.2", div2.div[1].text
    }
  }

  void test_selector_built_correctly() {
    web.do {
      assertElementTagNames(3, "tr", names.tbody[0].tr)
      assertElementTagNames(6, "td", names.tbody[0].tr.td)
    }
  }

  void test_table_tbody_optional() {
    web.do {
      assertEquals(["first", "last", "Pinky", "Jones", "Winky", "Jones"], names.tbody[0].tr.td.text)
      assertEquals(["first", "last", "Pinky", "Jones", "Winky", "Jones"], names.tr.td.text)
    }
  }

  void test_selector_supports_collect_method() {
    web.do {
      assertEquals(["first", "last", "Pinky", "Jones", "Winky", "Jones"], names.tr.td.collect { text })
      assertEquals(['div3_1', 'div3_2', 'div3_3', 'div3_4'], div[2].div.collect { attr("id") })
    }
  }

  void test_selector_each() {
    web.do {
      def names = []
      table[0].tr.td.each { names << text }
      assert ['first', 'last', 'Pinky', 'Jones', 'Winky', 'Jones'] == names
    }
  }

  void test_selector_find() {
    web.do {
      assert "WinkyJones" == table[0].tr.find { td.find { text == "Winky" } }.text
      assert "PinkyJones" == table[0].tr.find { td.find { text == "Jones" } }.text
    }
  }

  void test_selector_findAll() {
    web.do {
      assert ['1.2', '2.1', '2.2', '3.2'] == div.div.findAll { text.contains('2') }.text
    }
  }

  void test_selector_findResult() {
    web.do {
      assert ['1.2', '2.1', '2.2', '3.2'] == div.div.findResult { text.contains('2') ? text : null }
    }
  }

  private assertElementTagNames(int expectedCount, String name, SelectorDsl selector) {
    int count = 0
    selector.each { item ->
      assert item instanceof BaseElementDsl
      assert item.tagName == name
      ++count
    }
    assert count == expectedCount
  }

}