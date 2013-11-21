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

import java.util.regex.Matcher

class UrlBuilder {
  private String baseUrl

  UrlBuilder(String baseUrl) {
    if(baseUrl.endsWith('/'))
      this.baseUrl = baseUrl[0..-2]
    else
      if(baseUrl ==~ 'https?://[^/]+')
        this.baseUrl = baseUrl
      else
        this.baseUrl = baseUrl[0..<baseUrl.lastIndexOf('/')]
  }

  String build(String newUrl) {
    if(newUrl.startsWith('http'))
      newUrl
    else
      computeRelativePath(newUrl)
  }

  protected String computeRelativePath(String newUrl) {
    if(newUrl.startsWith('/'))
      extractHostAndPort(baseUrl) + newUrl
    else
      computeDotDotPaths(newUrl)
  }

  protected String computeDotDotPaths(String newUrl) {
    String newUrlPart = newUrl
    String baseUrlPart = baseUrl

    while(newUrlPart.startsWith('../')) {
      newUrlPart = newUrlPart[3..-1]
      baseUrlPart = trimDirectoryFromUrl(baseUrlPart)
    }

    baseUrlPart + '/' + newUrlPart
  }

  protected String trimDirectoryFromUrl(String url) {
    Matcher m = url =~ '(https?://[^/]*)(/.+)'
    if(m) {
      String host = m[0][1]
      List restParts = m[0][2].split('/') as List
      restParts.pop()
      host + restParts.join('/')
    }
    else
      url
  }

  private String extractHostAndPort(String baseUrl) {
    (baseUrl =~ 'https?://[^/]+')[0]
  }
}