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


class StartsWithHyphenatedMatcherTest {

  @Test
  void test_matches_equal() {
    assert new StartsWithHyphenatedMatcher('foo').matches('foo')
  }

  @Test
  void test_matches_starts_with_value_followed_by_hyphen() {
    assert new StartsWithHyphenatedMatcher('foo').matches('foo-')
  }

  @Test
  void test_matches_starts_with_value_followed_by_hyphenated_text() {
    assert new StartsWithHyphenatedMatcher('foo').matches('foo-bar')
  }

  @Test
  void test_matches_starts_with_value() {
    assert !new StartsWithHyphenatedMatcher('foo').matches('foobar')
  }

  @Test
  void test_matches_ends_with_value() {
    assert !new StartsWithHyphenatedMatcher('foo').matches('barfoo')
  }

  @Test
  void test_matches_contains_value() {
    assert !new StartsWithHyphenatedMatcher('foo').matches('barfoobaz')
  }

  @Test
  void test_matches_too_long() {
    assert !new StartsWithHyphenatedMatcher('foo').matches('fo')
  }

  @Test
  void test_matches_check_value_null() {
    assert !new StartsWithHyphenatedMatcher('foo').matches(null)
  }

  @Test
  void test_matches_matcher_value_null() {
    assert !new StartsWithHyphenatedMatcher(null).matches('foo')
  }

  @Test
  void test_toString() {
    assert new StartsWithHyphenatedMatcher('foo').toString() == "STARTS_WITH_HYPHENATED(foo)"
  }
}