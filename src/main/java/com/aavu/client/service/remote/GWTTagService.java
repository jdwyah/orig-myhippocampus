package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.RemoteService;

public interface GWTTagService extends RemoteService {

	Topic createTagIfNonExistent(String tagName) throws HippoException;

	//void removeTag(Tag selectedTag) throws PermissionDeniedException;

	

	/**
     * @gwt.typeArgs <com.aavu.client.domain.dto.TopicIdentifier>
     */
	List match(String match);
	
	
	TagStat[] getTagStats() throws HippoException;
	
}
