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

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler
import org.mortbay.jetty.handler.ResourceHandler
import org.mortbay.jetty.handler.HandlerList
import org.mortbay.jetty.Handler
import javax.servlet.http.HttpServlet
import org.mortbay.jetty.servlet.ServletHolder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.mortbay.jetty.servlet.ServletHandler;

public class JettyRunner {

  private Server server
  def params
  def path

  JettyRunner(options) {
    if(options == null) {
      options = [port:8081]
    }
    def testServlet = [:]
    testServlet.doGet = {HttpServletRequest request, HttpServletResponse response ->
          path = request.getRequestURI().split("/")[-1]
          params = request.getParameterMap()
          response.setContentType("text/html");
          response.setStatus(HttpServletResponse.SC_OK);
          response.getWriter().println """
            <html>
              <head><title>TestServlet $path</title></head>
              <body>
                <h1>Hello TestServlet</h1>
                <p><a href="/main.html">main</a>
              </body>
            </html> """
        }
    testServlet.doPost = testServlet.doGet

    ResourceHandler resource_handler = new ResourceHandler()
    resource_handler.setWelcomeFiles(["index.html"] as String[])
    resource_handler.setResourceBase("src/test/resources/webapps/test")

    ServletHandler handler = new ServletHandler();
    handler.addServletWithMapping(new ServletHolder(testServlet as HttpServlet), "/testit/*");

    server = new Server(options.port);
    HandlerList handlers = new HandlerList()
    handlers.setHandlers([resource_handler, handler, new DefaultHandler()] as Handler[])
    server.setHandler(handlers)
  }

  def start() {
    path = ""
    params = [:]
    server.start()
  }

  def stop() {
    server.stop()
  }


  static void main(args) {
    JettyRunner runner = new JettyRunner()
    runner.start()
    runner.server.join()

  }
}
