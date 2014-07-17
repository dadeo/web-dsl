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

import org.junit.After
import org.junit.Test


class JettyRunnerTest {
  private static final String DEFAULT_TEST_URL_BASE = 'http://localhost:8081/testit'
  private JettyRunner runner

  @After
  void tearDown() {
    runner?.stop()
  }

  @Test
  void test_testit_dynamic_page_title() {
    runner = new JettyRunner()
    runner.start()

    new WebDsl().for(DEFAULT_TEST_URL_BASE + "/now").do {
      assert title == 'TestServlet now'
    }
  }

  @Test
  void test_testit_displays_request_parameters() {
    runner = new JettyRunner()
    runner.start()

    new WebDsl().for(DEFAULT_TEST_URL_BASE + "/now?abc=1&def=3&def=2").do {
      assert title == 'TestServlet now'

      assert $('#parameters').as.objects(names: ['key', 'value']) == [[key:'abc', value:'1'], [key:'def', value:'2, 3']]
    }
  }
}