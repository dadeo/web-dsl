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
import org.junit.Before
import org.junit.Test
import webdsl.support.BaseElementDsl
import webdsl.support.DslFactory
import webdsl.support.SelectDsl

@Mixin(NonServerMixin)
class DslHousekeepingTest {
  def map

  @Before
  void setUp() {
    map = [key: 'value']
  }

  @Test
  void test_closure_result_returned() {
    assert webdsl { 3 } == 3
  }

  @Test
  void test_access_field() {
    webdsl {
      assert map.key == 'value'
    }
  }

  @Test
  void test_modify_field() {
    webdsl {
      map = [key2: 'value']
    }
    assert map == [key2: 'value']
  }

  @Test
  void test_assertions_in_dsl() {
    boolean failed = false

    try {
      webdsl {
        fail("should fail")
      }
    } catch (e) {
      failed = true
    }

    assert failed
  }

  @Test
  void test_factory_no_override_of_select() {
    html {
      select(name: 'auto')
    }

    webdsl {
      assert auto instanceof SelectDsl
    }
  }

  @Test
  void test_factory_override_of_select() {
    html {
      select(name: 'auto')
    }

    webdsl {
      handle HtmlSelect with OtherDsl

      assert auto instanceof OtherDsl
    }
  }

  @Test
  void test_factory_defaults_to_reset() {
    html {
      select(name: 'auto')
    }

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

  @Test
  void test_factory_reset_can_be_overridden() {
    html {
      select(name: 'auto')
    }

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


  static class OtherDsl extends BaseElementDsl {
    OtherDsl(pageContainer, DslFactory factory, element) {
      super(pageContainer, factory, element)
    }
  }
}