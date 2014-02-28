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
package webdsl.css.selector

import org.junit.Before
import org.junit.Test
import webdsl.support.css.selector.ElementCssSelector
import webdsl.support.css.selector.ElementCssSelectorParser
import webdsl.support.matchers.*

import static webdsl.support.css.selector.ElementCssSelectorParser.*

class ElementCssSelectorParserTest {
  private ElementCssSelectorParser parser

  @Before
  void setUp() {
    parser = new ElementCssSelectorParser()
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
  void test_LIST_CONTAINS() {
    assert LIST_CONTAINS('foo') == new ListContainsMatcher('foo')
  }

  @Test
  void test_STARTS_WITH_HYPHENATED() {
    assert STARTS_WITH_HYPHENATED('foo') == new StartsWithHyphenatedMatcher('foo')
  }

  @Test
  void test_parse_class_only() {
    assert parser.parse('.selected') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('selected')])]
  }

  @Test
  void test_parse_id_only() {
    assert parser.parse('#myId') == [new ElementCssSelector(id: 'myId')]
  }

  @Test
  void test_parse_id_with_class() {
    assert parser.parse('#myId.selected') == [new ElementCssSelector(id: 'myId', attributes: [class: LIST_CONTAINS('selected')])]
  }

  @Test
  void test_parse_tagName_only() {
    assert parser.parse('div') == [new ElementCssSelector(tagName: 'div')]
  }

  @Test
  void test_parse_tagName_with_class() {
    assert parser.parse('div.selected') == [new ElementCssSelector(tagName: 'div', attributes: [class: LIST_CONTAINS('selected')])]
  }

  @Test
  void test_parse_id_with_tagName_and_class() {
    assert parser.parse('div#myId.selected') == [new ElementCssSelector(id: 'myId', tagName: 'div', attributes: [class: LIST_CONTAINS('selected')])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_then_tagName() {
    assert parser.parse('p div') == [new ElementCssSelector(tagName: 'p'), new ElementCssSelector(tagName: 'div')]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_then_class() {
    assert parser.parse('p .selected') == [new ElementCssSelector(tagName: 'p'), new ElementCssSelector(attributes: [class: LIST_CONTAINS('selected')])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_and_class_then_tagName_and_class() {
    assert parser.parse('p.selected div.name') == [
        new ElementCssSelector(tagName: 'p', attributes: [class: LIST_CONTAINS('selected')]),
        new ElementCssSelector(tagName: 'div', attributes: [class: LIST_CONTAINS('name')])]
  }

  @Test
  void test_parse_multiple_selectors_by_tagName_and_id_and_class_then_tagName_and_class_then_tagName_and_class() {
    assert parser.parse('li#owners.error p.selected div.name') == [
        new ElementCssSelector(id: 'owners', tagName: 'li', attributes: [class: LIST_CONTAINS('error')]),
        new ElementCssSelector(tagName: 'p', attributes: [class: LIST_CONTAINS('selected')]),
        new ElementCssSelector(tagName: 'div', attributes: [class: LIST_CONTAINS('name')])]
  }

  @Test
  void test_parse_attribute_name() {
    assert parser.parse('[foo]') == [new ElementCssSelector(attributes: [foo: ALWAYS()])]
  }

  @Test
  void test_parse_tag_with_attribute() {
    assert parser.parse('li[foo]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: ALWAYS()])]
  }

  @Test
  void test_parse_id_with_attribute() {
    assert parser.parse('#bar[foo]') == [new ElementCssSelector(id: 'bar', attributes: [foo: ALWAYS()])]
  }

  @Test
  void test_parse_class_with_attribute() {
    assert parser.parse('.bar[foo]') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('bar'), foo: ALWAYS()])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute() {
    assert parser.parse('baz#bam.bar[foo]') == [new ElementCssSelector(tagName: 'baz', id: 'bam', attributes: [class: LIST_CONTAINS('bar'), foo: ALWAYS()])]
  }

  @Test
  void test_parse_attribute_equals_value() {
    assert parser.parse('[foo="bar"]') == [new ElementCssSelector(attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_attribute_equals_value_can_have_multiple_words() {
    assert parser.parse('[foo="foo bar baz"]') == [new ElementCssSelector(attributes: [foo: EQ('foo bar baz')])]
  }

  @Test
  void test_parse_attribute_equals_value_can_have_multiple_words_even_when_no_quotes() {
    assert parser.parse('[foo=foo bar baz]') == [new ElementCssSelector(attributes: [foo: EQ('foo bar baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_equals_value() {
    assert parser.parse('li[foo="bar"]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_equals_value() {
    assert parser.parse('#bar[foo="bar"]') == [new ElementCssSelector(id: 'bar', attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_equals_value() {
    assert parser.parse('.bar[foo="bar"]') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('bar'), foo: EQ('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_equals_value() {
    assert parser.parse('baz#bam.bar[foo="bar"]') == [new ElementCssSelector(tagName: 'baz', id: 'bam', attributes: [class: LIST_CONTAINS('bar'), foo: EQ('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_equals_value_can_use_no_quotes() {
    assert parser.parse("li[foo=bar]") == [new ElementCssSelector(tagName: 'li', attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_equals_value_can_use_half_quotes() {
    assert parser.parse("li[foo='bar']") == [new ElementCssSelector(tagName: 'li', attributes: [foo: EQ('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_start_with_value() {
    assert parser.parse('li[foo^="bar"]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_starts_with_value() {
    assert parser.parse('#bar[foo^="bar"]') == [new ElementCssSelector(id: 'bar', attributes: [foo: STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_starts_with_value() {
    assert parser.parse('.bar[foo^="bar"]') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('bar'), foo: STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_starts_with_value() {
    assert parser.parse('baz#bam.bar[foo^="baz"]') == [new ElementCssSelector(tagName: 'baz', id: 'bam', attributes: [class: LIST_CONTAINS('bar'), foo: STARTS_WITH('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_starts_with_value_can_use_no_quotes() {
    assert parser.parse("li[foo^=bar]") == [new ElementCssSelector(tagName: 'li', attributes: [foo: STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_starts_with_value_can_use_half_quotes() {
    assert parser.parse("li[foo^='bar']") == [new ElementCssSelector(tagName: 'li', attributes: [foo: STARTS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_ends_with_value() {
    assert parser.parse('li[foo$="bar"]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_ends_with_value() {
    assert parser.parse('#bar[foo$="bar"]') == [new ElementCssSelector(id: 'bar', attributes: [foo: ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_ends_with_value() {
    assert parser.parse('.bar[foo$="bar"]') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('bar'), foo: ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_ends_with_value() {
    assert parser.parse('baz#bam.bar[foo$="baz"]') == [new ElementCssSelector(tagName: 'baz', id: 'bam', attributes: [class: LIST_CONTAINS('bar'), foo: ENDS_WITH('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_ends_with_value_can_use_no_quotes() {
    assert parser.parse('li[foo$=bar]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_ends_with_value_can_use_half_quotes() {
    assert parser.parse("li[foo\$='bar']") == [new ElementCssSelector(tagName: 'li', attributes: [foo: ENDS_WITH('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_contains_value() {
    assert parser.parse('li[foo*="bar"]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: CONTAINS('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_contains_value() {
    assert parser.parse('#bar[foo*="bar"]') == [new ElementCssSelector(id: 'bar', attributes: [foo: CONTAINS('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_contains_value() {
    assert parser.parse('.bar[foo*="bar"]') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('bar'), foo: CONTAINS('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_contains_value() {
    assert parser.parse('baz#bam.bar[foo*="baz"]') == [new ElementCssSelector(tagName: 'baz', id: 'bam', attributes: [class: LIST_CONTAINS('bar'), foo: CONTAINS('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_contains_can_use_no_quotes() {
    assert parser.parse("li[foo*=bar]") == [new ElementCssSelector(tagName: 'li', attributes: [foo: CONTAINS('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_contains_can_use_half_quotes() {
    assert parser.parse("li[foo*='bar']") == [new ElementCssSelector(tagName: 'li', attributes: [foo: CONTAINS('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_list_contains_value() {
    assert parser.parse('li[foo~="bar"]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: LIST_CONTAINS('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_list_contains_value() {
    assert parser.parse('#bar[foo~="bar"]') == [new ElementCssSelector(id: 'bar', attributes: [foo: LIST_CONTAINS('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_list_contains_value() {
    assert parser.parse('.bar[foo~="bar"]') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('bar'), foo: LIST_CONTAINS('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_list_contains_value() {
    assert parser.parse('baz#bam.bar[foo~="baz"]') == [new ElementCssSelector(tagName: 'baz', id: 'bam', attributes: [class: LIST_CONTAINS('bar'), foo: LIST_CONTAINS('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_list_contains_can_use_no_quotes() {
    assert parser.parse("li[foo~=bar]") == [new ElementCssSelector(tagName: 'li', attributes: [foo: LIST_CONTAINS('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_list_contains_can_use_half_quotes() {
    assert parser.parse("li[foo~='bar']") == [new ElementCssSelector(tagName: 'li', attributes: [foo: LIST_CONTAINS('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_starts_with_hyphenated_value() {
    assert parser.parse('li[foo|="bar"]') == [new ElementCssSelector(tagName: 'li', attributes: [foo: STARTS_WITH_HYPHENATED('bar')])]
  }

  @Test
  void test_parse_id_with_attribute_starts_with_hyphenated_value() {
    assert parser.parse('#bar[foo|="bar"]') == [new ElementCssSelector(id: 'bar', attributes: [foo: STARTS_WITH_HYPHENATED('bar')])]
  }

  @Test
  void test_parse_class_with_attribute_starts_with_hyphenated_value() {
    assert parser.parse('.bar[foo|="bar"]') == [new ElementCssSelector(attributes: [class: LIST_CONTAINS('bar'), foo: STARTS_WITH_HYPHENATED('bar')])]
  }

  @Test
  void test_parse_tag_id_class_with_attribute_starts_with_hyphenated_value() {
    assert parser.parse('baz#bam.bar[foo|="baz"]') == [new ElementCssSelector(tagName: 'baz', id: 'bam', attributes: [class: LIST_CONTAINS('bar'), foo: STARTS_WITH_HYPHENATED('baz')])]
  }

  @Test
  void test_parse_tag_with_attribute_starts_with_hyphenated_can_use_no_quotes() {
    assert parser.parse("li[foo|=bar]") == [new ElementCssSelector(tagName: 'li', attributes: [foo: STARTS_WITH_HYPHENATED('bar')])]
  }

  @Test
  void test_parse_tag_with_attribute_starts_with_hyphenated_can_use_half_quotes() {
    assert parser.parse("li[foo|='bar']") == [new ElementCssSelector(tagName: 'li', attributes: [foo: STARTS_WITH_HYPHENATED('bar')])]
  }


}