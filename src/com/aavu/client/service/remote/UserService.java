package com.aavu.client.service.remote;

import com.aavu.client.domain.User;
import com.google.gwt.user.client.rpc.RemoteService;

public interface UserService extends RemoteService {

	User getCurrentUser();
	
}
