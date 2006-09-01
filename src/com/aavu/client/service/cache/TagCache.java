package com.aavu.client.service.cache;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TagCache {

	private GWTTagServiceAsync tagService;

	public TagCache(GWTTagServiceAsync tagService) {
		this.tagService = tagService;
	}
	
	
	

	public void getTagAddIfNew(String itemText, AsyncCallback callback) {
		tagService.getTagAddIfNew(itemText, callback);
	}

	public void removeTag(Tag selectedTag, StdAsyncCallback callback) {
		tagService.removeTag(selectedTag, callback);		
	}

	public void saveTag(Tag selectedTag, StdAsyncCallback callback) {
		tagService.saveTag(selectedTag, callback);		
	}

	public void getAllTags(StdAsyncCallback callback) {
		tagService.getAllTags(callback);
	}

	public void match(String match, AsyncCallback call) {
		tagService.match(match, call);
	}
}
