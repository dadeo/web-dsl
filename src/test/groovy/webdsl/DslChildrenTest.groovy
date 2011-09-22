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

class DslChildrenTest extends AbstractServerTest {

  protected String defaultPage() {
    return "children"
  }

  void test_children() {
    web.do {
      assert(['html', 'head', 'title', 'body', 'div', 'span', 'span'] == children.tagName)
    }
  }

  void test_children_of_type() {
    web.do {
      assert(['hello', 'world'] == children(type:'span').text)
      assert(['yo dog'] == children(type:'div').text)
    }
  }

  void test_children_of_type__allows_list() {
    web.do {
      assert(['hello', 'world'] == children(type:['span']).text)
    }
  }

  void test_children_of_types() {
    web.do {
      assert(['hello', 'world'] == children(types:['span']).text)
      assert(['yo dog'] == children(types:['div']).text)
      assert(['yo dog', 'hello', 'world'] == children(types:['span', 'div']).text)
    }
  }

  void test_children_of_types__allows_single_type() {
    web.do {
      assert(['hello', 'world'] == children(types: 'span').text)
    }
  }

  void test_children_of_type__none_found() {
    web.do {
      assert([] == children(types: 'table').text)
    }
  }

}