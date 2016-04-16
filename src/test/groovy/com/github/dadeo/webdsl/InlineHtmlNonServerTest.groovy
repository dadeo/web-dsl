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
package com.github.dadeo.webdsl

import org.junit.Test

@Mixin(NonServerMixin)
class InlineHtmlNonServerTest {

  @Test void test_test_invokes_closure() {
    def invoked

    webdsl {
      invoked = true
    }

    assert invoked
  }

  @Test void test_test_returns_result() {
    def invoked

    def result = webdsl {
      true
    }

    assert result
  }

  @Test void test_test_with_html_as_string() {
    html """
        <div id='mydiv'>hello world</div>
      """

    webdsl {
      assert mydiv.text == 'hello world'
    }
  }

  @Test void test_test_with_htmlbuilder() {
    html {
      div(id: 'mydiv', 'hello world')
    }

    webdsl {
      assert mydiv.text == 'hello world'
    }
  }

}