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
class TextAreaDslTest {

  @Test
  void test_value_textarea() {
    html {
      form {
        textarea(name: "name", id: "id")
      }
    }

    webdsl {
      $("#id").value = "MYVALUE"

      assert $("#id").value == "MYVALUE"
      assert $("#id").text == "MYVALUE"
    }
  }

  @Test
  void test_fillInWith_by_name_textarea() {
    html {
      form {
        textarea(name: "name", id: "id")
      }
    }

    webdsl {
      form {
        fillInWith([name: "MYVALUE"])
      }
      assert $("#id").value == "MYVALUE"
      assert $("#id").text == "MYVALUE"
    }
  }

  @Test
  void test_fillInWith_by_id_textarea() {
    html {
      form {
        textarea(id: "id", name: "name")
      }
    }

    webdsl {
      form {
        fillInWith([id: "MYVALUE"])
      }
      assert $("#id").value == "MYVALUE"
      assert $("#id").text == "MYVALUE"
    }
  }

  @Test
  void test_values_textarea() {
    html {
      form {
        textarea(name: "name", "MYVALUE")
      }
    }

    webdsl {
      form {
        assert [name: "MYVALUE"] == values()
      }
    }
  }

  @Test
  void test_valuesById_textarea() {
    html {
      form {
        textarea(id: "id", name: "name", "MYVALUE")
      }
    }

    webdsl {
      form {
        assert [id: "MYVALUE"] == valuesById()
      }
    }
  }
}