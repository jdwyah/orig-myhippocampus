package com.aavu.server.dao;

import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;

public interface TopicDAO {

	public abstract Topic save(Topic t);
	
	public abstract List<TopicIdentifier> getAllTopicIdentifiers(User user);
	
	public abstract List<Topic> getAllTopics(User user);

	public abstract List<Topic> getTopicsStarting(User user,String match);

	public abstract Topic getForName(User user,String string);

	public abstract void tester();

	public abstract List<Topic> getBlogTopics(User user,int start, int numberPerScreen);

	public abstract List<TopicIdentifier> getTopicIdsWithTag(Tag tag,User user);

	public abstract Topic getForID(User currentUser, long topicID);

}