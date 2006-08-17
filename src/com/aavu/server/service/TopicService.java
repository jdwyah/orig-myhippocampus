package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.Topic;

public interface TopicService {

	List<Topic> getAllTopics();

	void save(Topic topic);

	List<Topic> getTopicsStarting(String match);

	Topic getForName(String string);

	List<Topic> getBlogTopics(int start, int numberPerScreen);

}
