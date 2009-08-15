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


class DslHelperTest extends GroovyTestCase {
  def helper

  void setUp() {
    helper = new DslHelper()
    helper.factory = [create: {a, b-> b._value}]
  }
  
  void test_addGetterMethodsFor_multiple_elements() {
    def targetObject = new Object()

    helper.addGetterMethodsForAll([createElement(1, 123), createElement(2,'abc')], ['id', 'name'], targetObject)

    assertEquals 123, targetObject.element1
    assertEquals 123, targetObject.namedElement1

    assertEquals 'abc', targetObject.element2
    assertEquals 'abc', targetObject.namedElement2
  }

  void test_addGetterMethodsFor_single_letter_id_and_name() {
    def targetObject = new Object()

    helper.addGetterMethodsForAll([createElement("a", "b", 123)], ['id', 'name'], targetObject)

    assertEquals 123, targetObject.a
    assertEquals 123, targetObject.b
  }

  void test_addGetterMethodsFor_only_modifies_target_objects_class() {
    test_addGetterMethodsFor_multiple_elements()

    shouldFail(MissingPropertyException) {
      new Object().element1
    }
  }

  void test_toGetter() {
    assertEquals "getName", DslHelper.toGetter("name")
    assertEquals "getN", DslHelper.toGetter("n")
    assertEquals 'get$Name', DslHelper.toGetter("Name")
    assertEquals 'get$N', DslHelper.toGetter("N")
  }

  void test_fromGetter() {
    assertEquals "name", DslHelper.fromGetter("getName")
    assertEquals "n", DslHelper.fromGetter("getN")
    assertEquals "Name", DslHelper.fromGetter('get$Name')
    assertEquals "N", DslHelper.fromGetter('get$N')
  }

  def createElement(int number, value) {
    createElement "element$number", "namedElement$number", value
  }

  def createElement(String id, String name, value) {
    def elementObject = [_id: id, _name: name, _value:value]
    elementObject.getAttribute = {it == 'id' ? elementObject._id : elementObject._name }
    elementObject
  }
}