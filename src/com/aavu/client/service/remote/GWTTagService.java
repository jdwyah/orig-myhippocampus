package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoException;
import com.aavu.client.exception.PermissionDeniedException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTagService extends RemoteService {

	Tag getTagAddIfNew(String tagName) throws HippoException;

	//void removeTag(Tag selectedTag) throws PermissionDeniedException;

	Tag[] getAllTags();
	
	String[] match(String match);//List<String>
	
	Tag saveTag(Tag selectedTag) throws HippoException;
	
	Tag getTagForName(String completeText);
	
	TagStat[] getTagStats() throws HippoException;
	
	Tag makeMeATag(Topic topic) throws HippoException;
}
