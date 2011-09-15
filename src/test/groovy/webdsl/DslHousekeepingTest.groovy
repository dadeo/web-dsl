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


class DslHousekeepingTest extends AbstractServerTest {
  def map

  void setUp() {
    map = [key: 'value']
    super.setUp()
  }
  
  void test_closure_result_returned() {
    assertEquals 3, web.do {3}
  }

  void test_access_field() {
    web.do {
      assertEquals 'value', map.key
    }
  }

  void test_modify_field() {
    web.do {
      map = [key2: 'value']
    }
    assertEquals([key2: 'value'], map)
  }

  void test_assertions_in_dsl() {
    shouldFail {
      web.do {
        fail("should fail")
      }
    }
  }

  void test_page_resets_getters() {
    web.do {
      assertTrue(exists('namedRainbow'))

      assertFalse exists('namedMain')

      namedRainbow.click()

      assertFalse exists('namedRainbow')

      assertTrue exists('namedMain')
    }
  }

//  void test_page_resets_getters_when_string_properties_are_used() {
//    web.do {
//      assertTrue exists('namedRainbow')
//
//      'Submit 1'.click()
//
//      println page.asXml()
//      assertFalse exists('namedRainbow')
//    }
//  }

}