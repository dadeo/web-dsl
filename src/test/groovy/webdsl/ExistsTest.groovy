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
package webdsl

import org.junit.Test

@Mixin(NonServerMixin)
class ExistsTest {

  @Test
  void test_nothing_exists() {
    html {
    }

    webdsl {
      assert !exists('search')
    }
  }

  @Test
  void test_no_math() {
    html {
      a(id: "myId", href:'http://something', 'MYLINK')
      input(name: "name", id: "id", type: 'text', value: 'MYVALUE')
    }

    webdsl {
      assert !exists('search')
    }
  }

  @Test
  void test_exists_by_css_selector() {
    html {
      input(name: "name", id: "id", type: 'text', value: 'MYVALUE')
    }

    webdsl {
      assert exists('[value=MYVALUE]')
    }
  }

  @Test
  void test_exists_by_name() {
    html {
      input(name: "search", id: "id", type: 'text', value: 'MYVALUE')
    }

    webdsl {
      assert exists('search')
    }
  }

  @Test
  void test_exists_by_id() {
    html {
      input(name: "name", id: "search", type: 'text', value: 'MYVALUE')
    }

    webdsl {
      assert exists('search')
    }
  }

  @Test
  void test_exists_by_value() {
    html {
      input(name: "name", id: "id", type: 'text', value: 'search')
    }

    webdsl {
      assert exists('search')
    }
  }

  @Test
  void test_exists_by_href() {
    html {
      a(id: "myId", href:'search', 'MYLINK')
    }

    webdsl {
      assert exists('search')
    }
  }

  @Test
  void test_exists_by_text() {
    html {
      a(id: "myId", href:'http://something', 'search')
    }

    webdsl {
      assert exists('search')
    }
  }

}