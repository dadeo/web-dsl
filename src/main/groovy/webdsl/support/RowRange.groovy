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


class RowRange {
  private Range<Integer> range
  private Range<Integer> enhancedRange
  private int size

  RowRange(Range<Integer> range, int size) {
    this.range = range
    int to = range.to < 0 ? size + range.to : range.to
    int from = range.from < 0 ? size + range.from : range.from

    this.enhancedRange = new IntRange(*[from, to].sort())
    this.size = size
  }

  boolean contains(int value) {
    enhancedRange.containsWithinBounds(value) && value < size
  }

  boolean isReverse() {
    range.isReverse() ^ (range.from == -1)
  }
}