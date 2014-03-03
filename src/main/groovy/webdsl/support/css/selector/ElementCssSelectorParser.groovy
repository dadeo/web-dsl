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
package webdsl.support.css.selector

import webdsl.support.matchers.*

import java.util.regex.Matcher

class ElementCssSelectorParser {
  List<CssSelector> parse(String selector) {

    String tag = /[^#.\[\s]+/
    String clazz = /\./ + tag
    String selectorId = /#/ + tag
    String attributesAll = /\[\s*([^$^|*~=\]]*)\s*(?:([$^|*~])?=\s*('|")?([^'"\]]*)\7?)?\s*\]/
    String regex = "($tag)?($selectorId)?($clazz)?((?:$attributesAll)+)?"

    String attributesRegex = attributesAll.replace(/\7/, /\3/)

    List<ElementCssSelector> result = []

    Matcher m = selector =~ regex
    while (m.find()) {
      String id = m.group(2)
      String tagName = m.group(1)
      String cssClass = m.group(3)
      String attributesString = m.group(4)

      if (id || tagName || cssClass || attributesString) {
        Map<String, String> attributes = [:]

        if (id)
          id -= '#'

        if (cssClass)
          attributes.class = LIST_CONTAINS(cssClass - ".")

        if (attributesString) {
          Matcher attributesMatcher = attributesString =~ attributesRegex
          while(attributesMatcher.find()) {
            String attributeName = attributesMatcher.group(1)
            String matchType = attributesMatcher.group(2)
            String attributeValue = attributesMatcher.group(4)
            attributes[attributeName] = createAttributeMatcher(attributeValue, matchType)
          }
        }

        result << new ElementCssSelector(id, tagName, attributes)
      }
    }

    result
  }

  private def createAttributeMatcher(String attributeValue, String matchType) {
    switch (matchType) {
      case '^':
        return STARTS_WITH(attributeValue)
      case '$':
        return ENDS_WITH(attributeValue)
      case '*':
        return CONTAINS(attributeValue)
      case '~':
        return LIST_CONTAINS(attributeValue)
      case '|':
        return STARTS_WITH_HYPHENATED(attributeValue)
      default:
        return attributeValue ? EQ(attributeValue) : ALWAYS()
    }
  }

  static EQ(String value) {
    new EqualsMatcher(value)
  }

  static ALWAYS() {
    new AlwaysMatcher()
  }

  static STARTS_WITH(String value) {
    new StartsWithMatcher(value)
  }

  static ENDS_WITH(String value) {
    new EndsWithMatcher(value)
  }

  static CONTAINS(String value) {
    new ContainsMatcher(value)
  }

  static LIST_CONTAINS(String value) {
    new ListContainsMatcher(value)
  }

  static STARTS_WITH_HYPHENATED(String value) {
    new StartsWithHyphenatedMatcher(value)
  }
}