package com.aavu.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HippoInfrastructureException extends HippoException implements IsSerializable {

	public HippoInfrastructureException(){}
	
	public HippoInfrastructureException(String string) {
		super(string);
	}

}
