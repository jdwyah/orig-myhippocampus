package com.aavu.client.exception;

import java.io.Serializable;

public class HippoInfrastructureException extends HippoException implements Serializable {

	public HippoInfrastructureException() {
	}

	public HippoInfrastructureException(String string) {
		super(string);
	}

	public HippoInfrastructureException(Exception e) {
		super(e);
	}

}
