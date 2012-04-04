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

import com.gargoylesoftware.htmlunit.html.HtmlSelect
import webdsl.support.DslFactory
import webdsl.support.SelectDsl

class DslHousekeepingTest extends AbstractServerTest {
  def map

  void setUp() {
    map = [key: 'value']
    super.setUp()
  }

  void test_closure_result_returned() {
    assert webdsl {3} == 3
  }

  void test_access_field() {
    webdsl {
      assert map.key == 'value'
    }
  }

  void test_modify_field() {
    webdsl {
      map = [key2: 'value']
    }
    assert map == [key2: 'value']
  }

  void test_assertions_in_dsl() {
    shouldFail {
      webdsl {
        fail("should fail")
      }
    }
  }

  void test_factory_no_override_of_select() {
    webdsl {
      assert auto instanceof SelectDsl
    }
  }

  void test_factory_override_of_select() {
    webdsl {
      handle HtmlSelect with OtherDsl

      assert auto instanceof OtherDsl
    }
  }

  void test_factory_defaults_to_reset() {
    webdsl(
        {
          handle HtmlSelect with OtherDsl

          assert auto instanceof OtherDsl
        },

        {
          assert auto instanceof SelectDsl
        }
    )
  }

  void test_factory_reset_can_be_overridden() {
    webdsl(
        {
          factoryResets = false

          handle HtmlSelect with OtherDsl

          assert auto instanceof OtherDsl
        },

        {
          assert auto instanceof OtherDsl
        }
    )
  }


  static class OtherDsl {
    OtherDsl(pageContainer, DslFactory factory, element) {
    }
  }
}