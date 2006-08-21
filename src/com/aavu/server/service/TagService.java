package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Tag;

public interface TagService {

	void save(Tag selectedTag);

	List<Tag> getTagsStarting(String match);

	void removeTag(Tag selectedTag);

	Tag getTagAddIfNew(String tagName);

	List<Tag> getAllTags();

}
