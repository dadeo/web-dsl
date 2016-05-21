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

import com.gargoylesoftware.htmlunit.html.HtmlSpan
import com.github.dadeo.webdsl.Orientation
import com.github.dadeo.webdsl.WebDsl

import static com.github.dadeo.webdsl.Orientation.HORIZONTAL

class GridDsl {
    List grid = []
    private Orientation gridOrientation = HORIZONTAL
    private PageContainer pageContainer
    private DslFactory factory
    private Closure textClosure = { it.text.trim() }
    private Closure camelCaseClosure = { WebDsl.camel(it.text.trim()) }

    GridDsl(PageContainer pageContainer, DslFactory factory) {
        this.pageContainer = pageContainer
        this.factory = factory
    }

    def nextRow(td) {
        grid << [td]
    }

    def appendColumn(td) {
        grid[-1] << td
    }

    def getAs() {
        this
    }

    def getSpan() {
        def result = []
        process { row, column, td ->
            if (column == 0) {
                result << [:]
            }
            td.element.htmlElementDescendants.each { span ->
                if (span instanceof HtmlSpan && span.getAttribute("name")) {
                    result[-1][span.getAttribute("name")] = span.getTextContent()
                }
            }
        }
        result
    }

    def getList() {
        def result = []
        process { row, column, td ->
            if (column == 0) {
                result << td.text
            }
        }
        result
    }

    def getObject() {
        object([:])
    }

    def object(Map tableOptions) {
        objects(tableOptions)[0] ?: [:]
    }

    def getObjects() {
        objects([:])
    }

    def objects(Map tableOptions) {
        reorientGrid(tableOptions)

        Map<Integer, String> attributes = extractKeys(tableOptions)

        def results = []
        int lastRow = -1
        int startRow = tableOptions.names ? 0 : 1
        process(startRow) { row, column, td ->
            if (lastRow != row) {
                results << [:]
                lastRow = row
            }
            String key = attributes[column]
            results[-1][key] = extractValue(tableOptions, td, key)
        }

        results
    }

    def columns(String... columnNames) {
        def result = []
        def map = [:]
        def finalize = {
            (map.size()..<columnNames.size()).each { index -> map[columnNames[index]] = "" }
            result << map
        }
        def oldRow = -1
        process { row, column, td ->
            if (oldRow != -1 && row != oldRow && column == 0) {
                finalize()
                map = [:]
            }
            if (column < columnNames.size()) {
                map[columnNames[column]] = td.text
            }
            oldRow = row
        }
        finalize()
        result
    }

    def process(Closure closure) {
        process(0, closure)
    }

    def process(int fromIndex, Closure closure) {
        for (int row = fromIndex; row < grid.size(); ++row) {
            List columnList = grid[row]
            for (int column = 0; column < columnList.size(); ++column) {
                def td = columnList[column]
                closure(row, column, td)
            }
        }
    }

    protected void reorientGrid(Map tableOptions) {
        Orientation optionOrientation = tableOptions.orientation ?: HORIZONTAL
        if (gridOrientation != optionOrientation) {
            grid = grid.transpose()
            gridOrientation = optionOrientation
        }
    }

    protected Map<Integer, String> extractKeys(Map<String, Object> tableOptions) {
        Map<Integer, String> result = [:]
        if (tableOptions.names) {
            ((List<String>) tableOptions.names).eachWithIndex { String name, Integer index -> result[index] = name }
        } else {
            grid[0].eachWithIndex { td, Integer index -> result[index] = extractKey(tableOptions, td) }
        }
        result
    }

    protected String extractKey(Map tableOptions, BaseElementDsl elementDsl) {
        Closure keyExtractor = tableOptions.keyExtractor ?: camelCaseClosure
        keyExtractor(elementDsl)
    }

    protected Object extractValue(Map tableOptions, BaseElementDsl elementDsl, String key) {
        Closure valueExtractor = tableOptions.valueExtractors?.get(key) ?: tableOptions.valueExtractor ?: textClosure
        valueExtractor(elementDsl)
    }

}