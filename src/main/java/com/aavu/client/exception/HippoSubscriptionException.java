package com.aavu.client.exception;

import java.io.Serializable;

public class HippoSubscriptionException extends HippoBusinessException implements Serializable {

	public HippoSubscriptionException() {
	}

	public HippoSubscriptionException(String string) {
		super(string);
	}

}