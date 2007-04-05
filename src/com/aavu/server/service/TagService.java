package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.PermissionDeniedException;

public interface TagService {

	Tag save(Tag selectedTag) throws HippoBusinessException;

	List<TopicIdentifier> getTagsStarting(String match);


	Tag createTagIfNonExistent(String tagName) throws HippoBusinessException;

	List<Tag> getAllTags();

	Tag getTagForName(String completeText);

	List<TagStat> getTagStats();

}
