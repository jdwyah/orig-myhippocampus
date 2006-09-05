package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.aavu.client.exception.PermissionDeniedException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTagService extends RemoteService {

	Tag getTagAddIfNew(String tagName);

	void removeTag(Tag selectedTag) throws PermissionDeniedException;

	Tag[] getAllTags();
	
	Tag[] match(String match);//List<String>
	
	void saveTag(Tag selectedTag);
	
}
