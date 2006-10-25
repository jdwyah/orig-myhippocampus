package com.aavu.client.gui;

public class StatusCode {

	public final static StatusCode SEND = new StatusCode("H-Send");
	public final static StatusCode FAIL = new StatusCode("H-Fail");
	public final static StatusCode SUCCESS = new StatusCode("H-Success");
	
	private String code;
	
	public StatusCode(String c){
		code = c;
	}
	public String getCode() {
		return code;
	}
	
}
