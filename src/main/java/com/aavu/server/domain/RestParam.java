package com.aavu.server.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class RestParam {
	private String name;
	private String val;

	public RestParam(String name, String val) {
		super();
		this.name = name;
		this.val = val;
	}

	public void appendMe(StringBuilder sb) throws UnsupportedEncodingException {
		sb.append("&");
		sb.append(name);
		sb.append("=");
		sb.append(URLEncoder.encode(val, "UTF-8"));
	}
}
