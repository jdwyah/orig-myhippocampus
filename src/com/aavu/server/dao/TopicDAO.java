package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Topic;

public interface TopicDAO {

	public abstract void save(Topic t);

	public abstract List<Topic> getAllTopics();

	public abstract List<Topic> getTopicsStarting(String match);

}