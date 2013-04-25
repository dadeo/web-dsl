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
package webdsl.support.matchers

import org.junit.Test


class EqualsMatcherTest {

  @Test
  void test_equals_same_value() {
    assert new EqualsMatcher('foo').matches('foo')
  }

  @Test
  void test_equals_different_values() {
    assert !new EqualsMatcher('foo').matches('bar')
  }

  @Test
  void test_equals_both_null() {
    assert new EqualsMatcher(null).matches(null)
  }

  @Test
  void test_equals_matcher_value_null_other_value_present() {
    assert !new EqualsMatcher(null).matches('foo')
  }

  @Test
  void test_equals_matcher_value_present_other_value_null() {
    assert !new EqualsMatcher('foo').matches(null)
  }
}