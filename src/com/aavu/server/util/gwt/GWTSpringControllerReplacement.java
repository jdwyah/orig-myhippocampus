package com.aavu.server.util.gwt;



/*
 * Copyright 2006 George Georgovassilis <g.georgovassilis[at]gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Simple spring controller that merges GWT's {@link RemoteServiceServlet}, the
 * {@link Controller} and also implements the {@link RemoteService} interface so
 * as to be able to directly delegate RPC calls to extending classes.
 * 
 * @author g.georgovassilis
 * 
 */

public class GWTSpringControllerReplacement extends RemoteServiceServletReplacement implements
ServletContextAware, Controller, RemoteService {
	private static final long serialVersionUID = 5399966488983189122L;

	private static ThreadLocal<HttpServletRequest> servletRequest = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> servletResponse = new ThreadLocal<HttpServletResponse>();

	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	/**
	 * Return the request which invokes the service. Valid only if used in the dispatching thread.
	 * @return the servlet request
	 */
	public static HttpServletRequest getRequest(){
		return servletRequest.get();
	}

	/**
	 * Return the response which accompanies the request. Valid only if used in the dispatching thread.
	 * @return the servlet response
	 */
	public static HttpServletResponse getResponse(){
		return servletResponse.get();
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			servletRequest.set(request);
			servletResponse.set(response);
			doPost(request, response);
		} finally {
			servletRequest.set(null);
			servletResponse.set(null);
		}
		return null;
	}
}



