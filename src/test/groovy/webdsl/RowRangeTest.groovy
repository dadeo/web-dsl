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

import org.junit.Test
import webdsl.support.RowRange


class RowRangeTest {

  @Test
  void test_contains() {
    RowRange rowRange = new RowRange(2..4, 100)
    assert !rowRange.contains(0)
    assert !rowRange.contains(1)
    assert rowRange.contains(2)
    assert rowRange.contains(3)
    assert rowRange.contains(4)
    assert !rowRange.contains(5)
    assert !rowRange.contains(100)
  }

  @Test
  void test_contains_reversed() {
    RowRange rowRange = new RowRange(4..2, 100)
    assert !rowRange.contains(0)
    assert !rowRange.contains(1)
    assert rowRange.contains(2)
    assert rowRange.contains(3)
    assert rowRange.contains(4)
    assert !rowRange.contains(5)
    assert !rowRange.contains(100)
  }

  @Test
  void test_contains_upper_boundary_index_from_end() {
    RowRange rowRange = new RowRange(2..-1, 100)
    assert !rowRange.contains(0)
    assert !rowRange.contains(1)
    assert rowRange.contains(2)
    assert rowRange.contains(50)
    assert rowRange.contains(99)
    assert !rowRange.contains(100)
  }

  @Test
  void test_contains_lower_boundary_index_from_end() {
    RowRange rowRange = new RowRange(-1..2, 100)
    assert !rowRange.contains(0)
    assert !rowRange.contains(1)
    assert rowRange.contains(2)
    assert rowRange.contains(50)
    assert rowRange.contains(99)
    assert !rowRange.contains(100)
  }

  @Test
  void test_contains_both_boundaries_forward_indexes_from_end() {
    RowRange rowRange = new RowRange(-8..-2, 100)
    assert !rowRange.contains(0)
    assert !rowRange.contains(91)
    assert rowRange.contains(92)
    assert rowRange.contains(98)
    assert !rowRange.contains(99)
    assert !rowRange.contains(100)
  }

  @Test
  void test_contains_both_boundaries_reverse_indexes_from_end() {
    RowRange rowRange = new RowRange(-2..-8, 100)
    assert !rowRange.contains(0)
    assert !rowRange.contains(91)
    assert rowRange.contains(92)
    assert rowRange.contains(98)
    assert !rowRange.contains(99)
    assert !rowRange.contains(100)
  }

  @Test
  void test_isReversed_forward() {
    assert !new RowRange(2..4, 100).isReverse()
  }

  @Test
  void test_isReversed_reverse() {
    assert new RowRange(4..2, 100).isReverse()
  }

  @Test
  void test_isReversed_forward_unbounded() {
    assert !new RowRange(2..-1, 100).isReverse()
  }

  @Test
  void test_isReversed_reverse_unbounded() {
    assert new RowRange(-1..2, 100).isReverse()
  }

  @Test
  void test_isReversed_reverse_true_negative_from_and_to() {
    assert new RowRange(-1..-3, 100).isReverse()
    assert new RowRange(-2..-3, 100).isReverse()
  }

  @Test
  void test_isReversed_reverse_false_negative_from_and_to() {
    assert !new RowRange(-3..-1, 100).isReverse()
    assert !new RowRange(-3..-2, 100).isReverse()
  }

}