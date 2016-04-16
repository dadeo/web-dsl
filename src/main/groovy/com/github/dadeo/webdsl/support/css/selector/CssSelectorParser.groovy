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
package com.github.dadeo.webdsl.support.css.selector

import java.util.regex.Matcher


class CssSelectorParser {
  private ElementCssSelectorParser elementSelectorParser = new ElementCssSelectorParser()
  private RelationshipParser relationshipParser = new RelationshipParser()

  CssSelector parse(String selectorString) {
    Matcher matcher = selectorString =~ /([^,]+)[,]?/

    List<CssSelector> selectors = matcher.collect { parseOrFragment(it[1]) }
    selectors.size() == 1 ? selectors[0] : new OrCssSelector(selectors)
  }

  private CssSelector parseOrFragment(String selectorString) {
    List parts = relationshipParser.parse(selectorString)

    CssSelector selector = new InsideCssSelector(elementSelectorParser.parse(parts.head()[1]))

    parts.tail().each {
      String operator = it[0]
      switch (operator) {
        case '>':
          selector = new ParentCssSelector(selector, createChildCssSelector(it[1]))
          break
        case '+':
          selector = new StalkerCssSelector(selector, createChildCssSelector(it[1]))
          break
        case '~':
          selector = new PrecededBySiblingCssSelector(selector, createChildCssSelector(it[1]))
          break
      }
    }

    selector
  }

  protected ChildCssSelector createChildCssSelector(childSelectionPattern) {
    List<CssSelector> cssSelectors = elementSelectorParser.parse(childSelectionPattern)
    CssSelector exactMatchSelector = cssSelectors.head()
    InsideCssSelector insideCssSelector = cssSelectors.size() > 1 ? new InsideCssSelector(cssSelectors.tail()) : null
    new ChildCssSelector(exactMatchSelector, insideCssSelector)
  }
}