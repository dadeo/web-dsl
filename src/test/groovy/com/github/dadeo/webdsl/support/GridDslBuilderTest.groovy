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
package com.github.dadeo.webdsl.support

import org.junit.Test

import static org.junit.Assert.fail

class GridDslBuilderTest {

    /*
     *  source   -----------> target
     *
     *  aa bb cc              aa bb cc
     *  a1 b1 c1              a1 b1 c1
     *  a2 b2 c2              a2 b2 c2
     *  a3 b3 c3              a3 b3 c3
     *
     */

    @Test
    void test_build_no_options() {
        List<List<Object>> data = [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [:])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  -- -- --
     *  -- -- --
     *  aa bb cc              aa bb cc
     *  a1 b1 c1              a1 b1 c1
     *  a2 b2 c2              a2 b2 c2
     *  a3 b3 c3              a3 b3 c3
     *
     */

    @Test
    void test_build_rowRange_skip_first_rows() {
        List<List<Object>> data = [
            ['--', '--', '--'],
            ['--', '--', '--'],
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [rowRange: 2..-1])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  aa bb cc              aa bb cc
     *  a1 b1 c1              a1 b1 c1
     *  a2 b2 c2              a2 b2 c2
     *  a3 b3 c3              a3 b3 c3
     *  -- -- --
     *  -- -- --
     *
     */

    @Test
    void test_build_rowRange_skip_last_rows() {
        List<List<Object>> data = [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
            ['--', '--', '--'],
            ['--', '--', '--'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [rowRange: 0..-3])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  -- -- --
     *  aa bb cc              aa bb cc
     *  a1 b1 c1              a1 b1 c1
     *  a2 b2 c2              a2 b2 c2
     *  a3 b3 c3              a3 b3 c3
     *  -- -- --
     *
     */

    @Test
    void test_build_rowRange_extract_middle_rows() {
        List<List<Object>> data = [
            ['--', '--', '--'],
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
            ['--', '--', '--'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [rowRange: 1..-2])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  -- -- --
     *  -- -- --
     *  aa bb cc              aa bb cc
     *  a1 b1 c1              a1 b1 c1
     *  a2 b2 c2              a2 b2 c2
     *  a3 b3 c3              a3 b3 c3
     *
     */

    @Test
    void test_build_startRow() {
        List<List<Object>> data = [
            ['--', '--', '--'],
            ['--', '--', '--'],
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [startRow: 2])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    @Test
    void test_build_faliure_rowRange_and_startRow_are_mutually_exclusive() {
        List<List<Object>> data = [[]]

        try {
            GridDsl gridDsl = buildGridDsl(data, [startRow: 2, rowRange: 1..-2])
            fail("expected exception")
        } catch (RuntimeException e) {
            String message = e.message.toLowerCase()
            assert message.contains('startrow')
            assert message.contains('rowrange')
            assert message.contains('exclusive')
        }
    }

    /*
     *  source   -----------> target
     *
     *  -- -- aa bb cc        aa bb cc
     *  -- -- a1 b1 c1        a1 b1 c1
     *  -- -- a2 b2 c2        a2 b2 c2
     *  -- -- a3 b3 c3        a3 b3 c3
     *
     */

    @Test
    void test_build_columnRange_skip_first_columns() {
        List<List<Object>> data = [
            ['--', '--', 'aa', 'bb', 'cc'],
            ['--', '--', 'a1', 'b1', 'c1'],
            ['--', '--', 'a2', 'b2', 'c2'],
            ['--', '--', 'a3', 'b3', 'c3'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [columnRange: 2..-1])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  aa bb cc -- --        aa bb cc
     *  a1 b1 c1 -- --        a1 b1 c1
     *  a2 b2 c2 -- --        a2 b2 c2
     *  a3 b3 c3 -- --        a3 b3 c3
     *
     */

    @Test
    void test_build_columnRange_skip_last_columns() {
        List<List<Object>> data = [
            ['aa', 'bb', 'cc', '--', '--'],
            ['a1', 'b1', 'c1', '--', '--'],
            ['a2', 'b2', 'c2', '--', '--'],
            ['a3', 'b3', 'c3', '--', '--'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [columnRange: 0..-3])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  -- aa bb cc --        aa bb cc
     *  -- a1 b1 c1 --        a1 b1 c1
     *  -- a2 b2 c2 --        a2 b2 c2
     *  -- a3 b3 c3 --        a3 b3 c3
     *
     */

    @Test
    void test_build_columnRange_extract_middle_columns() {
        List<List<Object>> data = [
            ['--', 'aa', 'bb', 'cc', '--'],
            ['--', 'a1', 'b1', 'c1', '--'],
            ['--', 'a2', 'b2', 'c2', '--'],
            ['--', 'a3', 'b3', 'c3', '--'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [columnRange: 1..-2])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  -- -- aa bb cc        aa bb cc
     *  -- -- a1 b1 c1        a1 b1 c1
     *  -- -- a2 b2 c2        a2 b2 c2
     *  -- -- a3 b3 c3        a3 b3 c3
     *
     */

    @Test
    void test_build_startColumn() {
        List<List<Object>> data = [
            ['--', '--', 'aa', 'bb', 'cc'],
            ['--', '--', 'a1', 'b1', 'c1'],
            ['--', '--', 'a2', 'b2', 'c2'],
            ['--', '--', 'a3', 'b3', 'c3'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [startColumn: 2])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    @Test
    void test_build_faliure_columnRange_and_startColumn_are_mutually_exclusive() {
        List<List<Object>> data = [[]]

        try {
            buildGridDsl(data, [startColumn: 2, columnRange: 1..-2])
            fail("expected exception")
        } catch (RuntimeException e) {
            String message = e.message.toLowerCase()
            assert message.contains("'startcolumn'")
            assert message.contains("'columnrange'")
            assert message.contains('exclusive')
        }
    }

    /*
     *  source   -----------> target
     *
     *  -- -- -- -- --
     *  -- -- -- -- --
     *  -- aa bb cc --        aa bb cc
     *  -- a1 b1 c1 --        a1 b1 c1
     *  -- a2 b2 c2 --        a2 b2 c2
     *  -- a3 b3 c3 --        a3 b3 c3
     *  -- -- -- -- --
     *  -- -- -- -- --
     *
     */

    @Test
    void test_build_rowRange_and_columnRange_extract_middle_columns() {
        List<List<Object>> data = [
            ['--', '--', '--', '--', '--'],
            ['--', '--', '--', '--', '--'],
            ['--', 'aa', 'bb', 'cc', '--'],
            ['--', 'a1', 'b1', 'c1', '--'],
            ['--', 'a2', 'b2', 'c2', '--'],
            ['--', 'a3', 'b3', 'c3', '--'],
            ['--', '--', '--', '--', '--'],
            ['--', '--', '--', '--', '--'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [rowRange: 2..-3, columnRange: 1..-2])

        assert gridDsl.grid == [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]
    }

    /*
     *  source   -----------> target
     *
     *  aa bb cc
     *  a1 b1 c1
     *  a2 b2 c2
     *  a3 b3 c3
     *
     */

    @Test
    void test_build_no_rows() {
        List<List<Object>> data = [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [startRow: 10])

        assert gridDsl.grid == []
    }

    /*
     *  source   -----------> target
     *
     *  aa bb cc
     *  a1 b1 c1
     *  a2 b2 c2
     *  a3 b3 c3
     *
     */

    @Test
    void test_build_no_columns() {
        List<List<Object>> data = [
            ['aa', 'bb', 'cc'],
            ['a1', 'b1', 'c1'],
            ['a2', 'b2', 'c2'],
            ['a3', 'b3', 'c3'],
        ]

        GridDsl gridDsl = buildGridDsl(data, [startColumn: 10])

        assert gridDsl.grid == []
    }

    @Test
    void test_build_populates_pageContainer_and_dslFactory() {
        PageContainer pageContainer = [:] as PageContainer
        DslFactory dslFactory = [:] as DslFactory

        GridDslBuilder builder = new GridDslBuilder(pageContainer, dslFactory, [rowRange: 2..-3, columnRange: 1..-2], 1, 1)
        GridDsl gridDsl = builder.buildGrid()

        assert gridDsl.pageContainer == pageContainer
        assert gridDsl.factory == dslFactory
    }

    protected GridDsl buildGridDsl(List<List<Object>> data, Map<String, Object> options) {
        int rowCount = data.size()
        int columnCount = data[0].size()

        GridDslBuilder builder = new GridDslBuilder(null, null, options, rowCount, columnCount)

        for (int r = 0; r < rowCount; ++r) {
            List<Object> row = data[r]
            for (int c = 0; c < row.size(); ++c) {
                builder.append(r, c, row[c])
            }
        }

        builder.buildGrid()
    }

}
