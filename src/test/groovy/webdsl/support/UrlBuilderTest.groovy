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
package webdsl.support

import org.junit.Test


class UrlBuilderTest {

  @Test
  void test_build_absolute_path_scenarios() {
    assert new UrlBuilder('http://localhost:8080').build('http://myhost/css/my.css') == 'http://myhost/css/my.css'
    assert new UrlBuilder('http://localhost:8080/index.html').build('http://myhost/css/my.css') == 'http://myhost/css/my.css'
    assert new UrlBuilder('http://localhost:8080/index.html').build('https://myhost/css/my.css') == 'https://myhost/css/my.css'
  }

  @Test
  void test_build_relative_to_host_scenarios() {
    assert new UrlBuilder('http://localhost:8080/').build('css/my.css') == 'http://localhost:8080/css/my.css'
    assert new UrlBuilder('http://localhost:8080').build('css/my.css') == 'http://localhost:8080/css/my.css'
    assert new UrlBuilder('http://localhost:8080/index.html').build('css/my.css') == 'http://localhost:8080/css/my.css'
    assert new UrlBuilder('http://localhost:8080').build('/css/my.css') == 'http://localhost:8080/css/my.css'
    assert new UrlBuilder('http://localhost:8080/index.html').build('/css/my.css') == 'http://localhost:8080/css/my.css'

    assert new UrlBuilder('https://localhost:8080/').build('css/my.css') == 'https://localhost:8080/css/my.css'
    assert new UrlBuilder('https://localhost:8080').build('css/my.css') == 'https://localhost:8080/css/my.css'
    assert new UrlBuilder('https://localhost:8080/index.html').build('css/my.css') == 'https://localhost:8080/css/my.css'
    assert new UrlBuilder('https://localhost:8080').build('/css/my.css') == 'https://localhost:8080/css/my.css'
    assert new UrlBuilder('https://localhost:8080/index.html').build('/css/my.css') == 'https://localhost:8080/css/my.css'
  }

  @Test
  void test_build_base_path_contains_directory_scenarios() {
    assert new UrlBuilder('http://localhost:8080/buried/index').build('css/my.css') == 'http://localhost:8080/buried/css/my.css'
    assert new UrlBuilder('http://localhost:8080/buried/index').build('/css/my.css') == 'http://localhost:8080/css/my.css'

    assert new UrlBuilder('http://localhost:8080/buried/index.html').build('css/my.css') == 'http://localhost:8080/buried/css/my.css'
    assert new UrlBuilder('http://localhost:8080/buried/index.html').build('/css/my.css') == 'http://localhost:8080/css/my.css'

    assert new UrlBuilder('https://localhost:8080/buried/index').build('css/my.css') == 'https://localhost:8080/buried/css/my.css'
    assert new UrlBuilder('https://localhost:8080/buried/index').build('/css/my.css') == 'https://localhost:8080/css/my.css'

    assert new UrlBuilder('https://localhost:8080/buried/index.html').build('css/my.css') == 'https://localhost:8080/buried/css/my.css'
    assert new UrlBuilder('https://localhost:8080/buried/index.html').build('/css/my.css') == 'https://localhost:8080/css/my.css'
  }

  @Test
  void test_build_dot_dot_scenarios() {
    assert new UrlBuilder('http://localhost:8080/one/two/three').build('../css/my.css') == 'http://localhost:8080/one/css/my.css'
    assert new UrlBuilder('http://localhost:8080/one/two/three').build('../../css/my.css') == 'http://localhost:8080/css/my.css'
    assert new UrlBuilder('http://localhost:8080/one/two/three/four').build('../css/my.css') == 'http://localhost:8080/one/two/css/my.css'
    assert new UrlBuilder('http://localhost:8080/one/two/three/four').build('../../css/my.css') == 'http://localhost:8080/one/css/my.css'
    assert new UrlBuilder('http://localhost:8080/one/two/three/four').build('../../../css/my.css') == 'http://localhost:8080/css/my.css'

    assert new UrlBuilder('http://localhost:8080').build('../css/my.css') == 'http://localhost:8080/css/my.css'
    assert new UrlBuilder('http://localhost:8080').build('../../css/my.css') == 'http://localhost:8080/css/my.css'
  }

}