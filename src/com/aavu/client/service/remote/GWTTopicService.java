package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.RemoteService;


public interface GWTTopicService extends RemoteService {

	Topic[] getAllTopics(int startIndex, int maxCount);
	Topic save(Topic topic);
	//List getTopicsStarting(String match);//List<String>
	Topic[] match(String match);//List<String>

	//void save(Topic topic, String[] seeAlsos); 
	Topic getTopicForName(String topicName);
	Topic[] getBlogTopics(int start, int numberPerScreen);
	Topic[] getTopicsWithTag(Tag tag);
}
