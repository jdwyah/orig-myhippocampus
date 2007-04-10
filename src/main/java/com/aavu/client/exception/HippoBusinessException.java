package com.aavu.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HippoBusinessException extends HippoException implements IsSerializable {

	public HippoBusinessException(){}
	
	public HippoBusinessException(String string) {
		super(string);
	}

}
