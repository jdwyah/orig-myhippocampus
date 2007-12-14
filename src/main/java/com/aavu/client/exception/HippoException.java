package com.aavu.client.exception;

import java.io.Serializable;

public class HippoException extends Exception implements Serializable {

	private String savedMessage;

	public HippoException() {
	}

	/**
	 * 
	 * @param string
	 */
	public HippoException(String string) {
		super(string);
		savedMessage = string;
	}

	public HippoException(Exception e) {
		super(e);
		savedMessage = e.getMessage();
	}

	// @Override
	public String getMessage() {
		return savedMessage;
	}


}
