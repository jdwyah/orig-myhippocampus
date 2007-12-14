package com.aavu.client.exception;

import java.io.Serializable;

public class PermissionDeniedException extends Exception implements Serializable {

	public PermissionDeniedException() {
	}

	public PermissionDeniedException(String string) {
		super(string);
	}

}
