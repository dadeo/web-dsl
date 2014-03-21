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
import groovy.xml.StreamingMarkupBuilder
import junit.framework.TestCase

abstract class AbstractNonServerTest extends TestCase {
  private String contents
  private String cssContents
  private String jsContents

  def html(String contents) {
    this.contents = contents
  }

  def html(Closure closure) {
    contents = new StreamingMarkupBuilder().bind(closure).toString()
  }

  def css(String cssContents) {
    this.cssContents = cssContents
  }

  def js(String jsContents) {
    this.jsContents = jsContents
  }

  def webdsl(Closure closure) {
    withBuilder { WebPageDslBuilder builder ->
      WebDsl webDsl = builder.build()
      webDsl.do closure
    }
  }

  def withWebConnection(Closure closure) {
    withBuilder { WebPageDslBuilder builder ->
      closure(builder.buildWebConnection())
    }
  }

  private def withBuilder(Closure closure) {
    String modifiedContents = contents

    String cssLink = cssContents ? '<link rel="stylesheet" type="text/css" href="test.css"></link>' : ""
    String scriptTag = jsContents ? '<script src="jquery.js"></script>' : ""

    if (cssContents || jsContents) {
      modifiedContents = """
        <html>
          <head>
            $cssLink
            $scriptTag
          </head>
          <body>
            $contents
          </body>
        </html>
      """
    }

    WebPageDslBuilder builder = new WebPageDslBuilder()
        .browserVersion(BrowserVersion.CHROME)
        .defaultUrl("http://localhost/test.html")
        .defaultContents(modifiedContents)

    if (cssContents)
      builder = builder.setResponseFor("test.css", cssContents, "text/css")

    if (jsContents)
      builder = builder.setResponseFor("jquery.js", jsContents, 'application/javascript')

    closure(builder)
  }
}