package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;

public interface TopicDAO {

	Topic save(Topic t);

	List<TopicIdentifier> getAllTopicIdentifiers(User user);

	List<String> getTopicsStarting(User user,String match);

	Topic getForName(User user,String string);

	void tester();

	List<TopicIdentifier> getTopicIdsWithTag(Tag tag,User user);

	Topic getForID(User currentUser, long topicID);

	List<TimeLineObj> getTimeline(User user);
	
}