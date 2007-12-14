package com.aavu.client.exception;

import java.io.Serializable;

public class HippoBusinessException extends HippoException implements Serializable {

	public HippoBusinessException() {
	}

	public HippoBusinessException(String string) {
		super(string);
	}

	public HippoBusinessException(Exception e) {
		super(e);
	}

}
