package com.aavu.client.exception;

import java.io.Serializable;

public class HippoPermissionException extends HippoBusinessException implements Serializable {

	public HippoPermissionException() {
		super("Can't Perform Operation With This User");
	}

	public HippoPermissionException(String string) {
		super("Can't Perform Operation With User " + string);
	}
}
