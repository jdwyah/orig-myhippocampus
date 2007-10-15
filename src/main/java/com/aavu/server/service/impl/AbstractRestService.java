package com.aavu.server.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Vector;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.domain.RestParam;

public class AbstractRestService {
	private static final Logger log = Logger.getLogger(AbstractRestService.class);

	private HttpClient client;
	private String userAgent;
	private String authURL;
	private int waitBetweenReq = -1;

	public AbstractRestService() {
		// Create an instance of HttpClient.
		client = new HttpClient();
	}

	public AbstractRestService(String userAgent, String authURL, int waitBetweenReq) {
		this();

		this.waitBetweenReq = waitBetweenReq;
		this.userAgent = userAgent;
		this.authURL = authURL;
	}

	protected Document xmlRESTReq(String baseURL, Vector<RestParam> params) throws IOException,
			DocumentException, HippoBusinessException {
		return xmlRESTReq(baseURL, params, null, null);
	}

	protected Document xmlRESTReq(String baseURL, Vector<RestParam> params, String username,
			String password) throws IOException, DocumentException, HippoBusinessException {
		StringBuilder url = new StringBuilder(baseURL);

		for (RestParam p : params) {
			p.appendMe(url);
		}



		// Create a method instance.
		GetMethod method = new GetMethod(url.toString());

		// Provide custom retry handler is necessary
		// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		// new DefaultHttpMethodRetryHandler(1, false));

		method.getParams().setParameter(HttpMethodParams.USER_AGENT, userAgent);

		if (authURL != null) {

			Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
			client.getState().setCredentials(new AuthScope(authURL, AuthScope.ANY_PORT),
					defaultcreds);


			client.getParams().setAuthenticationPreemptive(true);
			// client.getState().setCredentials(AuthScope.ANY, defaultcreds);
		}

		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				throw new HippoBusinessException("Request Failed: " + method.getStatusLine());
			}

			// Read the response body.

			InputStream in = method.getResponseBodyAsStream();
			SAXReader reader = new SAXReader();
			Document response = reader.read(in);
			in.close();

			return response;

		} finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	protected Document getDocumentFromString(String xmlString) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read(new StringReader(xmlString));
	}

}
