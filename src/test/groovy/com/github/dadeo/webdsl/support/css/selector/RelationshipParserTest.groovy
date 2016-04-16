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

import org.junit.Before
import org.junit.Test


class RelationshipParserTest {
  private RelationshipParser parser

  @Before
  void setUp() {
    parser = new RelationshipParser()
  }

  @Test
  void test_parse_child() {
    assert parser.parse('aa>bb') == [[null, 'aa'], ['>', 'bb']]
  }

  @Test
  void test_parse_stalker() {
    assert parser.parse('aa+bb') == [[null, 'aa'], ['+', 'bb']]
  }

  @Test
  void test_parse_precededBySibling() {
    assert parser.parse('aa~bb') == [[null, 'aa'], ['~', 'bb']]
  }

  @Test
  void test_parse_multiple() {
    assert parser.parse('aa~bb+cc>dd') == [[null, 'aa'], ['~', 'bb'], ['+', 'cc'], ['>', 'dd']]
  }

  @Test
  void test_parse_inList_attribute() {
    assert parser.parse('[foo~="bar"]') == [[null, '[foo~="bar"]']]
  }

  @Test
  void test_parse_relationship_operators_with_inList_attribute() {
    assert parser.parse('a>[foo~="bar"]+b') == [[null, 'a'], ['>', '[foo~="bar"]'], ['+', 'b']]
  }
}