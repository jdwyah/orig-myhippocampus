package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;

public interface TopicDAO {

	public abstract void save(Topic t);

	public abstract List<Topic> getAllTopics(User user);

	public abstract List<Topic> getTopicsStarting(User user,String match);

	public abstract Topic getForName(User user,String string);

	public abstract void tester();

	public abstract List<Topic> getBlogTopics(User user,int start, int numberPerScreen);

}