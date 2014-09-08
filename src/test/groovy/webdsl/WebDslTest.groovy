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
import com.gargoylesoftware.htmlunit.WebClient
import org.junit.Test

@Mixin(NonServerMixin)
class WebDslTest {
  private static final String WEBAPPS_DIRECTORY = 'src/test/resources/webapps/simple'

  @Test
  void test_constructor_no_args() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl().init("http://localhost:8081/test.html")

      assert webDsl.title == 'test html'

      assert webDsl.webClient.browserVersion == BrowserVersion.default
      assert webDsl.javaScriptEnabled
      assert !webDsl.printContentOnFailingStatusCode
      assert !webDsl.throwExceptionOnFailingStatusCode
    }
  }

  @Test
  void test_constructor_url() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html")

      assert webDsl.title == 'test html'

      assert webDsl.webClient.browserVersion == BrowserVersion.default
      assert webDsl.javaScriptEnabled
      assert !webDsl.printContentOnFailingStatusCode
      assert !webDsl.throwExceptionOnFailingStatusCode
    }
  }

  @Test
  void test_constructor_url_and_option_and_customizer() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html",
                                 new WebDsl.Options(browserVersion: BrowserVersion.CHROME),
                                 { dsl ->
                                   assert dsl instanceof WebDsl
                                   assert dsl.webClient instanceof WebClient
                                   assert dsl.page == null
                                   assert dsl.webClient.browserVersion == BrowserVersion.CHROME
                                   assert dsl.webClient.options.cssEnabled
                                   dsl.webClient.options.cssEnabled = false
                                 })

      assert webDsl.title == 'test html'

      assert webDsl.javaScriptEnabled
      assert !webDsl.printContentOnFailingStatusCode
      assert !webDsl.throwExceptionOnFailingStatusCode
      assert !webDsl.webClient.options.cssEnabled
    }
  }

  @Test
  void test_constructor_url_and_option_browserVersion_overridden() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html",
                                 new WebDsl.Options(browserVersion: BrowserVersion.CHROME))

      assert webDsl.title == 'test html'

      assert webDsl.webClient.browserVersion == BrowserVersion.CHROME
      assert webDsl.javaScriptEnabled
      assert !webDsl.printContentOnFailingStatusCode
      assert !webDsl.throwExceptionOnFailingStatusCode
    }
  }

  @Test
  void test_constructor_url_and_option_javaScriptEnabled_overridden() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html",
                                 new WebDsl.Options(javaScriptEnabled: false))

      assert webDsl.title == 'test html'

      assert webDsl.webClient.browserVersion == BrowserVersion.default
      assert !webDsl.javaScriptEnabled
      assert !webDsl.printContentOnFailingStatusCode
      assert !webDsl.throwExceptionOnFailingStatusCode
    }
  }

  @Test
  void test_constructor_url_and_option_printContentOnFailingStatusCode_overridden() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html",
                                 new WebDsl.Options(printContentOnFailingStatusCode: true))

      assert webDsl.title == 'test html'

      assert webDsl.webClient.browserVersion == BrowserVersion.default
      assert webDsl.javaScriptEnabled
      assert webDsl.printContentOnFailingStatusCode
      assert !webDsl.throwExceptionOnFailingStatusCode
    }
  }

  @Test
  void test_constructor_url_and_option_throwExceptionOnFailingStatusCode_overridden() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html",
                                 new WebDsl.Options(throwExceptionOnFailingStatusCode: true))

      assert webDsl.title == 'test html'

      assert webDsl.webClient.browserVersion == BrowserVersion.default
      assert webDsl.javaScriptEnabled
      assert !webDsl.printContentOnFailingStatusCode
      assert webDsl.throwExceptionOnFailingStatusCode
    }
  }

  @Test
  void test_constructor_url_and_web_connection() {
    html """
      <div id="myDiv">yo</div>
    """

    WebDsl webDsl = new WebDsl('http://localhost', createWebConnection())

    assert webDsl.$('#myDiv').text == 'yo'

    assert webDsl.webClient.browserVersion == BrowserVersion.default
    assert webDsl.javaScriptEnabled
    assert !webDsl.printContentOnFailingStatusCode
    assert !webDsl.throwExceptionOnFailingStatusCode
  }

  @Test
  void test_constructor_web_connection() {
    html """
      <div id="myDiv">yo</div>
    """

    WebDsl webDsl = new WebDsl(createWebConnection())
    webDsl.init('http://localhost')

    assert webDsl.$('#myDiv').text == 'yo'

    assert webDsl.webClient.browserVersion == BrowserVersion.default
    assert webDsl.javaScriptEnabled
    assert !webDsl.printContentOnFailingStatusCode
    assert !webDsl.throwExceptionOnFailingStatusCode
  }

  @Test
  void test_constructor_url_and_options_and_web_connection() {
    html """
      <div id="myDiv">yo</div>
    """

    WebDsl webDsl = new WebDsl('http://localhost',
                               createWebConnection(),
                               [
                                   browserVersion: BrowserVersion.CHROME,
                                   javaScriptEnabled: false,
                                   printContentOnFailingStatusCode: true,
                                   throwExceptionOnFailingStatusCode: true
                               ] as WebDsl.Options)

    assert webDsl.$('#myDiv').text == 'yo'

    assert webDsl.webClient.browserVersion == BrowserVersion.CHROME
    assert !webDsl.javaScriptEnabled
    assert webDsl.printContentOnFailingStatusCode
    assert webDsl.throwExceptionOnFailingStatusCode
  }

  @Test
  void test_constructor_url_and_options_and_web_connection_and_customizer() {
    html """
      <div id="myDiv">yo</div>
    """

    WebDsl webDsl = new WebDsl('http://localhost',
                               createWebConnection(),
                               [
                                   browserVersion: BrowserVersion.CHROME,
                                   javaScriptEnabled: false,
                                   printContentOnFailingStatusCode: true,
                                   throwExceptionOnFailingStatusCode: true
                               ] as WebDsl.Options,
                               { dsl ->
                                 assert dsl instanceof WebDsl
                                 assert dsl.webClient instanceof WebClient
                                 assert dsl.page == null
                                 assert dsl.webClient.browserVersion == BrowserVersion.CHROME
                                 assert dsl.webClient.options.cssEnabled
                                 dsl.webClient.options.cssEnabled = false
                               })

    assert webDsl.$('#myDiv').text == 'yo'

    assert webDsl.webClient.browserVersion == BrowserVersion.CHROME
    assert !webDsl.javaScriptEnabled
    assert webDsl.printContentOnFailingStatusCode
    assert webDsl.throwExceptionOnFailingStatusCode
    assert !webDsl.webClient.options.cssEnabled
  }

  @Test
  void test_alertHandler_configured_by_default() {
    html """
      <script>
        alert('hello world!!!');
        alert('good-bye world!!!');
      </script>
    """

    WebDsl webDsl = new WebDsl('http://localhost', createWebConnection())

    assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']
  }

  @Test
  void test_alerts_compound() {
    html """
      <script>
        alert('hello world!!!');
        alert('good-bye world!!!');
      </script>
    """

    WebDsl webDsl = new WebDsl('http://localhost', createWebConnection())

    assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']

    webDsl.init('http://localhost')

    assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!', 'hello world!!!', 'good-bye world!!!']
  }

  @Test
  void test_alertHandler_alerts_clear() {
    html """
      <script>
        alert('hello world!!!');
        alert('good-bye world!!!');
      </script>
    """

    WebDsl webDsl = new WebDsl('http://localhost', createWebConnection())

    assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']

    webDsl.alerts.clear()

    assert webDsl.alerts == []

    webDsl.init('http://localhost')

    assert webDsl.alerts == ['hello world!!!', 'good-bye world!!!']
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
  void test_throwExceptionOnFailingStatusCode_is_modifiable() {
    WebDsl dsl = new WebDsl()
    dsl.throwExceptionOnFailingStatusCode = true
    assert dsl.throwExceptionOnFailingStatusCode
    assert dsl.webClient.options.throwExceptionOnFailingStatusCode == true
  }

  @Test
  void test_printContentOnFailingStatusCode_is_modifiable() {
    WebDsl dsl = new WebDsl()
    dsl.printContentOnFailingStatusCode = true
    assert dsl.printContentOnFailingStatusCode
    assert dsl.webClient.options.printContentOnFailingStatusCode == true
  }

  @Test
  void test_httpGet_url_and_parameters() {
    JettyRunner.withServer() {
      WebDsl webDsl = new WebDsl()

      webDsl.httpGet(new URL("http://localhost:8081/testit/test.html"), [a: 1, b: 2, c: null, d: 4])

      assert webDsl.title == 'TestServlet test.html'
      assert webDsl.$('#methodType').value == 'GET'
      assert webDsl.$('#parameters').do(offset:1).as.object == [a: '1', b: '2', c:'', d: '4']
    }
  }

  @Test
  void test_httpGet_url_and_no_parameters() {
    JettyRunner.withServer() {
      WebDsl webDsl = new WebDsl()

      webDsl.httpGet(new URL("http://localhost:8081/testit/test.html"))

      assert webDsl.title == 'TestServlet test.html'
      assert webDsl.$('#methodType').value == 'GET'
      assert webDsl.$('#parameters').do(offset:1).as.object == [:]
    }
  }

  @Test
  void test_httpGet_url_string_and_parameters() {
    JettyRunner.withServer() {
      WebDsl webDsl = new WebDsl()

      webDsl.httpGet("http://localhost:8081/testit/test.html", [a: 1, b: 2, c: null, d: 4])

      assert webDsl.title == 'TestServlet test.html'
      assert webDsl.$('#methodType').value == 'GET'
      assert webDsl.$('#parameters').do(offset:1).as.object == [a: '1', b: '2', c:'', d: '4']
    }
  }

  @Test
  void test_httpGet_url_string_and_no_parameters() {
    JettyRunner.withServer() {
      WebDsl webDsl = new WebDsl()

      webDsl.httpGet("http://localhost:8081/testit/test.html")

      assert webDsl.title == 'TestServlet test.html'
      assert webDsl.$('#methodType').value == 'GET'
      assert webDsl.$('#parameters').do(offset:1).as.object == [:]
    }
  }

  @Test
  void test_httpPost_url_and_parameters() {
    JettyRunner.withServer() {
      WebDsl webDsl = new WebDsl()

      webDsl.httpPost(new URL("http://localhost:8081/testit/test.html"), [a: 1, b: 2, c: null, d: 4])

      assert webDsl.title == 'TestServlet test.html'
      assert webDsl.$('#methodType').value == 'POST'
      assert webDsl.$('#parameters').do(offset:1).as.object == [a: '1', b: '2', c:'', d: '4']
    }
  }

  @Test
  void test_httpPost_url_string_and_parameters() {
    JettyRunner.withServer() {
      WebDsl webDsl = new WebDsl()

      webDsl.httpPost("http://localhost:8081/testit/test.html", [a: 1, b: 2, c: null, d: 4])

      assert webDsl.title == 'TestServlet test.html'
      assert webDsl.$('#methodType').value == 'POST'
      assert webDsl.$('#parameters').do(offset:1).as.object == [a: '1', b: '2', c:'', d: '4']
    }
  }
}
