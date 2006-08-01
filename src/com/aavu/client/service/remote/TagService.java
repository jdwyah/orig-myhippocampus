package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface TagService extends RemoteService {

	void addTag(Tag tag);

	Tag getTag(String tagName);

	void removeTag(String itemText);

	Tag[] getAllTags();
	
	String[] match(String match);//List<String>
	
	void saveTag(Tag selectedTag);
	
}
