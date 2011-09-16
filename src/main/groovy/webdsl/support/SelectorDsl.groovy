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


class SelectorDsl {
  private dsl
  private factory
  private selected = []

  SelectorDsl(dsl, factory) {
    this.dsl = dsl
    this.factory = factory
  }

  SelectorDsl(dsl, factory, selected) {
    this.dsl = dsl
    this.factory = factory
    this.selected = selected
  }

  def leftShift(item) { selected << factory.create(dsl, item) }

  def size() { selected.size() }

  def getAt(index) { selected[index] }

  def propertyMissing(String name) {
    def result = selected[name]
    if (result && result[0] instanceof SelectorDsl) {
      result = new SelectorDsl(dsl, factory, extractResultsFrom(result))
    }
    result
  }

  void each(Closure closure) {
    executeClosureWith(selected.&each, closure)
  }

  List collect(Closure closure) {
    executeClosureWith(selected.&collect, closure)
  }

  def find(Closure closure) {
    executeClosureWith(selected.&find, closure)
  }

  List findAll(Closure closure) {
    executeClosureWith(selected.&findAll, closure)
  }

  List findResult(Closure closure) {
    executeClosureWith(selected.&collect, closure).findAll { it }
  }

  private executeClosureWith(method, closure) {
    Closure c = closure.clone()
    c.resolveStrategy = Closure.DELEGATE_FIRST
    method {
      c.delegate = it
      c(it)
    }
  }

  private extractResultsFrom(List<SelectorDsl> selectors) {
    selectors.collect { it.selected }.flatten()
  }
}