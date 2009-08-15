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

class DslHelper {
  def factory = new DslFactory()

  def addGetterMethodsForAll(elements, List attributes, target) {
    def emc = new ExpandoMetaClass(target.getClass(), false, true)
    elements.each {element ->
      def dsl = factory.create(target, element)
      addGetterMethodsFor(attributes, {-> dsl }, element, emc)
    }
    emc.initialize()
    target.metaClass = emc
  }

  def addGetterMethodsFor(attributes, method, element, ExpandoMetaClass emc) {
    attributes.each {attribute ->
      def methodName = element.getAttribute(attribute)
      if (methodName) {
        emc[toGetter(methodName)] = method
      }
    }
  }

  static def toGetter(name) {
    def start = name[0]
    if(start =~ /[A-Z]/) {
     start = '$' + start
    } else {
      start = start.toUpperCase()
    }
    "get" + start + (name.size() > 1 ? name[1..-1] : "")
  }

  static def fromGetter(name) {
    name -= 'get'
    def start
    if(name.startsWith('$')) {
      name -= '$'
      start = name[0]
    } else {
      start = name[0].toLowerCase()
    }
    start + (name.size() > 1 ? name[1..-1] : "")
  }
}