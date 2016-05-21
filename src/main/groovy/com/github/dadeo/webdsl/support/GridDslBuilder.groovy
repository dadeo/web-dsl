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


class GridDslBuilder<T> {
    private GridDsl gridDsl
    private RowRange rowRange
    private RowRange columnRange
    private int lastRowProcessed = -1

    GridDslBuilder(PageContainer pageContainer, DslFactory factory, Map<String, Object> options, int rows, int columns) {
        if (options.startRow && options.rowRange)
            throw new RuntimeException("The options 'rowRange' and 'startRow' are mutually exclusive.")

        if (options.startColumn && options.columnRange)
            throw new RuntimeException("The options 'columnRange' and 'startColumn' are mutually exclusive.")

        if (options.offset || options.column)
            throw new RuntimeException("The options 'offset' and 'column' are no longer supported.")

        rowRange = createRowRange(options, rows)
        columnRange = createColumnRange(options, columns)

        gridDsl = new GridDsl(pageContainer, factory)
    }

    void append(int row, int column, T value) {
        boolean append = rowRange.contains(row) && columnRange.contains(column)

        if (append) {
            if (lastRowProcessed != row) {
                gridDsl.nextRow value
                lastRowProcessed = row
            } else {
                gridDsl.appendColumn value
            }
        }

    }

    GridDsl buildGrid() {
        gridDsl
    }

    protected RowRange createRowRange(Map<String, Object> options, int size) {
        if (options.rowRange)
            return new RowRange(options.rowRange as IntRange, size)

        if (options.startRow) {
            int start = options.startRow as int
            int end = Math.max(size, start)
            return new RowRange(start..end, size)
        }

        new RowRange(0..-1, size)
    }

    protected RowRange createColumnRange(Map<String, Object> options, int size) {
        if (options.columnRange)
            return new RowRange(options.columnRange as IntRange, size)

        if (options.startColumn) {
            int start = options.startColumn as int
            int end = Math.max(size, start)
            return new RowRange(start..end, size)
        }

        new RowRange(0..-1, size)
    }

}
