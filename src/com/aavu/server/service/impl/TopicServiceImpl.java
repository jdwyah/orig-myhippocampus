package com.aavu.server.service.impl;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;

public class TopicServiceImpl implements TopicService {
	
	private TopicDAO topicDAO;
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}

	
	public List<Topic> getAllTopics() {
		return topicDAO.getAllTopics(userService.getCurrentUser());
	}

	public List<Topic> getBlogTopics(int start, int numberPerScreen) {
		return topicDAO.getBlogTopics(userService.getCurrentUser(),start, numberPerScreen);
	}

	public Topic getForName(String string) {
		return topicDAO.getForName(userService.getCurrentUser(),string);
	}

	public List<Topic> getTopicsStarting(String match) {
		return topicDAO.getTopicsStarting(userService.getCurrentUser(),match);
	}

	public void save(Topic topic) {
		System.out.println("Topic Save Setting User "+userService.getCurrentUser());
		topic.setUser(userService.getCurrentUser());
		topicDAO.save(topic);
	}

}