package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.mapper.MindTree;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTTopicServiceAsync {

	//remember, these MUST BE VOID! returns
	//

	void changeState(long topicID, boolean toIsland,AsyncCallback callback);

	
	void createNew(String title,Topic topicOrTagOrMeta, AsyncCallback callback);	

	void delete(Topic topic, AsyncCallback callback);
	void deleteOccurrence(long id, AsyncCallback callback);
	
	void getAllMetas(AsyncCallback callback);
	
	//List<DatedTopicIdentifiers>
	void getAllTopicIdentifiers(int start, int max, String startStr, AsyncCallback callback);	
	
	//List<TopicIdentifier> 
	void getLinksTo(Topic topic, AsyncCallback callback);
	
	//List TimeLineObj
	//void getTimelineObjs(long tag_id, AsyncCallback callback);
	
	//List<TimeLineObj>
	void getTimeline(AsyncCallback callback);
	
	//List<List<TimeLineObj>>
	void getTimelineWithTags(List shoppingList, AsyncCallback callback);
	
	/*
	 * @Deprecated
	 */	
	void getTopicByID(long topicID, AsyncCallback back);
	void getTopicForName(String topicName, AsyncCallback callback);

	void getTopicIdsWithTag(long id, AsyncCallback callback);

	//List<TopicIdentifier>
	void getTopicsWithTags(List shoppingList, AsyncCallback callback);

	//MindTree
	void getTree(MindTreeOcc occ,AsyncCallback callback);

	//LinkAndUser
	void getWebLinkForURLAndUser(String url, AsyncCallback callback);
	
	//void getTopicsStarting(String match, AsyncCallback callback);
	void match(String match, AsyncCallback callback);
	
	void saveCommand(AbstractCommand command, AsyncCallback callback);


	void saveTopicLocation(long tagId, long topicId, int lat, int lng, AsyncCallback callback);

	//MindTree 
	void saveTree(MindTree tree,AsyncCallback callback);

	//List<SearchResult>
	void search(String searchString,AsyncCallback callback);


	void getAllLocations(AsyncCallback callback);


	void getLocationsForTags(List shoppingList, AsyncCallback callback);
}


