package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.User;
import com.aavu.client.exception.PermissionDeniedException;

public interface TagDAO {

	Tag getTag(User user,String tagName);

	void removeTag(User user,Tag selectedTag);

	List<Tag> getAllTags(User user);
	
	List<String> getTagsStarting(User user,String match);

	void save(Tag selectedTag);

	List<Tag> getPublicTags();
	
	List<TagStat> getTagStats(User user);
	
}
