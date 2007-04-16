package com.aavu.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HippoSubscriptionException extends HippoBusinessException implements IsSerializable {

	public HippoSubscriptionException(){}

	public HippoSubscriptionException(String string) {
		super(string);
	}

}