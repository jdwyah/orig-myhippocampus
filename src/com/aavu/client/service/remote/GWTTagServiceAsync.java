package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.aavu.client.exception.PermissionDeniedException;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTTagServiceAsync {

	void getTagAddIfNew(String tagName, AsyncCallback callback);

	void removeTag(Tag selectedTag, AsyncCallback callback) throws PermissionDeniedException;;

	void getAllTags(AsyncCallback callback);

	void match(String match, AsyncCallback call);

	void saveTag(Tag selectedTag, AsyncCallback call);
}
