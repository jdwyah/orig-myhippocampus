package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.subjects.Subject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTExternalServiceAsync {

	// remember, these MUST BE VOID! returns
	//

	/**
	 * Would like to pass Class, but it's not IsSerializable
	 * 
	 */
	void lookup(Subject type, String matchString, AsyncCallback<List<Subject>> callback);

	void addDeliciousTags(String username, String password, AsyncCallback<Void> callback);
}
