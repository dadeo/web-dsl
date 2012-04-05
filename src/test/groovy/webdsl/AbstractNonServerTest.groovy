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

import groovy.xml.StreamingMarkupBuilder
import junit.framework.TestCase
import com.gargoylesoftware.htmlunit.MockWebConnection
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.BrowserVersion

abstract class AbstractNonServerTest extends TestCase {
  private contents

  def html(String contents) {
    this.contents = contents
  }

  def html(Closure closure) {
    contents = new StreamingMarkupBuilder().bind(closure).toString()
  }

  def webdsl(Closure closure) {
    def browserVersion = BrowserVersion.getDefault()
    def webClient = new WebClient(browserVersion)
    final MockWebConnection webConnection = new MockWebConnection();
    webConnection.setDefaultResponse(contents);
    webClient.setWebConnection(webConnection);

    WebDsl web = new WebDsl()
    web.webClient = webClient
    web.for("http://localhost/test.html")
    web.do closure
  }

    //    final List<NameValuePair> expectedParameters = Collections.emptyList();
    //    final MockWebConnection webConnection = getMockConnection(secondPage);
    //
    //    assertEquals("url", "http://www.foo2.com/", secondPage.getWebResponse().getWebRequest().getUrl());
    //    assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
    //    Assert.assertEquals("parameters", expectedParameters, webConnection.getLastParameters());
    //    assertNotNull(secondPage);
}