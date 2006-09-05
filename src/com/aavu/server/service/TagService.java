package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.exception.PermissionDeniedException;

public interface TagService {

	void save(Tag selectedTag);

	List<Tag> getTagsStarting(String match);

	void removeTag(Tag selectedTag) throws PermissionDeniedException;

	Tag getTagAddIfNew(String tagName);

	List<Tag> getAllTags();

}
