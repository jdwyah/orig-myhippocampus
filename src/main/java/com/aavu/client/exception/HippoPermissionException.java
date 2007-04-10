package com.aavu.client.exception;

public class HippoPermissionException extends HippoBusinessException {
	
	public HippoPermissionException() {
		super("Can't Perform Operation With This User");
	}
}
