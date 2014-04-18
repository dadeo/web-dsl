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

import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.WebConnection
import org.junit.Test

@Mixin(NonServerMixin)
class WebDslTest {
  private static final String WEBAPPS_DIRECTORY = 'src/test/resources/webapps/simple'

  @Test
  void test_constructor_no_args() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl().init("http://localhost:8081/test.html")
      assert webDsl.title == 'test html'
    }
  }

  @Test
  void test_constructor_url() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html")
      assert webDsl.title == 'test html'
      assert webDsl.webClient.browserVersion == BrowserVersion.default
    }
  }

  @Test
  void test_constructor_url_and_browser_version() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl(BrowserVersion.CHROME, "http://localhost:8081/test.html")
      assert webDsl.title == 'test html'
      assert webDsl.webClient.browserVersion == BrowserVersion.CHROME
    }
  }

  @Test
  void test_alertHandler_configured_by_default() {
    html """
      <script>
        alert('hello world!!!');
        alert('good-bye world!!!');
      </script>
    """

    withWebConnection { WebConnection connection ->
      WebDsl webDsl = new WebDsl()
      webDsl.webClient.webConnection = connection
      webDsl.init('http://localhost')

      assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']
    }
  }

  @Test
  void test_constructor_browser_version() {
    html """
      <div id="myDiv">yo</div>
    """

    withWebConnection { WebConnection connection ->
      WebDsl webDsl = new WebDsl(BrowserVersion.CHROME)
      webDsl.webClient.webConnection = connection
      webDsl.init('http://localhost')

      assert webDsl.$('#myDiv').text == 'yo'
      assert webDsl.webClient.browserVersion == BrowserVersion.CHROME
    }
  }

  @Test
  void test_alerts_compound() {
    html """
      <script>
        alert('hello world!!!');
        alert('good-bye world!!!');
      </script>
    """

    withWebConnection { WebConnection connection ->
      WebDsl webDsl = new WebDsl()
      webDsl.webClient.webConnection = connection
      webDsl.init('http://localhost')

      assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']

      webDsl.init('http://localhost')

      assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!', 'hello world!!!', 'good-bye world!!!']
    }
  }

  @Test
  void test_alertHandler_alerts_clear() {
    html """
      <script>
        alert('hello world!!!');
        alert('good-bye world!!!');
      </script>
    """

    withWebConnection { WebConnection connection ->
      WebDsl webDsl = new WebDsl()
      webDsl.webClient.webConnection = connection
      webDsl.init('http://localhost')

      assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']

      webDsl.alerts.clear()

      assert webDsl.alerts == []

      webDsl.init('http://localhost')

      assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']
    }
  }

  @Test
  void test_javascriptEnabled() {
    WebDsl dsl = new WebDsl()

    assert dsl.javaScriptEnabled
    assert dsl.webClient.options.javaScriptEnabled

    dsl.javaScriptEnabled = false

    assert !dsl.javaScriptEnabled
    assert !dsl.webClient.options.javaScriptEnabled

    dsl.javaScriptEnabled = true

    assert dsl.javaScriptEnabled
    assert dsl.webClient.options.javaScriptEnabled
  }

  @Test
  void test_enableJavaScript() {
    WebDsl dsl = new WebDsl()

    dsl.javaScriptEnabled = false

    dsl.enableJavaScript()

    assert dsl.javaScriptEnabled
  }

  @Test
  void test_disableJavaScript() {
    WebDsl dsl = new WebDsl()

    dsl.disableJavaScript()

    assert !dsl.javaScriptEnabled
  }

  @Test
  void test_throwExceptionOnFailingStatusCode_defaults_to_false() {
    WebDsl dsl = new WebDsl()
    assert !dsl.throwExceptionOnFailingStatusCode
  }

  @Test
  void test_throwExceptionOnFailingStatusCode_is_modifiable() {
    WebDsl dsl = new WebDsl()
    dsl.throwExceptionOnFailingStatusCode = true
    assert dsl.throwExceptionOnFailingStatusCode
  }
}
