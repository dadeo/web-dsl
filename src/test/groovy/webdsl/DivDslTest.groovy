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

class DivDslTest extends AbstractNonServerTest {

  void test_getValue() {
    html {
      div id:'id', ' MY VALUE '
    }

    webdsl {
      assert $("#id").value == "MY VALUE"
      assert $("#id").untrimmedValue == " MY VALUE "
    }
  }

  void test_getValue_with_additional_markup() {
    html "<div id='id'>this is <span>marked up</span> text</div>"

    webdsl {
      assert $("#id").value == "this is marked up text"
      assert $("#id").untrimmedValue == "this is marked up text"
    }
  }

  void test_setValue() {
    html {
      div id:'id', ' MY VALUE '
    }

    webdsl {
      $("#id").value = " MYVALUE "

      assert $("#id").value == "MYVALUE"
      assert $("#id").untrimmedValue == " MYVALUE "
    }
  }

  void test_getText() {
    html {
      div id:'id', ' MY VALUE '
    }

    webdsl {
      assert $("#id").text == "MY VALUE"
      assert $("#id").untrimmedText == " MY VALUE "
    }
  }

  void test_setText() {
    html {
      div id:'id', ' MY VALUE '
    }

    webdsl {
      $("#id").text = " MYVALUE "

      assert $("#id").text == "MYVALUE"
      assert $("#id").untrimmedText == " MYVALUE "
    }
  }

  void test_asXml() {
    html {
      div id:'id', 'OLD VALUE'
    }

    webdsl {
      $("#id").value = "MY VALUE"

      assert $('#id').asXml().replaceAll('\r\n', '\n') == """<div id="id">
                                                             |  MY VALUE
                                                             |</div>
                                                             |""".stripMargin()
                                                                 .replaceAll('\r\n', '\n')
    }
  }

  void test_click() {
    html """
      <div id="id" onclick="echo();">abcd</div>
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