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

import com.github.dadeo.webdsl.support.BaseElementDsl
import com.github.dadeo.webdsl.support.GridDsl
import org.junit.Test

import static com.github.dadeo.webdsl.Orientation.HORIZONTAL
import static com.github.dadeo.webdsl.Orientation.VERTICAL

@Mixin(NonServerMixin)
class TableDsl_objects_Test {

    @Test
    void test_table_as_objects_defaults_to_horizontal() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]
            assert $('#items').as.objects == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            assert $('#items').as.objects(orientation: HORIZONTAL) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            assert $('#items').as.objects(orientation: VERTICAL) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_with_offset_and_column() {
        html {
            table(id: 'items') {
                tr { td('xx'); td('xx'); td('xx'); td('xx') }
                tr { td('xx'); td('a'); td('b'); td('c') }
                tr { td('xx'); td('a1'); td('b1'); td('c1') }
                tr { td('xx'); td('a2'); td('b2'); td('c2') }
                tr { td('xx'); td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            assert $('#items').structure(startRow: 1, startColumn: 1).as.objects(orientation: HORIZONTAL) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_with_offset_and_column() {
        html {
            table(id: 'items') {
                tr { td('xx'); td('xx'); td('xx'); td('xx'); td('xx') }
                tr { td('xx'); td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('xx'); td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('xx'); td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            assert $('#items').structure(startRow: 1, startColumn: 1).as.objects(orientation: VERTICAL) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_with_rowRange_and_columnRange() {
        html {
            table(id: 'items') {
                tr { td('xx'); td('xx'); td('xx'); td('xx') }
                tr { td('xx'); td('a'); td('b'); td('xx') }
                tr { td('xx'); td('a1'); td('b1'); td('xx') }
                tr { td('xx'); td('a2'); td('b2'); td('xx') }
                tr { td('xx'); td('a3'); td('b3'); td('xx') }
                tr { td('xx'); td('xx'); td('xx'); td('xx') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1'],
                [a: 'a2', b: 'b2'],
                [a: 'a3', b: 'b3'],
            ]

            assert $('#items').structure(rowRange: 1..-2, columnRange: 1..-2).as.objects(orientation: HORIZONTAL) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_with_rowRange_and_columnRange() {
        html {
            table(id: 'items') {
                tr { td('xx'); td('xx'); td('xx'); td('xx'); td('xx') }
                tr { td('xx'); td('a'); td('a1'); td('a2'); td('xx') }
                tr { td('xx'); td('b'); td('b1'); td('b2'); td('xx') }
                tr { td('xx'); td('c'); td('c1'); td('c2'); td('xx') }
                tr { td('xx'); td('xx'); td('xx'); td('xx'); td('xx') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
            ]

            assert $('#items').structure(rowRange: 1..-2, columnRange: 1..-2).as.objects(orientation: VERTICAL) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_with_rowRange_of_1() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected1 = [
            ]

            assert $('#items').structure(rowRange: 0..-4).objects(orientation: HORIZONTAL) == expected1

            def expected2 = [
                [aa: 'a', bb: 'b', cc: 'c'],
            ]

            assert $('#items').structure(rowRange: 0..-4).objects(orientation: HORIZONTAL, names: ['aa', 'bb', 'cc']) == expected2
        }
    }

    @Test
    void test_table_as_vertical_objects_with_rowRange_of_1() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1'],
                [a: 'a2'],
                [a: 'a3'],
            ]

            assert $('#items').structure(rowRange: 0..-3).objects(orientation: VERTICAL) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_with_columnRange_of_1() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1'],
                [a: 'a2'],
                [a: 'a3'],
            ]

            assert $('#items').structure(columnRange: 0..-3).objects(orientation: HORIZONTAL) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_with_columnRange_of_1() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected1 = [
            ]

            assert $('#items').structure(columnRange: 0..-4).objects(orientation: VERTICAL) == expected1

            def expected2 = [
                [aa: 'a', bb: 'b', cc: 'c'],
            ]

            assert $('#items').structure(columnRange: 0..-4).objects(orientation: VERTICAL, names: ['aa', 'bb', 'cc']) == expected2

        }
    }

    @Test
    void test_table_as_horizontal_objects_no_captured_fields() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
            ]

            assert $('#items').structure(startColumn: 5).objects(orientation: HORIZONTAL) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_no_captured_fields() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
            ]

            assert $('#items').structure(startRow: 5).objects(orientation: VERTICAL) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_no_captured_objects() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
            ]

            assert $('#items').structure(startRow: 5).objects(orientation: HORIZONTAL) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_no_captured_objects() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
            ]

            assert $('#items').structure(startColumn: 5).objects(orientation: VERTICAL) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_specify_names() {
        html {
            table(id: 'items') {
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            assert $('#items').as.objects(orientation: HORIZONTAL, names: ['a', 'b', 'c']) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_specify_names() {
        html {
            table(id: 'items') {
                tr { td('a1'); td('a2'); td('a3') }
                tr { td('b1'); td('b2'); td('b3') }
                tr { td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            assert $('#items').as.objects(orientation: VERTICAL, names: ['a', 'b', 'c']) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_with_key_extractor() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [aa: 'a1', bb: 'b1', cc: 'c1'],
                [aa: 'a2', bb: 'b2', cc: 'c2'],
                [aa: 'a3', bb: 'b3', cc: 'c3'],
            ]

            assert $('#items').as.objects(orientation: HORIZONTAL, keyExtractor: { it.text * 2 }) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_with_key_extractor() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [aa: 'a1', bb: 'b1', cc: 'c1'],
                [aa: 'a2', bb: 'b2', cc: 'c2'],
                [aa: 'a3', bb: 'b3', cc: 'c3'],
            ]

            assert $('#items').as.objects(orientation: VERTICAL, keyExtractor: { it.text * 2 }) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_with_value_extractors() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1a', b: 'b1b', c: 'c1c'],
                [a: 'a2a', b: 'b2b', c: 'c2c'],
                [a: 'a3a', b: 'b3b', c: 'c3c'],
            ]

            def valueExtractors = [
                a: { it.text + 'a' },
                b: { it.text + 'b' },
                c: { it.text + 'c' },
            ]

            assert $('#items').as.objects(orientation: HORIZONTAL, valueExtractors: valueExtractors) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_with_value_extractors() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1a', b: 'b1b', c: 'c1c'],
                [a: 'a2a', b: 'b2b', c: 'c2c'],
                [a: 'a3a', b: 'b3b', c: 'c3c'],
            ]


            def valueExtractors = [
                a: { it.text + 'a' },
                b: { it.text + 'b' },
                c: { it.text + 'c' },
            ]
            assert $('#items').as.objects(orientation: VERTICAL, valueExtractors: valueExtractors) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_value_extractor_precedence() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('xx'); td('xx'); td('xx') }
            }
        }

        webdsl {
            def expected = [
                [a: 'yy', b: 'zz', c: 'yy'],
            ]

            def yyExtractor = { 'yy' }
            def zzExtractor = { 'zz' }

            assert $('#items').as.objects(orientation: HORIZONTAL, valueExtractors: [b: zzExtractor], valueExtractor: yyExtractor) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_value_extractor_precedence() {
        html {
            table(id: 'items') {
                tr { td('a'); td('xx') }
                tr { td('b'); td('xx') }
                tr { td('c'); td('xx') }
            }
        }

        webdsl {
            def expected = [
                [a: 'yy', b: 'zz', c: 'yy'],
            ]

            def yyExtractor = { 'yy' }
            def zzExtractor = { 'zz' }

            assert $('#items').as.objects(orientation: VERTICAL, valueExtractors: [b: zzExtractor], valueExtractor: yyExtractor) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_with_value_extractors_that_return_objects() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: [name: 'a1'], b: [name: 'b1'], c: [name: 'c1']],
                [a: [name: 'a2'], b: [name: 'b2'], c: [name: 'c2']],
                [a: [name: 'a3'], b: [name: 'b3'], c: [name: 'c3']],
            ]

            def objectExtractor = { [name: it.text] }

            assert $('#items').as.objects(orientation: HORIZONTAL, valueExtractor: objectExtractor) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_with_value_extractor_that_return_objects() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: [name: 'a1'], b: [name: 'b1'], c: [name: 'c1']],
                [a: [name: 'a2'], b: [name: 'b2'], c: [name: 'c2']],
                [a: [name: 'a3'], b: [name: 'b3'], c: [name: 'c3']],
            ]

            def objectExtractor = { [name: it.text] }

            assert $('#items').as.objects(orientation: VERTICAL, valueExtractor: objectExtractor) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects_value_extractor_parameter_is_instanceof_BaseElementDsl() {
        html {
            table(id: 'items') {
                tr { td('a'); td('b'); td('c') }
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1a', b: 'b1b', c: 'c1c'],
                [a: 'a2a', b: 'b2b', c: 'c2c'],
                [a: 'a3a', b: 'b3b', c: 'c3c'],
            ]

            def valueExtractors = [
                a: { assert it instanceof BaseElementDsl; it.text + 'a' },
                b: { assert it instanceof BaseElementDsl; it.text + 'b' },
                c: { assert it instanceof BaseElementDsl; it.text + 'c' },
            ]

            assert $('#items').as.objects(orientation: HORIZONTAL, valueExtractors: valueExtractors) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_value_extractor_parameter_is_instanceof_BaseElementDsl() {
        html {
            table(id: 'items') {
                tr { td('a'); td('a1'); td('a2'); td('a3') }
                tr { td('b'); td('b1'); td('b2'); td('b3') }
                tr { td('c'); td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1a', b: 'b1b', c: 'c1c'],
                [a: 'a2a', b: 'b2b', c: 'c2c'],
                [a: 'a3a', b: 'b3b', c: 'c3c'],
            ]


            def valueExtractors = [
                a: { assert it instanceof BaseElementDsl; it.text + 'a' },
                b: { assert it instanceof BaseElementDsl; it.text + 'b' },
                c: { assert it instanceof BaseElementDsl; it.text + 'c' },
            ]
            assert $('#items').as.objects(orientation: VERTICAL, valueExtractors: valueExtractors) == expected
        }
    }

    @Test
    void test_table_as_horizontal_query_is_repeatable() {
        html {
            table(id: 'items') {
                tr { td('a1'); td('b1'); td('c1') }
                tr { td('a2'); td('b2'); td('c2') }
                tr { td('a3'); td('b3'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            GridDsl gridDsl = $('#items').getAs()
            assert gridDsl.objects(orientation: HORIZONTAL, names: ['a', 'b', 'c']) == expected
            assert gridDsl.objects(orientation: HORIZONTAL, names: ['a', 'b', 'c']) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects_query_is_repeatable() {
        html {
            table(id: 'items') {
                tr { td('a1'); td('a2'); td('a3') }
                tr { td('b1'); td('b2'); td('b3') }
                tr { td('c1'); td('c2'); td('c3') }
            }
        }

        webdsl {
            def expected = [
                [a: 'a1', b: 'b1', c: 'c1'],
                [a: 'a2', b: 'b2', c: 'c2'],
                [a: 'a3', b: 'b3', c: 'c3'],
            ]

            GridDsl gridDsl = $('#items').getAs()
            assert gridDsl.objects(orientation: VERTICAL, names: ['a', 'b', 'c']) == expected
            assert gridDsl.objects(orientation: VERTICAL, names: ['a', 'b', 'c']) == expected
        }
    }

    @Test
    void test_table_as_horizontal_objects__values_can_have_spaces() {
        html {
            table(id: 'table1') {
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
            assert $('#table1').as.objects(orientation: HORIZONTAL) == expected
        }
    }

    @Test
    void test_table_as_vertical_objects__values_can_have_spaces() {
        html {
            table(id: 'table1') {
                tr { td('first name'); td('pinky'); td('winky') }
                tr { td('last name'); td('jones1'); td('jones2') }
                tr { td('text'); td('this is pinky'); td('this is winky') }
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


}