package com.aavu.server.domain;

import java.io.Serializable;

public class MessageServiceReturn implements Serializable {

	private boolean success;
	private String message;
	private long topicID;
		
	
	public MessageServiceReturn(boolean success, String message,long topicID) {
		super();
		this.success = success;
		this.message = message;
		this.topicID = topicID;
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

	public long getTopicID() {
		return topicID;
	}

	public void setTopicID(long topicID) {
		this.topicID = topicID;
	}
	
}
