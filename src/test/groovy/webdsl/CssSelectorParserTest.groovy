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

import org.junit.Before
import org.junit.Test
import webdsl.support.CssSelector
import webdsl.support.CssSelectorParser

class CssSelectorParserTest {
  private CssSelectorParser parser

  @Before
  void setUp() {
    parser = new CssSelectorParser()
  }

  @Test
  void test_parse_class_only() {
    assert parser.parse('.selected') == [new CssSelector(attributes: [class: 'selected'])]
  }

  @Test
  void test_parse_id_only() {
    assert parser.parse('#myId') == [new CssSelector(id: 'myId')]
  }

  @Test
  void test_parse_id_with_class() {
    assert parser.parse('#myId.selected') == [new CssSelector(id: 'myId', attributes: [class: 'selected'])]
  }

  @Test
  void test_parse_tagName_only() {
    assert parser.parse('div') == [new CssSelector(tagName: 'div')]
  }

  @Test
  void test_parse_tagName_with_class() {
    assert parser.parse('div.selected') == [new CssSelector(tagName: 'div', attributes: [class: 'selected'])]
  }

  @Test
  void test_parse_id_with_tagName_and_class() {
    assert parser.parse('div#myId.selected') == [new CssSelector(id: 'myId', tagName: 'div', attributes: [class: 'selected'])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_then_tagName() {
    assert parser.parse('p div') == [new CssSelector(tagName: 'p'), new CssSelector(tagName: 'div')]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_then_class() {
    assert parser.parse('p .selected') == [new CssSelector(tagName: 'p'), new CssSelector(attributes: [class: 'selected'])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_and_class_then_tagName_and_class() {
    assert parser.parse('p.selected div.name') == [
        new CssSelector(tagName: 'p', attributes: [class: 'selected']),
        new CssSelector(tagName: 'div', attributes: [class: 'name'])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_and_id_and_class_then_tagName_and_class_then_tagName_and_class() {
    assert parser.parse('li#owners.error p.selected div.name') == [
        new CssSelector(id: 'owners', tagName: 'li', attributes: [class: 'error']),
        new CssSelector(tagName: 'p', attributes: [class: 'selected']),
        new CssSelector(tagName: 'div', attributes: [class: 'name'])]
  }

}