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

import static webdsl.Orientation.VERTICAL

@Mixin(NonServerMixin)
class TableDslTest {

  @Test
  void test_table_as_horizontal_objects__values_can_have_spaces() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('text') }
        tr { td('pinky'); td('this is pinky') }
        tr { td('winky'); td('this is winky') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "pinky", text: "this is pinky"],
          [firstName: "winky", text: "this is winky"],
      ]
      assert table3.as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects__values_can_have_spaces() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('text'); td('this is pinky'); td('this is winky')}
      }
    }

    webdsl {
      def expected = [
          [firstName: "pinky", lastName: "jones1", text: 'this is pinky'],
          [firstName: "winky", lastName: "jones2", text: 'this is winky'],
      ]
      assert $('#table1').as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "pinky", lastName: "jones1"],
          [firstName: "winky", lastName: "jones2"],
          [firstName: "dinky", lastName: "jones3"],
          [firstName: "linky", lastName: "jones4"],
          [firstName: "stinky", lastName: "jones5"],
      ]
      assert table3.as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('ssn'); td('111'); td('222') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "pinky", lastName: "jones1", ssn: '111'],
          [firstName: "winky", lastName: "jones2", ssn: '222'],
      ]
      assert $('#table1').as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_with_key_extractor() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
      }
    }

    webdsl {
      def expected = [
          [first: "pinky", last: "jones1"],
          [first: "winky", last: "jones2"],
          [first: "dinky", last: "jones3"],
      ]

      Closure firstWordClosure = { it.text.split()[0] }

      assert table3.as.objects(keyExtractor: firstWordClosure) == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_with_key_extractor() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('ssn'); td('111'); td('222') }
      }
    }

    webdsl {
      def expected = [
          [first: "pinky", last: "jones1", ssn: '111'],
          [first: "winky", last: "jones2", ssn: '222'],
      ]

      Closure firstWordClosure = { it.text.split()[0] }

      assert $('#table1').as.objects(orientation: VERTICAL, keyExtractor: firstWordClosure) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_with_value_extractors() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name'); td('ssn') }
        tr { td('pinky'); td('jones1'); td('1-1-1') }
        tr { td('winky'); td('jones2'); td('2-2-2') }
        tr { td('dinky'); td('jones3'); td('3-3-3') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "PINKY", lastName: "jones1", ssn: "111"],
          [firstName: "WINKY", lastName: "jones2", ssn: "222"],
          [firstName: "DINKY", lastName: "jones3", ssn: "333"],
      ]

      Closure toUpperCaseClosure = { it.text.toUpperCase() }
      Closure removeDashesClosure = { it.text.replaceAll('-', '') }
      Map<String, Closure> valueExtractors = [firstName: toUpperCaseClosure, ssn: removeDashesClosure]

      assert table3.as.objects(valueExtractors: valueExtractors) == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_with_value_extractors() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('ssn'); td('1-1-1'); td('2-2-2') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "PINKY", lastName: "jones1", ssn: '111'],
          [firstName: "WINKY", lastName: "jones2", ssn: '222'],
      ]

      Closure toUpperCaseClosure = { it.text.toUpperCase() }
      Closure removeDashesClosure = { it.text.replaceAll('-', '') }
      Map<String, Closure> valueExtractors = [firstName: toUpperCaseClosure, ssn: removeDashesClosure]

      assert $('#table1').as.objects(orientation: VERTICAL, valueExtractors: valueExtractors) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_with_names() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name'); td('pay rate') }
        tr { td('pinky'); td('jones1'); td('111') }
        tr { td('winky'); td('jones2'); td('222') }
        tr { td('dinky'); td('jones3'); td('333') }
        tr { td('linky'); td('jones4'); td('444') }
        tr { td('stinky'); td('jones5'); td('555') }
      }
    }

    webdsl {
      def expected = [
          [first: "pinky", last: "jones1", payRate: "111"],
          [first: "winky", last: "jones2", payRate: "222"],
          [first: "dinky", last: "jones3", payRate: "333"],
          [first: "linky", last: "jones4", payRate: "444"],
          [first: "stinky", last: "jones5", payRate: "555"],
      ]
      assert table3.as.objects(names: ['first', 'last']) == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_with_names() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('pay rate'); td('111'); td('222') }
      }
    }

    webdsl {
      def expected = [
          [first: "pinky", last: "jones1", payRate: '111'],
          [first: "winky", last: "jones2", payRate: '222'],
      ]
      assert $('#table1').as.objects(orientation: VERTICAL, names: ['first', 'last']) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_rowRange() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "dinky", lastName: "jones3"],
          [firstName: "linky", lastName: "jones4"],
      ]
      assert table3(rowRange: 2..3).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_rowRange() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('ssn'); td('111'); td('222') }
        tr { td('pay rate'); td('1'); td('2') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones1", ssn: '111'],
          [lastName: "jones2", ssn: '222'],
      ]
      assert $('#table1').do(rowRange: 1..2).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_rowRange_no_objects() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
      }
    }

    webdsl {
      def expected = [];

      assert table3(rowRange: 2..3).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_rowRange_no_properties() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
      }
    }

    webdsl {
      def expected = [
          [:],
          [:],
      ]
      assert $('#table1').do(rowRange: 2..3).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_rowRange_through_end() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "dinky", lastName: "jones3"],
          [firstName: "linky", lastName: "jones4"],
          [firstName: "stinky", lastName: "jones5"],
      ]
      assert table3(rowRange: 2..-1).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_rowRange_through_end() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('ssn'); td('111'); td('222') }
        tr { td('pay rate'); td('1'); td('2') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones1", ssn: '111', payRate: '1'],
          [lastName: "jones2", ssn: '222', payRate: '2'],
      ]
      assert $('#table1').do(rowRange: 1..-1).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_rowRange_from_end() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "dinky", lastName: "jones3"],
          [firstName: "linky", lastName: "jones4"],
          [firstName: "stinky", lastName: "jones5"],
      ]
      assert table3(rowRange: -3..-1).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_rowRange_from_end() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('ssn'); td('111'); td('222') }
        tr { td('pay rate'); td('1'); td('2') }
      }
    }

    webdsl {
      def expected = [
          [ssn: '111', payRate: '1'],
          [ssn: '222', payRate: '2'],
      ]
      assert $('#table1').do(rowRange: -2..-1).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_rowRange_of_one() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "dinky", lastName: "jones3"],
      ]
      assert table3(rowRange: 2..-3).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_rowRange_of_one() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky') }
        tr { td('last name'); td('jones1'); td('jones2') }
        tr { td('ssn'); td('111'); td('222') }
        tr { td('pay rate'); td('1'); td('2') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones1"],
          [lastName: "jones2"],
      ]
      assert $('#table1').do(rowRange: 1..-3).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_columnRange() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name'); td('ssn'); td('pay rate') }
        tr { td('pinky'); td('jones1'); td('111'); td('1') }
        tr { td('winky'); td('jones2'); td('222'); td('2') }
        tr { td('dinky'); td('jones3'); td('333'); td('3') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones1", ssn: "111"],
          [lastName: "jones2", ssn: "222"],
          [lastName: "jones3", ssn: "333"],
      ]
      assert $('#table3').do(columnRange: 1..2).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_columnRange() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky'); td('dinky'); td('linky'); td('stinky') }
        tr { td('last name'); td('jones1'); td('jones2'); td('jones3'); td('jones4'); td('jones5') }
        tr { td('ssn'); td('111'); td('222'); td('333'); td('444'); td('555') }
        tr { td('pay rate'); td('1'); td('2'); td('3'); td('4'); td('5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: 'winky', lastName: "jones2", ssn: '222', payRate: '2'],
          [firstName: 'dinky', lastName: "jones3", ssn: '333', payRate: '3'],
          [firstName: 'linky', lastName: "jones4", ssn: '444', payRate: '4'],
      ]
      assert $('#table1').do(columnRange: 1..3).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_columnRange_no_properties() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name'); }
        tr { td('pinky'); td('jones1'); }
        tr { td('winky'); td('jones2'); }
      }
    }

    webdsl {
      def expected = [
          [:],
          [:],
      ]
      assert $('#table3').do(columnRange: 2..3).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_columnRange_no_objects() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky'); }
        tr { td('last name'); td('jones1'); td('jones2'); }
        tr { td('ssn'); td('111'); td('222'); }
        tr { td('pay rate'); td('1'); td('2'); }
      }
    }

    webdsl {
      def expected = []
      assert $('#table1').do(columnRange: 2..3).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_columnRange_through_end() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name'); td('ssn'); td('pay rate') }
        tr { td('pinky'); td('jones1'); td('111'); td('1') }
        tr { td('winky'); td('jones2'); td('222'); td('2') }
        tr { td('dinky'); td('jones3'); td('333'); td('3') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones1", ssn: "111", payRate: '1'],
          [lastName: "jones2", ssn: "222", payRate: '2'],
          [lastName: "jones3", ssn: "333", payRate: '3'],
      ]
      assert $('#table3').do(columnRange: 1..-1).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_columnRange_through_end() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky'); td('dinky'); td('linky'); td('stinky') }
        tr { td('last name'); td('jones1'); td('jones2'); td('jones3'); td('jones4'); td('jones5') }
        tr { td('ssn'); td('111'); td('222'); td('333'); td('444'); td('555') }
        tr { td('pay rate'); td('1'); td('2'); td('3'); td('4'); td('5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: 'winky', lastName: "jones2", ssn: '222', payRate: '2'],
          [firstName: 'dinky', lastName: "jones3", ssn: '333', payRate: '3'],
          [firstName: 'linky', lastName: "jones4", ssn: '444', payRate: '4'],
          [firstName: 'stinky', lastName: "jones5", ssn: '555', payRate: '5'],
      ]
      assert $('#table1').do(columnRange: 1..-1).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_columnRange_from_end() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name'); td('ssn'); td('pay rate') }
        tr { td('pinky'); td('jones1'); td('111'); td('1') }
        tr { td('winky'); td('jones2'); td('222'); td('2') }
        tr { td('dinky'); td('jones3'); td('333'); td('3') }
      }
    }

    webdsl {
      def expected = [
          [ssn: "111", payRate: '1'],
          [ssn: "222", payRate: '2'],
          [ssn: "333", payRate: '3'],
      ]
      assert $('#table3').do(columnRange: -1..-2).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_columnRange_from_end() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky'); td('dinky'); td('linky'); td('stinky') }
        tr { td('last name'); td('jones1'); td('jones2'); td('jones3'); td('jones4'); td('jones5') }
        tr { td('ssn'); td('111'); td('222'); td('333'); td('444'); td('555') }
        tr { td('pay rate'); td('1'); td('2'); td('3'); td('4'); td('5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: 'dinky', lastName: "jones3", ssn: '333', payRate: '3'],
          [firstName: 'linky', lastName: "jones4", ssn: '444', payRate: '4'],
          [firstName: 'stinky', lastName: "jones5", ssn: '555', payRate: '5'],
      ]
      assert $('#table1').do(columnRange: -1..-3).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_columnRange_of_one() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name'); td('ssn'); td('pay rate') }
        tr { td('pinky'); td('jones1'); td('111'); td('1') }
        tr { td('winky'); td('jones2'); td('222'); td('2') }
        tr { td('dinky'); td('jones3'); td('333'); td('3') }
      }
    }

    webdsl {
      def expected = [
          [ssn: "111"],
          [ssn: "222"],
          [ssn: "333"],
      ]
      assert $('#table3').do(columnRange: 2..-2).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_columnRange_of_one() {
    html {
      table(id: 'table1') {
        tr { td('first name'); td('pinky'); td('winky'); td('dinky'); td('linky'); td('stinky') }
        tr { td('last name'); td('jones1'); td('jones2'); td('jones3'); td('jones4'); td('jones5') }
        tr { td('ssn'); td('111'); td('222'); td('333'); td('444'); td('555') }
        tr { td('pay rate'); td('1'); td('2'); td('3'); td('4'); td('5') }
      }
    }

    webdsl {
      def expected = [
          [firstName: 'dinky', lastName: "jones3", ssn: '333', payRate: '3'],
      ]
      assert $('#table1').do(columnRange: 2..-3).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_column() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones1"],
          [lastName: "jones2"],
          [lastName: "jones3"],
          [lastName: "jones4"],
          [lastName: "jones5"],
      ]
      assert table3(column: 1).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_column() {
    html {
      table(id: 'table1') {
        tr { td('ignored'); td('first name'); td('pinky'); td('winky') }
        tr { td('ignored'); td('last name'); td('jones1'); td('jones2') }
        tr { td('ignored'); td('ssn'); td('111'); td('222') }
        tr { td('ignored'); td('pay rate'); td('1'); td('2') }
      }
    }

    webdsl {
      def expected = [
          [firstName: "pinky", lastName: "jones1", ssn: '111', payRate: '1'],
          [firstName: "winky", lastName: "jones2", ssn: '222', payRate: '2'],
      ]
      assert $('#table1').do(column: 1).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_horizontal_objects_column_offset_and_row_range() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones2"],
          [lastName: "jones3"],
          [lastName: "jones4"],
      ]
      assert table3(column: 1, rowRange: 1..3).as.objects == expected
    }
  }

  @Test
  void test_table_as_vertical_objects_column_offset_and_row_range() {
    html {
      table(id: 'table1') {
        tr { td('ignored'); td('first name'); td('pinky'); td('winky') }
        tr { td('ignored'); td('last name'); td('jones1'); td('jones2') }
        tr { td('ignored'); td('ssn'); td('111'); td('222') }
        tr { td('ignored'); td('pay rate'); td('1'); td('2') }
      }
    }

    webdsl {
      def expected = [
          [lastName: "jones1", ssn: '111'],
          [lastName: "jones2", ssn: '222'],
      ]
      assert $('#table1').do(column: 1, rowRange: 1..2).as.objects(orientation: VERTICAL) == expected
    }
  }

  @Test
  void test_table_as_list() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          "first name",
          "pinky",
          "winky",
          "dinky",
          "linky",
          "stinky",
      ]
      assert table3.as.list == expected
    }
  }

  @Test
  void test_table_as_list_with_offset() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          "last name",
          "jones1",
          "jones2",
          "jones3",
          "jones4",
          "jones5",
      ]
      assert table3(column: 1).as.list == expected
    }
  }

  @Test
  void test_table_as_list_with_row_range() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          "pinky",
          "winky",
          "dinky",
      ]
      assert table3(rowRange: 1..3).as.list == expected
    }
  }

  @Test
  void test_table_as_list_with_column_and_row_range() {
    html {
      table(id: 'table3') {
        tr { td('first name'); td('last name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          "jones1",
          "jones2",
          "jones3",
      ]
      assert table3(column: 1, rowRange: 1..3).as.list == expected
    }
  }

  @Test
  void test_table_as_columns() {
    html {
      table(id: 'table3') {
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [first: "pinky", last: "jones1"],
          [first: "winky", last: "jones2"],
          [first: "dinky", last: "jones3"],
          [first: "linky", last: "jones4"],
          [first: "stinky", last: "jones5"],
      ]
      assert table3.as.columns('first', 'last') == expected
    }
  }

  @Test
  void test_table_as_columns_not_all_columns_requested() {
    html {
      table(id: 'table3') {
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
      }
    }

    webdsl {
      def expected = [
          [first: "pinky"],
          [first: "winky"],
          [first: "dinky"],
      ]
      assert table3.as.columns('first') == expected
    }
  }

  @Test
  void test_table_as_columns_extra_columns_requested() {
    html {
      table(id: 'table3') {
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
      }
    }

    webdsl {
      def expected = [
          [first: "pinky", last: "jones1", ssn: ""],
          [first: "winky", last: "jones2", ssn: ""],
          [first: "dinky", last: "jones3", ssn: ""],
      ]
      assert table3.as.columns('first', 'last', 'ssn') == expected
    }
  }

  @Test
  void test_table_as_columns_with_column_offset() {
    html {
      table(id: 'table3') {
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [last: "jones1"],
          [last: "jones2"],
          [last: "jones3"],
          [last: "jones4"],
          [last: "jones5"],
      ]
      assert table3(column: 1).as.columns('last') == expected
    }
  }

  @Test
  void test_table_as_columns_with_row_range() {
    html {
      table(id: 'table3') {
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [first: "winky", last: "jones2"],
          [first: "dinky", last: "jones3"],
          [first: "linky", last: "jones4"],
      ]
      assert table3(rowRange: 1..3).as.columns('first', 'last') == expected
    }
  }

  @Test
  void test_table_as_columns_with_column_offset_and_row_range() {
    html {
      table(id: 'table3') {
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
        tr { td('dinky'); td('jones3') }
        tr { td('linky'); td('jones4') }
        tr { td('stinky'); td('jones5') }
      }
    }

    webdsl {
      def expected = [
          [last: "jones2"],
          [last: "jones3"],
          [last: "jones4"],
      ]
      assert table3(column: 1, rowRange: 1..3).as.columns('last') == expected
    }
  }

  @Test
  void test_table_as_object__values_can_have_spaces() {
    html {
      table(id: 'person') {
        tr { td('first'); td('pinky') }
        tr { td('last'); td('jones') }
        tr { td('text'); td('this is pinky') }
      }
    }

    webdsl {
      def expected = [
          first: 'pinky',
          last: 'jones',
          text: 'this is pinky'
      ]
      assert person.as.object == expected
    }
  }

  @Test
  void test_table_as_object() {
    html {
      table(id: 'person') {
        tr { td('first'); td('pinky') }
        tr { td('last'); td('jones') }
        tr { td('ssn'); td('111-11-1111') }
      }
    }

    webdsl {
      def expected = [
          first: 'pinky',
          last: 'jones',
          ssn: '111-11-1111'
      ]
      assert person.as.object == expected
    }
  }

  @Test
  void test_table_as_object_key_extractor() {
    html {
      table(id: 'person') {
        tr { td('first name'); td('pinky') }
        tr { td('last name'); td('jones') }
        tr { td('ssn'); td('111-11-1111') }
      }
    }

    webdsl {
      def expected = [
          ('FIRST NAME'): 'pinky',
          ('LAST NAME'): 'jones',
          SSN: '111-11-1111'
      ]
      assert person.as.object(keyExtractor: { it.text.toUpperCase() }) == expected
    }
  }

  @Test
  void test_table_as_object_value_extractors() {
    html {
      table(id: 'person') {
        tr { td('first'); td('pinky') }
        tr { td('last'); td('jones') }
        tr { td('ssn'); td('111-11-1111') }
      }
    }

    webdsl {
      def expected = [
          first: 'PINKY',
          last: 'jones',
          ssn: '111111111'
      ]

      Closure toUpperCaseClosure = { it.text.toUpperCase() }
      Closure removeDashesClosure = { it.text.replaceAll('-', '') }

      assert person.as.object(valueExtractors: [first: toUpperCaseClosure, ssn: removeDashesClosure ]) == expected
    }
  }

  @Test
  void test_table_as_object_with_rowRange() {
    html {
      table(id: 'person') {
        tr { td('first'); td('pinky') }
        tr { td('last'); td('jones') }
        tr { td('ssn'); td('111-11-1111') }
      }
    }

    webdsl {
      def expected = [
          last: 'jones',
          ssn: '111-11-1111'
      ]
      assert person(rowRange: 1..2).as.object == expected
    }
  }

  @Test
  void test_table_as_object_with_column() {
    html {
      table(id: 'person') {
        tr { td('crap'); td('first'); td('pinky') }
        tr { td('crap'); td('last'); td('jones') }
        tr { td('crap'); td('ssn'); td('111-11-1111') }
      }
    }

    webdsl {
      def expected = [
          first: 'pinky',
          last: 'jones',
          ssn: '111-11-1111'
      ]
      assert person(column: 1).as.object == expected
    }
  }

  @Test
  void test_table_process() {
    html {
      table(id: 'employees') {
        tr { td('First Name'); td('Last Name') }
        tr { td('pinky'); td('jones1') }
        tr { td('winky'); td('jones2') }
      }
    }

    webdsl {
      def result = []

      employees.process { row, column, td ->
        result << [rowIndex: row, columnIndex: column, content: td.textContent.trim()]
      }

      def expected = [
          [rowIndex: 0, columnIndex: 0, content: "First Name"],
          [rowIndex: 0, columnIndex: 1, content: "Last Name"],
          [rowIndex: 1, columnIndex: 0, content: "pinky"],
          [rowIndex: 1, columnIndex: 1, content: "jones1"],
          [rowIndex: 2, columnIndex: 0, content: "winky"],
          [rowIndex: 2, columnIndex: 1, content: "jones2"],
      ]

      assert result == expected
    }
  }

  @Test
  void test_table_by_span() {
    html {
      table(id: 'employees') {
        tr {
          td { span(name: 'firstName', 'pinky') }; td { span(name: 'lastName', 'jones') }
        }
        tr {
          td { span(name: 'firstName', 'john') }; td { span(name: 'lastName', 'doe') }
        }
      }
    }

    webdsl {
      def expected = [[firstName: "pinky", lastName: "jones"], [firstName: "john", lastName: "doe"]]
      assert employees.by.span == expected
    }
  }
}