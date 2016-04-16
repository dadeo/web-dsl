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
package com.github.dadeo.webdsl.support.matchers

import org.junit.Test


class ListContainsMatcherTest {

  @Test
  void test_matches_equal() {
    assert new ListContainsMatcher('foo').matches('foo')
  }

  @Test
  void test_matches_first_value_in_list() {
    assert new ListContainsMatcher('foo').matches('foo bar baz')
  }

  @Test
  void test_matches_middle_value_in_list() {
    assert new ListContainsMatcher('foo').matches('bar foo baz')
  }

  @Test
  void test_matches_last_value_in_list() {
    assert new ListContainsMatcher('foo').matches('bar baz foo')
  }

  @Test
  void test_matches_first_value_in_list_with_whitespace() {
    assert new ListContainsMatcher('foo').matches('    foo   bar  baz  ')
  }

  @Test
  void test_matches_middle_value_in_list_with_whitespace() {
    assert new ListContainsMatcher('foo').matches(' bar   foo   baz   ')
  }

  @Test
  void test_matches_last_value_in_list_with_whitespace() {
    assert new ListContainsMatcher('foo').matches('   bar   baz   foo  ')
  }

  @Test
  void test_matches_against_word_that_starts_with_value() {
    assert !new ListContainsMatcher('foo').matches('foobar')
  }

  @Test
  void test_matches_against_word_that_ends_with_value() {
    assert !new ListContainsMatcher('foo').matches('barfoo')
  }

  @Test
  void test_matches_against_word_that_contains_with_value() {
    assert !new ListContainsMatcher('foo').matches('bazfoobar')
  }

  @Test
  void test_matches_check_value_null() {
    assert !new ListContainsMatcher('foo').matches(null)
  }

  @Test
  void test_matches_matcher_value_null() {
    assert !new ListContainsMatcher(null).matches('foo')
  }

  @Test
  void test_toString() {
    assert new ListContainsMatcher('foo').toString() == "LIST_CONTAINS(foo)"
  }
}