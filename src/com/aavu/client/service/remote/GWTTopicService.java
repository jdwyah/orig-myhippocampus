package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;


public interface GWTTopicService extends RemoteService {


	TopicIdentifier[] getAllTopicIdentifiers();
	
	Topic save(Topic topic) throws HippoException;
	Topic[] save(Topic[] l) throws HippoException;
	
	//List getTopicsStarting(String match);//List<String>
	String[] match(String match);//List<String>

	//void save(Topic topic, String[] seeAlsos); 
	Topic getTopicByID(long topicID);
	
	Topic getTopicForName(String topicName);
		
	TopicIdentifier[] getTopicIdsWithTag(Tag tag);
	//List<TimeLineObj>
	List getTimelineObjs();

	//List<TopicIdentifier> 
	List getLinksTo(Topic topic) throws HippoException;
}
