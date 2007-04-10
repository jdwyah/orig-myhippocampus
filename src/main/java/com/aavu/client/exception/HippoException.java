package com.aavu.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HippoException extends Exception implements IsSerializable {

	private String savedMessage; 
	
	public HippoException(){}
	
	/**
	 * We need to save the message string ourselves since Exception is not IsSerializable
	 * I really wonder what Google's impetus for IsSerializable vs Serializable was. bit of
	 * a pain...
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

	//@Override
	public String getMessage() {		
		return savedMessage;
	}
	
	
}
