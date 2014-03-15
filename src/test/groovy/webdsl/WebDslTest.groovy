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

import com.gargoylesoftware.htmlunit.WebConnection

class WebDslTest extends AbstractNonServerTest {
  private static final String WEBAPPS_DIRECTORY = 'src/test/resources/webapps/simple'

  void test_constructor_no_args() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl().init("http://localhost:8081/test.html")
      assert webDsl.title == 'test html'
    }
  }

  void test_constructor_url() {
    JettyRunner.withServer(webappsDirectory: WEBAPPS_DIRECTORY) {
      WebDsl webDsl = new WebDsl("http://localhost:8081/test.html")
      assert webDsl.title == 'test html'
    }
  }

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
}
