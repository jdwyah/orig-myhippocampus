package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.PermissionDeniedException;

public interface TagService {

	void save(Tag selectedTag) throws HippoBusinessException;

	List<String> getTagsStarting(String match);

	void removeTag(Tag selectedTag) throws PermissionDeniedException;

	Tag getTagAddIfNew(String tagName) throws HippoBusinessException;

	List<Tag> getAllTags();

	Tag getTagForName(String completeText);

	List<TagStat> getTagStats();
}
