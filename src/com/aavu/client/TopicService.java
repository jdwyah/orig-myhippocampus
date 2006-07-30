package com.aavu.client;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.RemoteService;


public interface TopicService extends RemoteService {

	Topic[] getAllTopics(int startIndex, int maxCount);
	void save(Topic topic);
	//List getTopicsStarting(String match);//List<String>
	String[] match(String match);//List<String>

	void save(Topic topic, String[] seeAlsos); 
	
}
