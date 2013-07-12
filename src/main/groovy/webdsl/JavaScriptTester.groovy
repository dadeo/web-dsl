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

class JavaScriptTester {
  private javascriptLocations

  JavaScriptTester(Map options = [:]) {
    javascriptLocations = []
    if (options.location) javascriptLocations << options.location
    if (options.locations) javascriptLocations.addAll(options.locations)
  }

  WebDsl test(String javascript = '') {
    String contents = """
      <!DOCTYPE html>
      <html>
        <head>
        </head>
        <body>
        <div id='results'></div>
        <script type="text/javascript" src="../scripts/assert.js"></script>
        ${
      javascriptLocations.collect { "<script type='text/javascript' src='$it'></script>" }.join("\n")
    }
        <script>
          $javascript
        </script>
        </body>
      </html>
    """

    WebPageDslBuilder webPageDslBuilder = new WebPageDslBuilder()
        .baseUrl("http://localhost/test.html")
        .pageContents(contents)

    javascriptLocations.each {
      webPageDslBuilder = webPageDslBuilder.setResponseFor "http://localhost/$it", new File(it).text, 'application/javascript'
    }

    webPageDslBuilder = webPageDslBuilder.setResponseFor("http://localhost/scripts/assert.js", """
      function assert(value, desc) {
        var resultsList = document.getElementById("results");
        if (!resultsList) {
          resultsList = document.createElement('ul');
          document.getElementsByTagName('body')[0].appendChild(resultsList);
          resultsList.setAttribute('id','results');
        }
        var li = document.createElement("li");
        li.className = value ? "pass" : "fail";
        li.appendChild(document.createTextNode(desc));
        resultsList.appendChild(li);
      }
    """, 'application/javascript')

    WebDsl webDsl = webPageDslBuilder.build()

    webDsl.do {
      def failures = $$('#results li.fail')
      if (failures) {
        String failureMessages = failures.text.join('\n')
        assert !failureMessages
      }
    }

    webDsl
  }
}