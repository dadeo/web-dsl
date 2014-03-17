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
import com.gargoylesoftware.htmlunit.WebConnection
import webdsl.support.UrlBuilder


class WebPageDslBuilder {
  private Map definition

  WebPageDslBuilder() {
    this([baseUrl: 'http://localhost', responses: [].asImmutable(), browserVersion:BrowserVersion.getDefault()])
  }

  WebPageDslBuilder(Map definition) {
    this.definition = definition.asImmutable()
  }

  WebPageDslBuilder defaultUrl(String baseUrl) {
    new WebPageDslBuilder(definition + [baseUrl: baseUrl])
  }

  WebPageDslBuilder defaultContents(String pageContents) {
    new WebPageDslBuilder(definition + [pageContents: pageContents])
  }

  WebPageDslBuilder setResponseFor(String url, String responseContent, String contentType) {
    List responses = new ArrayList(definition.responses)
    responses.add([url: url, responseContent: responseContent, contentType: contentType])
    new WebPageDslBuilder(definition + [responses: responses.asImmutable()])
  }

  WebPageDslBuilder browserVersion(BrowserVersion browserVersion) {
    new WebPageDslBuilder(definition + [browserVersion: browserVersion])
  }

  WebConnection buildWebConnection() {
    UrlBuilder urlBuilder = new UrlBuilder(definition.baseUrl)

    MockWebConnection webConnection = new MockWebConnection()
    definition.responses.each {
      webConnection.setResponse urlBuilder.build(it.url).toURL(), it.responseContent, it.contentType
    }

    webConnection.setDefaultResponse(definition.pageContents);

    webConnection
  }

  WebDsl build(String destinationUrl = null) {
    def webClient = new WebClient(definition.browserVersion)
    webClient.setWebConnection(buildWebConnection());

    new WebDsl(webClient, destinationUrl ?: definition.baseUrl)
  }
}