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

import webdsl.support.BaseElementDsl
import webdsl.support.SelectorDsl

class InlineHtmlTest extends AbstractServerTest {

  void test_test_invokes_closure() {
    def invoked

    test {
      html ''
      webdsl {
        invoked = true
      }
    }

    assert invoked
  }

  void test_test_returns_result() {
    def invoked

    def result = test {
      html ''
      webdsl {
        true
      }
    }

    assert result
  }

  void test_test_with_html_as_string() {
    test {
      html """
        <div id='mydiv'>hello world</div>
      """

      webdsl {
        assert mydiv.text == 'hello world'
      }
    }
  }

  void test_test_with_htmlbuilder() {
    test {
      html {
        div(id:'mydiv', 'hello world')
      }

      webdsl {
        assert mydiv.text == 'hello world'
      }
    }
  }

}