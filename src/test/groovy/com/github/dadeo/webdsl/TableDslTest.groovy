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
package com.github.dadeo.webdsl

import org.junit.Test

import static com.github.dadeo.webdsl.Orientation.VERTICAL

@Mixin(NonServerMixin)
class TableDslTest {

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
    void test_table_as_list_with_startColumn() {
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
            assert table3(startColumn: 1).as.list == expected
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
            assert table3(startColumn: 1, rowRange: 1..3).as.list == expected
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
    void test_table_as_columns_with_startColumn() {
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
            assert table3(startColumn: 1).as.columns('last') == expected
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
    void test_table_as_columns_with_startColumn_and_row_range() {
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
            assert table3(startColumn: 1, rowRange: 1..3).as.columns('last') == expected
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
                last : 'jones',
                text : 'this is pinky'
            ]
            assert $('#person').as.object(orientation: VERTICAL) == expected
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
                last : 'jones',
                ssn  : '111-11-1111'
            ]
            assert $('#person').as.object(orientation: VERTICAL) == expected
        }
    }

    @Test
    void test_table_as_object_when_table_empty() {
        html {
            table(id: 'person') {
            }
        }

        webdsl {
            def expected = [:]
            assert $('#person').as.object(orientation: VERTICAL) == expected
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
                ('LAST NAME') : 'jones',
                SSN           : '111-11-1111'
            ]
            assert $('#person').as.object(orientation: VERTICAL, keyExtractor: { it.text.toUpperCase() }) == expected
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
                last : 'jones',
                ssn  : '111111111'
            ]

            Closure toUpperCaseClosure = { it.text.toUpperCase() }
            Closure removeDashesClosure = { it.text.replaceAll('-', '') }

            assert $('#person').as.object(orientation: VERTICAL, valueExtractors: [first: toUpperCaseClosure, ssn: removeDashesClosure]) == expected
        }
    }

    @Test
    void test_table_as_object_value_extractor_is_able_to_return_an_object() {
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
                last : 'jones',
                ssn  : [part1: '111', part2: '11', part3: '1111']
            ]

            Closure ssnToMapClosure = {
                def parts = it.text.trim().split('-')
                [part1: parts[0], part2: parts[1], part3: parts[2]]
            }

            assert $('#person').as.object(orientation: VERTICAL, valueExtractors: [ssn: ssnToMapClosure]) == expected
        }
    }

    @Test
    void test_table_as_object_names() {
        html {
            table(id: 'person') {
                tr { td('first'); td('pinky') }
                tr { td('last'); td('jones') }
                tr { td('ssn'); td('111') }
            }
        }

        webdsl {
            def expected = [
                firstName: 'pinky',
                lastName : 'jones',
                ssn      : '111'
            ]

            assert $('#person').structure(startColumn: 1).as.object(orientation: VERTICAL, names: ['firstName', 'lastName', 'ssn']) == expected
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
                ssn : '111-11-1111'
            ]
            assert $('#person').structure(rowRange: 1..2).as.object(orientation: VERTICAL) == expected
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
                last : 'jones',
                ssn  : '111-11-1111'
            ]
            assert $('#person').structure(startColumn: 1).as.object(orientation: VERTICAL) == expected
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
                result << [rowIndex: row, columnIndex: column, content: td.text]
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