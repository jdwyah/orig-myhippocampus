package com.aavu.client.service.remote;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTTagServiceAsync {

	void createTagIfNonExistent(String tagName, AsyncCallback callback);

	//void removeTag(Tag selectedTag, AsyncCallback callback) throws PermissionDeniedException;;


	void match(String match, AsyncCallback call);

	
	void getTagStats(AsyncCallback callback);

}
