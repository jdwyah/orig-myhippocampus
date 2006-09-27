package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;

public interface TopicDAO {

	Topic save(Topic t);

	List<TopicIdentifier> getAllTopicIdentifiers(User user);

	List<Topic> getAllTopics(User user);

	List<Topic> getTopicsStarting(User user,String match);

	Topic getForName(User user,String string);

	void tester();

	List<Topic> getBlogTopics(User user,int start, int numberPerScreen);

	List<TopicIdentifier> getTopicIdsWithTag(Tag tag,User user);

	Topic getForID(User currentUser, long topicID);

	List<Topic> getTimeline(User user,Tag tag, MetaDate metaDate);
}