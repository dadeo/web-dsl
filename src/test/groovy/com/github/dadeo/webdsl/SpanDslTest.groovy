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
class SpanDslTest {

  @Test
  void test_getValue() {
    html {
      span id:'id', ' MY VALUE '
    }

    webdsl {
      assert $("#id").value == "MY VALUE"
      assert $("#id").untrimmedValue == " MY VALUE "
    }
  }

  @Test
  void test_getValue_with_additional_markup() {
    html "<span id='id'>this is <span>marked up</span> text</span>"

    webdsl {
      assert $("#id").value == "this is marked up text"
      assert $("#id").untrimmedValue == "this is marked up text"
    }
  }

  @Test
  void test_setValue() {
    html {
      span id:'id', ' MY VALUE '
    }

    webdsl {
      $("#id").value = " MYVALUE "

      assert $("#id").value == "MYVALUE"
      assert $("#id").untrimmedValue == " MYVALUE "
    }
  }

  void test_getText() {
    html {
      span id:'id', ' MY VALUE '
    }

    webdsl {
      assert $("#id").text == "MY VALUE"
      assert $("#id").untrimmedText == " MY VALUE "
    }
  }

  @Test
  void test_setText() {
    html {
      span id:'id', ' MY VALUE '
    }

    webdsl {
      $("#id").text = " MYVALUE "

      assert $("#id").text == "MYVALUE"
      assert $("#id").untrimmedText == " MYVALUE "
    }
  }

  @Test
  void test_asXml() {
    html {
      span id:'id', 'OLD VALUE'
    }

    webdsl {
      $("#id").value = "MY VALUE"

      assert $('#id').asXml().replaceAll('\r\n', '\n') == """<span id="id">
                                                             |  MY VALUE
                                                             |</span>
                                                             |""".stripMargin()
                                                                 .replaceAll('\r\n', '\n')
    }
  }

  @Test
  void test_click() {
    html """
      <span id="id" onclick="echo();">abcd</span>
      <input id="echoedText" type="text" value=""/>

      <script>
        function echo() {
          var textToEcho = document.getElementById('id').textContent;
          document.getElementById('echoedText').value = textToEcho;
        }
      </script>
    """

    webdsl {
      $('#id').click()
      assert echoedText.value == 'abcd'
    }
  }

}