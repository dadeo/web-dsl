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
import com.gargoylesoftware.htmlunit.MockWebConnection
import com.gargoylesoftware.htmlunit.WebClient


class WebPageDslBuilder {
  private Map definition

  WebPageDslBuilder() {
    this([responses: [].asImmutable()])
  }

  WebPageDslBuilder(Map definition) {
    this.definition = definition.asImmutable()
  }

  WebPageDslBuilder baseUrl(String baseUrl) {
    new WebPageDslBuilder(definition + [baseUrl: baseUrl])
  }

  WebPageDslBuilder pageContents(String pageContents) {
    new WebPageDslBuilder(definition + [pageContents: pageContents])
  }

  WebPageDslBuilder setResponseFor(String url, String responseContent, String contentType) {
    List responses = new ArrayList(definition.responses)
    responses.add([url: url, responseContent: responseContent, contentType: contentType])
    new WebPageDslBuilder(definition + [responses: responses.asImmutable()])
  }

  WebDsl build() {
    MockWebConnection webConnection = new MockWebConnection()
    definition.responses.each {
      webConnection.setResponse it.url.toURL(), it.responseContent, it.contentType
    }

    def browserVersion = BrowserVersion.getDefault()
    def webClient = new WebClient(browserVersion)
    webConnection.setDefaultResponse(definition.pageContents);
    webClient.setWebConnection(webConnection);

    WebDsl web = new WebDsl()
    web.webClient = webClient
    web.for(definition.baseUrl)
    web
  }
}