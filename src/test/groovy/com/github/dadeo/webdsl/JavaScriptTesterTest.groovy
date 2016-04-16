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


class JavaScriptTesterTest {

  @Test
  void test_javascript_inline_success() {
    new JavaScriptTester().test """
        var x = 5;
        assert(x === 5, "expected x to be 5");
      """
  }

  @Test
  void test_javascript_inline_failure() {
    try {
      new JavaScriptTester().test """
          var x = 5;
          assert(x === 4, "expected x to be 4");
          assert(x === 6, "expected x to be 6");
        """
      assert !"no failure occurred"
    } catch (AssertionError e) {
      assert e.message.contains('expected x to be 4')
      assert e.message.contains('expected x to be 6')
    }
  }

  @Test
  void test_javascript_external_script_success() {
    new JavaScriptTester(location: "src/test/resources/js/JavaScriptTesterTestScripts/good.js")
        .test()
  }

  @Test
  void test_javascript_external_script_failure() {
    try {
      new JavaScriptTester(location: "src/test/resources/js/JavaScriptTesterTestScripts/failure.js")
          .test()
      assert !"no failure occurred"
    } catch (AssertionError e) {
      assert e.message.contains('expected x to be 3')
    }
  }

  @Test
  void test_javascript_multiple_external_scripts_success() {
    new JavaScriptTester(locations: [
        "src/test/resources/js/JavaScriptTesterTestScripts/setXto3.js",
        "src/test/resources/js/JavaScriptTesterTestScripts/verifyXis3.js",
    ]).test()
  }

  @Test
  void test_javascript_multiple_external_scripts_failure() {
    try {
      new JavaScriptTester(locations: [
          "src/test/resources/js/JavaScriptTesterTestScripts/setXto3.js",
          "src/test/resources/js/JavaScriptTesterTestScripts/verifyXis4.js",
      ]).test()
      assert !"no failure occurred"
    } catch (AssertionError e) {
      assert e.message.contains('expected x to be 4')
    }
  }

  @Test
  void test_javascript_external_script_and_inline_success() {
    new JavaScriptTester(location: "src/test/resources/js/JavaScriptTesterTestScripts/setUpX.js").test """
        assert(x === 3, "expected x to be 3");
      """
  }

  @Test
  void test_javascript_external_script_and_inline_failure() {
    try {
      new JavaScriptTester(location: "src/test/resources/js/JavaScriptTesterTestScripts/setUpX.js").test """
          assert(x === 4, "expected x to be 4");
          assert(x === 6, "expected x to be 6");
        """
      assert !"no failure occurred"
    } catch (AssertionError e) {
      assert e.message.contains('expected x to be 4')
      assert e.message.contains('expected x to be 6')
    }
  }

  @Test
  void test_javascript_webdsl_works_success() {
    WebDsl webDsl = new JavaScriptTester().test """
        var x = 5;
        assert(x === 5, "expected x to be 5");
      """

    webDsl.do {
      assert $$('#results li.pass').text.contains("expected x to be 5")
    }
  }

  @Test
  void test_javascript_webdsl_works_throws_error() {
    try {
      WebDsl webDsl = new JavaScriptTester().test """
        var x = 5;
        assert(x === 5, "expected x to be 5");
      """

      webDsl.do {
        assert $$('#results li.pass').text.contains("expected x to be 4")
      }
      assert !"no failure occurred"
    } catch (AssertionError e) {
      assert e.message.contains('expected x to be 4')
    }
  }

}