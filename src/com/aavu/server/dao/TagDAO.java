package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.RemoteService;

public interface TagDAO extends RemoteService {

	void addTag(Tag tag);

	Tag getTag(String tagName);

	void removeTag(String itemText);

	List<Tag> getAllTags();
	
	List<Tag> getTagsStarting(String match);

	void save(Tag selectedTag);
	
}
