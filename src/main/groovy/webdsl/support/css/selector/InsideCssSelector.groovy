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

@groovy.transform.Immutable
class InsideCssSelector implements CssSelector {
  List<CssSelector> cssSelectors

  @Override
  List select(candidate) {
    def applySelector = { cssSelector, target ->
      def insideSelector = { element -> cssSelector.select(element) }

      target.getHtmlElementDescendants()
            .toList()
            .collectMany(insideSelector)
    }

    def applyAllSelectors = { selectors, target ->
      def dslElements = applySelector selectors.head(), target
      if (selectors.tail())
        dslElements.collectMany curry(selectors.tail())
      else
        dslElements
    }

    applyAllSelectors cssSelectors, candidate
  }
}