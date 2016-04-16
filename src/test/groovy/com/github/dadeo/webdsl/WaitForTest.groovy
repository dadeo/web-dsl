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
class WaitForTest {

  @Test
  void test_waitFor_attempts_exceeded() {
    webdsl {
      int i = 0

      def result = waitFor({ ++i; false }, 150)

      assert !result
      assert i >= 2
    }
  }

  @Test
  void test_waitFor_found_immediately_returns_closure_result() {
    webdsl {
      int i = 0

      def result = waitFor({ ++i; 8 }, 1000)

      assert result == 8
      assert i == 1
    }
  }

  @Test
  void test_waitFor_found_immediately_time_out_defaulted() {
    webdsl {
      def result = waitFor({ true })
      assert result
    }
  }

}