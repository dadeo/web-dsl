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
import webdsl.support.matchers.AlwaysMatcher
import webdsl.support.matchers.ContainsMatcher
import webdsl.support.matchers.EndsWithMatcher
import webdsl.support.matchers.EqualsMatcher
import webdsl.support.matchers.StartsWithMatcher

import static webdsl.support.CssSelectorParser.*

class CssSelectorParserTest {
  private CssSelectorParser parser

  @Before
  void setUp() {
    parser = new CssSelectorParser()
  }

  @Test
  void test_EQ() {
    assert EQ('value') == new EqualsMatcher('value')
  }

  @Test
  void test_ALWAYS() {
    assert ALWAYS() == new AlwaysMatcher()
  }

  @Test
  void test_STARTS_WITH() {
    assert STARTS_WITH('foo') == new StartsWithMatcher('foo')
  }

  @Test
  void test_ENDS_WITH() {
    assert ENDS_WITH('foo') == new EndsWithMatcher('foo')
  }

  @Test
  void test_CONTAINS() {
    assert CONTAINS('foo') == new ContainsMatcher('foo')
  }

  @Test
  void test_parse_class_only() {
    assert parser.parse('.selected') == [new CssSelector(attributes: [class: EQ('selected')])]
  }

  @Test
  void test_parse_id_only() {
    assert parser.parse('#myId') == [new CssSelector(id: 'myId')]
  }

  @Test
  void test_parse_id_with_class() {
    assert parser.parse('#myId.selected') == [new CssSelector(id: 'myId', attributes: [class: EQ('selected')])]
  }

  @Test
  void test_parse_tagName_only() {
    assert parser.parse('div') == [new CssSelector(tagName: 'div')]
  }

  @Test
  void test_parse_tagName_with_class() {
    assert parser.parse('div.selected') == [new CssSelector(tagName: 'div', attributes: [class: EQ('selected')])]
  }

  @Test
  void test_parse_id_with_tagName_and_class() {
    assert parser.parse('div#myId.selected') == [new CssSelector(id: 'myId', tagName: 'div', attributes: [class: EQ('selected')])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_then_tagName() {
    assert parser.parse('p div') == [new CssSelector(tagName: 'p'), new CssSelector(tagName: 'div')]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_then_class() {
    assert parser.parse('p .selected') == [new CssSelector(tagName: 'p'), new CssSelector(attributes: [class: EQ('selected')])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_and_class_then_tagName_and_class() {
    assert parser.parse('p.selected div.name') == [
        new CssSelector(tagName: 'p', attributes: [class: EQ('selected')]),
        new CssSelector(tagName: 'div', attributes: [class: EQ('name')])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_and_id_and_class_then_tagName_and_class_then_tagName_and_class() {
    assert parser.parse('li#owners.error p.selected div.name') == [
        new CssSelector(id: 'owners', tagName: 'li', attributes: [class: EQ('error')]),
        new CssSelector(tagName: 'p', attributes: [class: EQ('selected')]),
        new CssSelector(tagName: 'div', attributes: [class: EQ('name')])]
  }

  @Test
  void test_parse_attribute_name() {
    assert parser.parse('[foo]') == [new CssSelector(attributes: [foo: ALWAYS()])]
  }

  @Test
  void test_parse_tag_with_attribute() {
    assert parser.parse('li[foo]') == [new CssSelector(tagName: 'li', attributes: [foo: ALWAYS()])]
  }

  @Test
  void test_parse_id_with_attribute() {
    assert parser.parse('#bar[foo]') == [new CssSelector(id: 'bar', attributes: [foo: ALWAYS()])]
  }

  @Test
  void test_parse_class_with_attribute() {
    assert parser.parse('.bar[foo]') == [new CssSelector(attributes: [class: EQ('bar'), foo: ALWAYS()])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute() {
    assert parser.parse('baz#bam.bar[foo]') == [new CssSelector(tagName:'baz', id:'bam', attributes: [class: EQ('bar'), foo: ALWAYS()])]
  }

  @Test
  void test_parse_attribute_equals_value() {
    assert parser.parse('[foo="bar"]') == [new CssSelector(attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_equals_value() {
    assert parser.parse('li[foo="bar"]') == [new CssSelector(tagName: 'li', attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_equals_value() {
    assert parser.parse('#bar[foo="bar"]') == [new CssSelector(id: 'bar', attributes: [foo:  EQ('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_equals_value() {
    assert parser.parse('.bar[foo="bar"]') == [new CssSelector(attributes: [class: EQ('bar'), foo:  EQ('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_equals_value() {
    assert parser.parse('baz#bam.bar[foo="bar"]') == [new CssSelector(tagName:'baz', id:'bam', attributes: [class: EQ('bar'), foo:  EQ('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_equals_value_can_use_half_quotes() {
    assert parser.parse("li[foo='bar']") == [new CssSelector(tagName: 'li', attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_start_with_value() {
    assert parser.parse('li[foo^="bar"]') == [new CssSelector(tagName: 'li', attributes: [foo: STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_starts_with_value() {
    assert parser.parse('#bar[foo^="bar"]') == [new CssSelector(id: 'bar', attributes: [foo:  STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_starts_with_value() {
    assert parser.parse('.bar[foo^="bar"]') == [new CssSelector(attributes: [class: EQ('bar'), foo:  STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_starts_with_value() {
    assert parser.parse('baz#bam.bar[foo^="baz"]') == [new CssSelector(tagName:'baz', id:'bam', attributes: [class: EQ('bar'), foo:  STARTS_WITH('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_starts_with_value_can_use_half_quotes() {
    assert parser.parse("li[foo^='bar']") == [new CssSelector(tagName: 'li', attributes: [foo: STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_ends_with_value() {
    assert parser.parse('li[foo$="bar"]') == [new CssSelector(tagName: 'li', attributes: [foo: ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_ends_with_value() {
    assert parser.parse('#bar[foo$="bar"]') == [new CssSelector(id: 'bar', attributes: [foo:  ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_ends_with_value() {
    assert parser.parse('.bar[foo$="bar"]') == [new CssSelector(attributes: [class: EQ('bar'), foo:  ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_ends_with_value() {
    assert parser.parse('baz#bam.bar[foo$="baz"]') == [new CssSelector(tagName:'baz', id:'bam', attributes: [class: EQ('bar'), foo:  ENDS_WITH('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_ends_with_value_can_use_half_quotes() {
    assert parser.parse("li[foo\$='bar']") == [new CssSelector(tagName: 'li', attributes: [foo: ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_contains_value() {
    assert parser.parse('li[foo*="bar"]') == [new CssSelector(tagName: 'li', attributes: [foo: CONTAINS('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_contains_value() {
    assert parser.parse('#bar[foo*="bar"]') == [new CssSelector(id: 'bar', attributes: [foo:  CONTAINS('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_contains_value() {
    assert parser.parse('.bar[foo*="bar"]') == [new CssSelector(attributes: [class: EQ('bar'), foo:  CONTAINS('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_contains_value() {
    assert parser.parse('baz#bam.bar[foo*="baz"]') == [new CssSelector(tagName:'baz', id:'bam', attributes: [class: EQ('bar'), foo:  CONTAINS('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_contains_can_use_half_quotes() {
    assert parser.parse("li[foo*='bar']") == [new CssSelector(tagName: 'li', attributes: [foo: CONTAINS('bar')])]
  }


}