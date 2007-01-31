package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;


public interface GWTTopicService extends RemoteService {


	TopicIdentifier[] getAllTopicIdentifiers() throws HippoException;
	
	Topic save(Topic topic) throws HippoException;
	Topic[] save(Topic[] l) throws HippoException;
	
	//List getTopicsStarting(String match);//List<String>
	String[] match(String match);//List<String>

	//void save(Topic topic, String[] seeAlsos); 
	Topic getTopicByID(long topicID) throws HippoException;
	
	Topic getTopicForName(String topicName);
		
	FullTopicIdentifier[] getTopicIdsWithTag(long id) throws HippoException;
	//List<TimeLineObj>
	List getTimelineObjs() throws HippoException;

	//List<TopicIdentifier> 
	List getLinksTo(Topic topic) throws HippoException;
	
	//List<SearchResult>
	List search(String searchString) throws HippoException;
	
	MindTree getTree(MindTreeOcc occ) throws HippoException;
	MindTree saveTree(MindTree tree) throws HippoException;
	
	void delete(Topic topic) throws HippoException;
	
	void saveTopicLocation(long tagId, long topicId, double xpct, double ypct) throws HippoException;
}
