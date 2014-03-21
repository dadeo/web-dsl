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

@Mixin(ServerMixin)
class DslSelectorChildrenTest {

  @Before
  void setUp() {
    defaultPage = "selector"
  }

  @Test
  void test_selector_children() {
    webdsl {
      assert(['tbody', 'tr', 'td', 'td', 'tr', 'td', 'td', 'tr', 'td', 'td'] == table.children.tagName)
    }
  }

  @Test
  void test_selector_children_with_options() {
    webdsl {
      assert(['value 1', 'value 2', 'value 4', 'value 4'] == span.children(type:'span').text)
    }
  }

}