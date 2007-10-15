package com.aavu.server.util;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * See http://groups.google.com/group/Google-Web-Toolkit/msg/f917fa87e1593234
 * 
 * @author Jeff Dwyer
 * 
 */
public class CacheControlFilter implements Filter {

	// remember, don't match 'foo.nocache.js'
	private static final String CACHE = ".cache.";
	private static final String IMAGE_JPG = ".jpg";
	private static final String IMAGE_PNG = ".png";
	private static final String IMAGE_GIF = ".gif";
	private static final String FILE_CSS = ".css";


	public void init(FilterConfig filterConfig) throws ServletException {
	}


	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws java.io.IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpServletResponse hres = (HttpServletResponse) response;
		String uri = hreq.getRequestURI();


		// Set cache headers for GWT cache, 1 year, could be forever
		if (uri.indexOf(CACHE) != -1) {
			hres.addHeader("Cache-Control", "max-age=31556926, public");
		}
		// cache for 1 hour
		if (uri.endsWith(IMAGE_PNG) || uri.endsWith(IMAGE_GIF) || uri.endsWith(IMAGE_JPG)) {
			hres.addHeader("Cache-Control", "max-age=3600, public");
		}
		// cache for 1 hour
		if (uri.endsWith(FILE_CSS)) {
			// cache for one hour
			hres.addHeader("Cache-Control", "max-age=3600, private");
		}

		chain.doFilter(request, response);
	}


	public void destroy() {
	}



}
