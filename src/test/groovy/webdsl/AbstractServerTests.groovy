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


abstract class AbstractTests extends GroovyTestCase {

  def assertEquals(Map expected, Map actual) {
    def message = {"\n\nexpected list:${expected}\nactual list  :${actual}\nkey          :${it}\n"}
    assertEquals message(), expected.size(), actual ? actual.size() : 0
    expected.each {k, v ->
      assertEquals message(k), v?.toString(), actual[k]?.toString()
    }
  }

  def assertEquals(List expected, List actual) {
    def message = {"\n\nexpected list:${expected}\nactual list  :${actual}\n"}
    assertEquals message(), expected.size(), actual ? actual.size() : 0
    expected.size().times {
      assertEquals message(), expected[it].toString(), actual[it].toString()
    }
  }

}