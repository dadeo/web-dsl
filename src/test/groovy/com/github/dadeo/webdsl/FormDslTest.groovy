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

@Mixin(NonServerMixin)
class FormDslTest {

  @Test
  void test_fillInWith_works_when_more_than_one_dsl_object() {
    html {
      form {
        textarea(name: "name1", id: "id")
      }
    }

    WebDsl dsl1 = createBuilder().build()

    html {
      form {
        textarea(name: "name2", id: "id", "def")
      }
    }

    WebDsl dsl2 = createBuilder().build()

    dsl1.do {
      form.fillInWith([name1: "abc"])

      assert form.values() == [name1: "abc"]
    }

    dsl2.do {
      assert form.values() == [name2: "def"]
    }
  }

}