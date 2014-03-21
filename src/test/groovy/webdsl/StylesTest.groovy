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

import org.junit.Test

@Mixin(NonServerMixin)
class StylesTest {

  @Test
  void test_element_with_inline_style() {
    html {
      div "my text", id: 'mydiv', style: 'margin-bottom: 10px;width:100px'
    }

    webdsl { WebDsl dsl ->
      assert $('#mydiv').style.marginBottom == '10px'
      assert $('#mydiv').style.width == '100px'
    }
  }

  @Test
  void test_element_with_in_page_css_styles() {
    html """
      <html>
        <head>
          <style type="text/css">
            #mydiv {
              width: 100px;
            }
          </style>
        </head>
        <body>
          <div id="mydiv">the contents</div>
        </body>
      </html>
    """

    webdsl {
      assert $('#mydiv').style.width == '100px'
    }
  }

  @Test
  void test_element_with_external_css_style() {
    css """
      div {
        margin: 25px;
      }

      #mydiv {
        width: 100px;
        margin-top: 40px;
      }
    """

    html {
      div "my text", id: 'mydiv'
    }

    webdsl {
      assert $('#mydiv').style.width == '100px'
    }
  }

  @Test
  void test_element_with_inline_style_overrides_external_css_style() {
    css """
      div {
        margin: 25px;
      }

      #mydiv {
        width: 100px;
        margin-top: 40px;
      }
    """

    html {
      div "my text", id: 'mydiv', style: 'width:50px'
    }

    webdsl {
      assert $('#mydiv').style.margin == '25px'
      assert $('#mydiv').style.marginTop == '40px'
      assert $('#mydiv').style.width == '50px'
    }
  }

  @Test
  void test_element_with_multiple_inline_styles_parse_whitespace_correctly() {
    html {
      div "my text", id: 'mydiv', style: ' width : 50px; margin-top: 40px;'
    }

    webdsl {
      assert $('#mydiv').style.width == '50px'
      assert $('#mydiv').style.marginTop == '40px'
    }
  }
}