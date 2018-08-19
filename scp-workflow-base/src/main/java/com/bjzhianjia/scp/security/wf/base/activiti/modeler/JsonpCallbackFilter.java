/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.security.wf.base.activiti.modeler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class JsonpCallbackFilter implements Filter {

  private static Logger log = LoggerFactory.getLogger(JsonpCallbackFilter.class);

  public void init(FilterConfig fConfig) throws ServletException {}

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    Map<String, String[]> parms = httpRequest.getParameterMap();

    if (parms.containsKey("callback")) {
      if (log.isDebugEnabled()) {
        log.debug("Wrapping response with JSONP callback '" + parms.get("callback")[0] + "'");
      }

      OutputStream out = httpResponse.getOutputStream();

      GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);

      chain.doFilter(request, wrapper);

      //handles the content-size truncation
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
      outputStream.write(new String(parms.get("callback")[0] + "(").getBytes() );
      outputStream.write(wrapper.getData());
      outputStream.write(new String(");").getBytes());
      byte jsonpResponse[] = outputStream.toByteArray( );

      wrapper.setContentType("text/javascript;charset=UTF-8");
      wrapper.setContentLength(jsonpResponse.length);

      out.write(jsonpResponse);

      out.close();
    
    } else {
      chain.doFilter(request, response);
    }
  }

  public void destroy() {}
}
