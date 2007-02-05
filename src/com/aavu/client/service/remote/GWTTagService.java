package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTagService extends RemoteService {

	Tag createTagIfNonExistent(String tagName) throws HippoException;

	//void removeTag(Tag selectedTag) throws PermissionDeniedException;

	Tag[] getAllTags();
	
	String[] match(String match);//List<String>
	
	Tag getTagForName(String completeText);
	
	TagStat[] getTagStats() throws HippoException;
	
	Tag makeMeATag(Topic topic) throws HippoException;
}
