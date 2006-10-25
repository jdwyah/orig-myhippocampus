package com.aavu.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HippoException extends Exception implements IsSerializable {

	public HippoException(){}
	
	public HippoException(String string) {
		super(string);
	}

}
