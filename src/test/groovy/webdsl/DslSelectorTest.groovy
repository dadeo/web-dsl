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

import org.junit.Before
import org.junit.Test
import webdsl.support.BaseElementDsl
import webdsl.support.SelectorDsl

@Mixin(ServerMixin)
class DslSelectorTest {

  @Before
  @Test
  void setUp() {
    defaultPage = "selector"
  }

  @Test
  void test_divs() {
    webdsl {
      assert div.size() == 3
      assert div[1].div.size() == 2
      assert div[1].div[0].text == "2.1"
      assert div[1].div[1].text == "2.2"
      assert div2.div[1].text == "2.2"
    }
  }

  @Test
  void test_selector_built_correctly() {
    webdsl {
      assertElementTagNames(3, "tr", names.tbody[0].tr)
      assertElementTagNames(6, "td", names.tbody[0].tr.td)
    }
  }

  @Test
  void test_table_tbody_optional() {
    webdsl {
      assert names.tbody[0].tr.td.text == ["first", "last", "Pinky", "Jones", "Winky", "Jones"]
      assert names.tr.td.text == ["first", "last", "Pinky", "Jones", "Winky", "Jones"]
    }
  }

  @Test
  void test_selector_supports_collect_method() {
    webdsl {
      assert names.tr.td.collect { text } == ["first", "last", "Pinky", "Jones", "Winky", "Jones"]
      assert div[2].div.collect { attr("id") } == ['div3_1', 'div3_2', 'div3_3', 'div3_4']
    }
  }

  @Test
  void test_selector_each() {
    webdsl {
      def names = []
      table[0].tr.td.each { names << text }
      assert ['first', 'last', 'Pinky', 'Jones', 'Winky', 'Jones'] == names
    }
  }

  @Test
  void test_selector_find() {
    webdsl {
      assert "WinkyJones" == table[0].tr.find { td.find { text == "Winky" } }.text
      assert "PinkyJones" == table[0].tr.find { td.find { text == "Jones" } }.text
    }
  }

  @Test
  void test_selector_findAll() {
    webdsl {
      assert ['1.2', '2.1', '2.2', '3.2'] == div.div.findAll { text.contains('2') }.text
    }
  }

  @Test
  void test_selector_findResult() {
    webdsl {
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