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

import groovy.transform.Immutable
import webdsl.support.matchers.ValueMatcher

@Immutable
class ElementCssSelector implements CssSelector {
  String id
  String tagName
  Map<String, ValueMatcher> attributes = [:]

  List select(candidate) {
    def predicates = []

    if (id)
      predicates << { it.id == id }

    if (tagName)
      predicates << { it.tagName == tagName }

    attributes.each { attributeName, valueMatcher ->
      predicates << {
        it.hasAttribute(attributeName) && valueMatcher.matches(it.getAttribute(attributeName))
      }
    }

    predicates.every { it(candidate) } ? [candidate] : []
  }
}
