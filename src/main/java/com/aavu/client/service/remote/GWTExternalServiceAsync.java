package com.aavu.client.service.remote;

import com.aavu.client.domain.subjects.Subject;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTExternalServiceAsync {

	// remember, these MUST BE VOID! returns
	//

	/**
	 * Would like to pass Class, but it's not IsSerializable
	 * 
	 * rtn List<Subject>
	 */
	void lookup(Subject type, String matchString, AsyncCallback callback);

	void addDeliciousTags(String username, String password, AsyncCallback callback);
}
