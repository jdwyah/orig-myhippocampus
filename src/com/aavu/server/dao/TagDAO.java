package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TagStat;

public interface TagDAO {

	Tag getTag(User user,String tagName);

	void removeTag(User user,Tag selectedTag);

	List<Tag> getAllTags(User user);
	
	List<String> getTagsStarting(User user,String match);

	List<Tag> getPublicTags();
	
	List<TagStat> getTagStats(User user);

	
}
