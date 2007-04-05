package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTagService extends RemoteService {

	Tag createTagIfNonExistent(String tagName) throws HippoException;

	//void removeTag(Tag selectedTag) throws PermissionDeniedException;

	Tag[] getAllTags();
	
	//List<TopicIdentifier>
	List match(String match);
	
	Tag getTagForName(String completeText);
	
	TagStat[] getTagStats() throws HippoException;
	
}
