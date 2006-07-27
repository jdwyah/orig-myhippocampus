package com.aavu.server.helper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public abstract class GWTSpringController extends RemoteServiceServlet
implements Controller {

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("req "+request+" "+request.getContentLength()+" "+request.getMethod()+" "+request.getServletPath()+"| "+request.getContextPath());
		init();
		System.out.println("res "+response);
		
		doPost(request, response);
		System.out.println("fin");
		return null;
	} 
}

