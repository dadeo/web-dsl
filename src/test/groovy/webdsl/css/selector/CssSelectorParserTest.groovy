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
package webdsl.css.selector

import groovy.mock.interceptor.MockFor
import org.junit.Before
import org.junit.Test
import webdsl.support.css.selector.ChildCssSelector
import webdsl.support.css.selector.CssSelectorParser
import webdsl.support.css.selector.ElementCssSelector
import webdsl.support.css.selector.ElementCssSelectorParser
import webdsl.support.css.selector.InsideCssSelector
import webdsl.support.css.selector.OrCssSelector
import webdsl.support.css.selector.ParentCssSelector
import webdsl.support.css.selector.PrecededBySiblingCssSelector
import webdsl.support.css.selector.StalkerCssSelector

class CssSelectorParserTest {
  private CssSelectorParser parser
  private MockFor mockElementSelectorParser

  @Before
  void setUp() {
    mockElementSelectorParser = new MockFor(ElementCssSelectorParser)
    parser = new CssSelectorParser()
  }

  @Test
  void test_parse_inside_single() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag"
      [new ElementCssSelector(id:"x")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag")) == "inside(element(x))"
  }

  @Test
  void test_parse_inside_multiple() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1 tag2"
      [new ElementCssSelector(id:"x")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1 tag2")) == "inside(element(x))"
  }

  @Test
  void test_parse_parent() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1>tag2")) == "parent(inside(element(x)), child(element(y)))"
  }

  @Test
  void test_parse_grandparent() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag3"
      [new ElementCssSelector(id:"z")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1>tag2>tag3")) == "parent(parent(inside(element(x)), child(element(y))), child(element(z)))"
  }
  @Test
  void test_parse_stalker_simple() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1+tag2")) == "stalker(inside(element(x)), child(element(y)))"
  }

  @Test
  void test_parse_stalker_nested() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag3"
      [new ElementCssSelector(id:"z")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1+tag2+tag3")) == "stalker(stalker(inside(element(x)), child(element(y))), child(element(z)))"
  }

  @Test
  void test_parse_or_single() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1,tag2")) == "or(inside(element(x)), inside(element(y)))"
  }

  @Test
  void test_parse_or_multiple() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag3"
      [new ElementCssSelector(id:"z")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1,tag2,tag3")) == "or(inside(element(x)), inside(element(y)), inside(element(z)))"
  }

  @Test
  void test_parse_or_lowest_precedence_take_1() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag3"
      [new ElementCssSelector(id:"z")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1+tag2,tag3")) == "or(stalker(inside(element(x)), child(element(y))), inside(element(z)))"
  }

  @Test
  void test_parse_or_lowest_precedence_take_2() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag3"
      [new ElementCssSelector(id:"z")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1,tag2+tag3")) == "or(inside(element(x)), stalker(inside(element(y)), child(element(z))))"
  }

  @Test
  void test_parse_precededBySibling_single() {
    mockElementSelectorParser.demand.parse {
      assert it == "tag1"
      [new ElementCssSelector(id:"x")]
    }
    mockElementSelectorParser.demand.parse {
      assert it == "tag2"
      [new ElementCssSelector(id:"y")]
    }

    parser.elementSelectorParser = mockElementSelectorParser.proxyInstance()

    assert toString(parser.parse("tag1~tag2")) == "precededBySibling(inside(element(x)), child(element(y)))"
  }

  private toString(selector) {
    switch(selector.class) {
      case ElementCssSelector:
        return "element($selector.id)"
      case InsideCssSelector:
        return "inside(${selector.cssSelectors.collect { toString(it) }.join(', ')})"
      case ParentCssSelector:
        return "parent(${toString(selector.parent)}, ${toString(selector.child)})"
      case ChildCssSelector:
        return "child(${toString(selector.exactMatchSelector)})"
      case StalkerCssSelector:
        return "stalker(${toString(selector.stalked)}, ${toString(selector.stalker)})"
      case OrCssSelector:
        return "or(${selector.cssSelectors.collect { toString(it) }.join(', ')})"
      case PrecededBySiblingCssSelector:
        return "precededBySibling(${toString(selector.sibling)}, ${toString(selector.selector)})"
      default:
        throw new RuntimeException("Selector doesn't have toString() implementation")
    }
  }
}