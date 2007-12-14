package com.aavu.client.service.remote;

import com.aavu.client.domain.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTUserServiceAsync {

	void getCurrentUser(AsyncCallback<User> callback);

}
