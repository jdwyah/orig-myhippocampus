package com.aavu.server.domain;

import java.io.Serializable;

public class MessageServiceReturn implements Serializable {

	private boolean success;
	private String message;
		
	public MessageServiceReturn(){}
	
	public MessageServiceReturn(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
