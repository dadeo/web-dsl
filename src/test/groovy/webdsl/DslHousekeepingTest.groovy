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

import webdsl.support.SelectDsl
import webdsl.support.DslFactory
import com.gargoylesoftware.htmlunit.html.HtmlSelect


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

  void test_factory_no_override_of_select() {
    web.do {
      assert auto instanceof SelectDsl
    }
  }

  void test_factory_override_of_select() {
    web.do {
      handle HtmlSelect with OtherDsl

      assert auto instanceof OtherDsl
    }
  }

  void test_factory_defaults_to_reset() {
    web.do {
      handle HtmlSelect with OtherDsl

      assert auto instanceof OtherDsl
    }

    web.do {
      assert auto instanceof SelectDsl
    }
  }

  void test_factory_reset_can_be_overridden() {
    web.do {
      factoryResets = false

      handle HtmlSelect with OtherDsl

      assert auto instanceof OtherDsl
    }

    web.do {
      assert auto instanceof OtherDsl
    }
  }


  static class OtherDsl {
    OtherDsl(pageContainer, DslFactory factory, element) {
    }
  }
}