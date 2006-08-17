package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.server.domain.ServerSideUser;

public interface TopicDAO {

	public abstract void save(ServerSideUser user,Topic t);

	public abstract List<Topic> getAllTopics(ServerSideUser user);

	public abstract List<Topic> getTopicsStarting(ServerSideUser user,String match);

	public abstract Topic getForName(ServerSideUser user,String string);

	public abstract void tester();

	public abstract List<Topic> getBlogTopics(ServerSideUser user,int start, int numberPerScreen);

}