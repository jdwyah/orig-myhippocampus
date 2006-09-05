package com.aavu.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PermissionDeniedException extends Exception implements IsSerializable {

	public PermissionDeniedException(){}
	
	public PermissionDeniedException(String string) {
		super(string);
	}

}
