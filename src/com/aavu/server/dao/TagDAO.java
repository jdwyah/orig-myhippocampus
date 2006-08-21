package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.User;

public interface TagDAO {

	Tag getTag(User user,String tagName);

	void removeTag(User user,Tag selectedTag);

	List<Tag> getAllTags(User user);
	
	List<Tag> getTagsStarting(User user,String match);

	void save(Tag selectedTag);
	
}
