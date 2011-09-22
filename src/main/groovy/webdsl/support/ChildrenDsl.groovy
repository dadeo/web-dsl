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

import webdsl.WebDsl
import com.gargoylesoftware.htmlunit.html.HtmlElement


class ChildrenDsl {

  def children(WebDsl dsl, start) {
    start.allHtmlChildElements.collect { dsl.factory.create(dsl, it) }
  }

  def children(WebDsl dsl, start, options) {
    def result = []
    start.allHtmlChildElements.each {HtmlElement child ->
      def types = []
      if (options?.type) types.addAll(options.type)
      if (options?.types) types.addAll(options.types)

      if (types && !isInTypes(types, child)) return
      result << dsl.factory.create(dsl, child)
    }
    result
  }

  private boolean isInTypes(types, element) {
    types.find { isType it, element }
  }

  private boolean isType(type, HtmlElement element) {
    element.tagName == type
  }

}