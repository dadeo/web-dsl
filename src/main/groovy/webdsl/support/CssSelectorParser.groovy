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
package webdsl.support

import webdsl.support.matchers.AlwaysMatcher
import webdsl.support.matchers.EqualsMatcher

import java.util.regex.Matcher

class CssSelectorParser {
  List<CssSelector> parse(String selector) {
    String regex = /([^.#\s\[]+)?#?([^.\s\[]+)?[.]?([^\s\[]*)(?:\[(.+?)(?:="(.+)")?\])?\s*/

    List<CssSelector> result = []

    Matcher m = selector.replaceAll(/'/, /"/) =~ regex
    while (m.find()) {
      String id = m.group(2)
      String tagName = m.group(1)
      String cssClass = m.group(3)
      String attributeName = m.group(4)
      String attributeValue = m.group(5)

      if (id || tagName || cssClass || attributeName) {
        Map<String, String> attributes = [:]

        if (cssClass)
          attributes.class = EQ(cssClass)

        if (attributeName)
          attributes[attributeName.intern()] = attributeValue ? EQ(attributeValue) : ALWAYS()

        result << new CssSelector(id, tagName, attributes)
      }
    }

    result
  }

  static EQ(String value) {
    new EqualsMatcher(value)
  }

  static ALWAYS() {
    new AlwaysMatcher()
  }
}