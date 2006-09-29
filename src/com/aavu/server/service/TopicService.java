package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;

public interface TopicService {

	List<Topic> getAllTopics();
	
	List<TopicIdentifier> getAllTopicIdentifiers();

	Topic save(Topic topic);

	List<Topic> getTopicsStarting(String match);

	Topic getForName(String string);

	List<Topic> getBlogTopics(int start, int numberPerScreen);

	List<TopicIdentifier> getTopicIdsWithTag(Tag tag);

	Topic getForID(long topicID);

	List<TimeLineObj> getTimelineObjs();

}
