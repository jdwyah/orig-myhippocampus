package com.aavu.client.exception;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HippoPermissionException extends HippoBusinessException  implements IsSerializable {
	
	public HippoPermissionException() {
		super("Can't Perform Operation With This User");
	}
}
