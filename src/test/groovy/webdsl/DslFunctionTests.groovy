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


class DslFunctionTests extends AbstractTests {
  def web

  void setUp() {
    web = new WebDsl()
  }
  
  void test_camel_string() {
    assertEquals "firstName", web.camel("First Name")
    assertEquals "firstName", web.camel("first name")
    assertEquals "a", web.camel("A")
    assertEquals "a", web.camel("a")
    assertEquals "ssn", web.camel("SSN")
    assertEquals "ipAddress", web.camel("IP Address")
    assertEquals "ipAddress", web.camel("IP ADDRESS")
    assertEquals "externalIpAddress", web.camel("EXTERNAL IP ADDRESS")
    assertEquals "aBC", web.camel("A B C")
  }

  void test_camel_map() {
    def original = ["first name": "john", "last name": "doe"]
    def actual = web.camel(original)
    assertEquals(["firstName": "john", "lastName": "doe"], actual)
  }

  void test_camel_map_extended() {
    def original = ["first name": "john", "last name": "doe"]
    web.do {
      def actual = original.camel()
      assertEquals(["firstName": "john", "lastName": "doe"], actual)
    }
  }

  void test_map() {
    def original = ["first name": "john", "last name": "doe"]
    web.do {
      def actual = original.map("first name": "first", "last name": "last")
      assertEquals(["first": "john", "last": "doe"], actual)
    }
  }

  void test_map_subset() {
    def original = ["first name": "john", "last name": "doe"]
    web.do {
      def actual = original.map("first name": "first")
      assertEquals(["first": "john", "last name": "doe"], actual)
    }
  }
}