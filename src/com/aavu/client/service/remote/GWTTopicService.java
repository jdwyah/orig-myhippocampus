package com.aavu.client.service.remote;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.RemoteService;


public interface GWTTopicService extends RemoteService {

	Topic[] getAllTopics(int startIndex, int maxCount);
	void save(Topic topic);
	//List getTopicsStarting(String match);//List<String>
	String[] match(String match);//List<String>

	//void save(Topic topic, String[] seeAlsos); 

	Topic[] getBlogTopics(int start, int numberPerScreen);
}
