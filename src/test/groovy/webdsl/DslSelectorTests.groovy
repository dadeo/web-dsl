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


class DslSelectorTests extends AbstractServerTests {

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
      assertEquals(["first", "last", "Pinky", "Jones", "Winky", "Jones"], names.tr.td.collect { it.text })
      assertEquals(["imageFirst.gif", "imageLast.gif"], names.tr[0].td.img.collect { it.attr("src") })
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