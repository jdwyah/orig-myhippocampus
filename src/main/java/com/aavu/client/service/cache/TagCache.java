package com.aavu.client.service.cache;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TagCache {

	private GWTTagServiceAsync tagService;

	public TagCache(GWTTagServiceAsync tagService) {
		this.tagService = tagService;
	}
	
	
	

	public void getTagAddIfNew(String itemText, AsyncCallback callback) {
		tagService.createTagIfNonExistent(itemText, callback);
	}

	public void getAllTags(StdAsyncCallback callback) {
		tagService.getAllTags(callback);
	}

	public void match(String match, AsyncCallback call) {
		tagService.match(match, call);
	}

	public void getTagForName(String completeText, AsyncCallback call) {
		tagService.getTagForName(completeText, call);
	}




	public void getTagStats(AsyncCallback callback) {		
		tagService.getTagStats(callback);
	}

}
