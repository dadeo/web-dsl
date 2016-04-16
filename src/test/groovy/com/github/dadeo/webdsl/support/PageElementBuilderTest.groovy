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

import com.gargoylesoftware.htmlunit.html.DomAttr
import com.gargoylesoftware.htmlunit.html.HtmlDivision
import com.gargoylesoftware.htmlunit.html.HtmlElement
import com.gargoylesoftware.htmlunit.html.HtmlOrderedList
import com.gargoylesoftware.htmlunit.html.HtmlParagraph
import org.junit.Before
import org.junit.Test
import com.github.dadeo.webdsl.WebDsl
import com.github.dadeo.webdsl.WebPageDslBuilder

class PageElementBuilderTest {
  private PageElementBuilder builder

  @Before
  void setUp() {
    builder = new PageElementBuilder()
  }

  @Test
  void test_build_simple_element_with_value() {
    WebDsl dsl = new WebPageDslBuilder().build("")

    HtmlElement element = builder.build(dsl.page) {
      p('this is my text')
    }

    assert element instanceof HtmlParagraph
    assert getAttributes(element) == [:]
    assert element.textContent == 'this is my text'
  }

  @Test
  void test_build_simple_element_with_id() {
    WebDsl dsl = new WebPageDslBuilder().build("")

    HtmlElement element = builder.build(dsl.page) {
      p(id:'myP')
    }

    assert element instanceof HtmlParagraph
    assert getAttributes(element) == [id: 'myP']
    assert element.textContent == ''
  }

  @Test
  void test_build_simple_element_with_id_and_text() {
    WebDsl dsl = new WebPageDslBuilder().build("")

    HtmlElement element = builder.build(dsl.page) {
      p(id:'myP', 'this is my text')
    }

    assert element instanceof HtmlParagraph
    assert getAttributes(element) == [id: 'myP']
    assert element.textContent == 'this is my text'
  }

  @Test
  void test_build_element_with_child() {
    WebDsl dsl = new WebPageDslBuilder().build("")

    HtmlElement element = builder.build(dsl.page) {
      div(id: 'myDiv') {
        p('this is my text')
      }
    }

    assert element instanceof HtmlDivision
    assert getAttributes(element) == [id: 'myDiv']

    List<HtmlElement> children = element.childElements.toList()
    assert children.size() == 1
    assert children[0] instanceof HtmlParagraph
    assert children[0].textContent == 'this is my text'
  }


  @Test
  void test_build_element_with_nested_children() {
    WebDsl dsl = new WebPageDslBuilder().build("")

    HtmlElement element = builder.build(dsl.page) {
      div(id: 'myDiv') {
        ol {
          li 'item 1'
          li 'item 2'
          li 'item 3'
        }
      }
    }

    assert element instanceof HtmlDivision
    assert getAttributes(element) == [id: 'myDiv']

    List<HtmlElement> children = element.childElements.toList()
    assert children.size() == 1
    assert children[0] instanceof HtmlOrderedList

    List<HtmlElement> grandChildren = children[0].childElements.toList()
    assert grandChildren.size() == 3
    assert grandChildren[0].textContent == 'item 1'
    assert grandChildren[1].textContent == 'item 2'
    assert grandChildren[2].textContent == 'item 3'
  }

  private Map<String, String> getAttributes(HtmlElement element) {
    element.attributesMap.collectEntries { String name, DomAttr domAttr -> [name, domAttr.value] }
  }
}