package com.aavu.client.service.remote;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTUserService extends RemoteService {

	User getCurrentUser() throws HippoBusinessException;
	
}
