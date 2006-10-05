package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;

public interface TopicService {

	List<TopicIdentifier> getAllTopicIdentifiers();

	Topic save(Topic topic);

	List<String> getTopicsStarting(String match);

	Topic getForName(String string);

	List<TopicIdentifier> getTopicIdsWithTag(Tag tag);

	Topic getForID(long topicID);

	List<TimeLineObj> getTimelineObjs();

}
