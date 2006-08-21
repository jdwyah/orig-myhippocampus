package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTagService extends RemoteService {

	Tag getTagAddIfNew(String tagName);

	void removeTag(Tag selectedTag);

	Tag[] getAllTags();
	
	String[] match(String match);//List<String>
	
	void saveTag(Tag selectedTag);
	
}
