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

import groovy.xml.StreamingMarkupBuilder

abstract class AbstractServerTest extends GroovyTestCase {
  public static final PORT = 8081
  protected server

  protected String defaultPage() {
    return "main"
  }

  def webdsl(Closure... closures) {
    webdsl null, closures
  }

  def webdsl(String page, Closure... closures) {
    new ServerRunner().withServer {
      WebDsl web = new WebDsl().for("http://localhost:$PORT/${page ?: defaultPage()}.html")
      def result
      closures.each { closure ->
        def c = closure.clone()
        c.delegate = [server: server]
        result = web.do(c)
      }
      result
    }
  }

  def test(Closure closure) {
    def obj = new Object()
    obj.metaClass.html = {
      def pageContents
      if (it instanceof String)
        pageContents = it
      else
        pageContents = new StreamingMarkupBuilder().bind(it).toString()
      server.addPage("/test.html", pageContents)
    }
    obj.metaClass.webdsl = {
      WebDsl web = new WebDsl().for("http://localhost:$PORT/test.html")
      def c = it.clone()
      c.delegate = [server: server]
      web.do(c)
    }
    closure.delegate = obj
    closure.resolveStrategy = Closure.DELEGATE_FIRST
    new ServerRunner().withServer closure
  }

  private class ServerRunner {
    def withServer(Closure closure) {
      server = new JettyRunner(port: PORT)
      server.start()
      def result
      try {
        result = closure()
      } finally {
        server.stop()
      }
      result
    }
  }
}