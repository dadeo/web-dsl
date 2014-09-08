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

import org.mortbay.jetty.Handler
import org.mortbay.jetty.Server
import org.mortbay.jetty.handler.DefaultHandler
import org.mortbay.jetty.handler.HandlerList
import org.mortbay.jetty.handler.ResourceHandler
import org.mortbay.jetty.servlet.ServletHandler
import org.mortbay.jetty.servlet.ServletHolder

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

public class JettyRunner {
  static final int DEFAULT_PORT = 8081
  private Server server
  def params
  def path
  String methodType

  private ServletHandler handler

  JettyRunner(options = [:]) {
    options.port = options.port ?: DEFAULT_PORT
    options.webappsDirectory = options.webappsDirectory ?: "src/test/resources/webapps/test"

    ResourceHandler resource_handler = new ResourceHandler()
    resource_handler.setWelcomeFiles(["index.html"] as String[])
    resource_handler.setResourceBase(options.webappsDirectory)

    handler = new ServletHandler();

    addPage '/testit/*', {
      def paramlines = new HashMap(params)
          .sort()
          .collect { name, value ->
        "<tr><td>$name</td><td>${value.clone().sort().join(', ')}</td></tr>"
      }
          .join('\n')

      """
            <html>
              <head><title>TestServlet $path</title></head>
              <body>
                <h1>Hello TestServlet</h1>
                <p><a href="/main.html">main</a>
                <h2>Method Type</h2>
                <p id="methodType">$methodType</p>
                <h2>Parameters</h2>
                <table id="parameters">
                  <tr><th>Name</th><th>Value</th></tr>
                  ${paramlines}
                </table>
              </body>
            </html> """
    }


    server = new Server(options.port);
    HandlerList handlers = new HandlerList()
    handlers.setHandlers([resource_handler, handler, new DefaultHandler()] as Handler[])
    server.setHandler(handlers)
  }

  def start() {
    path = ""
    methodType = ""
    params = [:]
    server.start()
  }

  def stop() {
    server.stop()
  }

  def addPage(String mapping, String pageContent) {
    addPage mapping, { pageContent }
  }

  def addPage(String mapping, Closure pageContent) {
    def testServlet = [:]
    testServlet.doGet = { HttpServletRequest request, HttpServletResponse response ->
      path = request.getRequestURI().split("/")[-1]
      params = request.getParameterMap()
      methodType = request.method

      def c = pageContent.clone()
      c.delegate = this

      response.setContentType("text/html");
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println c()
    }
    testServlet.doPost = testServlet.doGet

    handler.addServletWithMapping(new ServletHolder(testServlet as HttpServlet), mapping)
  }

  static void main(args) {
    JettyRunner runner = new JettyRunner()
    runner.start()
    runner.server.join()

  }

  static def withServer(Closure closure) {
    withServer [:], closure
  }

  static def withServer(Map options, Closure closure) {
    JettyRunner server = new JettyRunner(options)
    server.start()
    try {
      closure()
    } finally {
      server.stop()
    }
  }

}
