package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;


public interface GWTTopicService extends RemoteService {


	void changeState(long topicID, boolean toIsland) throws HippoException;
		
	TopicIdentifier createNew(String title,Topic topicOrTagOrMeta) throws HippoBusinessException;
	void delete(Topic topic) throws HippoException;
	
	void deleteOccurrence(long id) throws HippoException;

	//List <LocationDTO>
	List getAllLocations() throws HippoException;
	
	List getAllMetas() throws HippoException;
		
	//List<DatedTopicIdentifier>
	List getAllTopicIdentifiers() throws HippoException;
	
	//List<TimeLineObj>
	//List getTimelineObjs(long tag_id) throws HippoException;
	
	//List<TopicIdentifier> 
	List getLinksTo(Topic topic) throws HippoException;
	//List<List <LocationDTO>>
	List getLocationsForTags(List shoppingList) throws HippoException;
	
	//List<TimeLineObj>
	List getTimeline() throws HippoException;

	//List<List<TimeLineObj>>
	List getTimelineWithTags(List shoppingList) throws HippoException;
	
	//void save(Topic topic, String[] seeAlsos); 
	Topic getTopicByID(long topicID) throws HippoException;
	
	Topic getTopicForName(String topicName);
	//List<FullTopicIdentifier>
	List getTopicIdsWithTag(long id) throws HippoException;
	
	//List<TopicIdentifier>
	//List<List<FullTopicIdentifier>>
	List getTopicsWithTags(List shoppingList) throws HippoException;
	
	MindTree getTree(MindTreeOcc occ) throws HippoException;

	WebLink getWebLinkForURL(String url) throws HippoException;
	//List getTopicsStarting(String match);//List<String>
	String[] match(String match);//List<String>
	
	void saveCommand(AbstractCommand command) throws HippoBusinessException;
	
	void saveTopicLocation(long tagId, long topicId, double xpct, double ypct) throws HippoException;
	
	MindTree saveTree(MindTree tree) throws HippoException;

	//List<SearchResult>
	List search(String searchString) throws HippoException;
}


