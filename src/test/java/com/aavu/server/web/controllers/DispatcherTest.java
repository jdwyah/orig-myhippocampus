package com.aavu.server.web.controllers;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

public class DispatcherTest extends AbstractBaseTestClass {



	private HandlerMapping handlerMapping;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		handlerMapping = (HandlerMapping) ctx.getBean("urlMapping");

	}


	public void testHandling() throws Exception {
		// ServletForwardingController
		HandlerExecutionChain chain = handlerMapping.getHandler(getRequest("/site/index.html"));
		System.out.println("chain " + chain);

		chain = handlerMapping.getHandler(getRequest("/site/foo"));
		System.out.println("chain " + chain);

		chain = handlerMapping.getHandler(getRequest("/index.html"));
		System.out.println("chain " + chain);

		chain = handlerMapping.getHandler(getRequest("/com.aavu.HippoTest/HippoTest.html"));
		System.out.println("chain " + chain);

		chain = handlerMapping.getHandler(getRequest("/decorators/default.ftl"));
		System.out.println("chain " + chain);

		if (chain != null) {
			System.out.println("handler " + chain.getHandler());
		}
	}

	public MockHttpServletRequest getRequest(String path) {
		return getRequest("GET", path);
	}

	public MockHttpServletRequest getRequest(String type, String path) {



		MockHttpServletRequest req = new MockHttpServletRequest(new MockServletContext(""));
		req.setMethod(type);
		req.setContextPath("");
		req.setRequestURI(path);
		// req.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, ctx
		// .getBean("localeResolver"));
		req.setAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE, ctx);
		assertEquals("en", req.getLocale().getLanguage());

		// UrlPathHelper helper = new UrlPathHelper();
		// System.out.println("LOKUP "+helper.getLookupPathForRequest(req));

		return req;
	}
}
