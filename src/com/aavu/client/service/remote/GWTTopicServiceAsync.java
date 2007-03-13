package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTTopicServiceAsync {

	//remember, these MUST BE VOID! returns
	//



	void getAllTopicIdentifiers(AsyncCallback callback);

	
	//void getTopicsStarting(String match, AsyncCallback callback);
	void match(String match, AsyncCallback callback);	

	void getTopicForName(String topicName, AsyncCallback callback);
	void getTopicIdsWithTag(long id, AsyncCallback callback);
	
	/*
	 * @Deprecated
	 */	
	void getTopicByID(long topicID, AsyncCallback back);
	void getTimelineObjs(long meta_id, AsyncCallback callback);	
	
	//List<TopicIdentifier> 
	void getLinksTo(Topic topic, AsyncCallback callback);
	
	//List<SearchResult>
	void search(String searchString,AsyncCallback callback);
	
	//MindTree
	void getTree(MindTreeOcc occ,AsyncCallback callback);
	//MindTree 
	void saveTree(MindTree tree,AsyncCallback callback);

	void delete(Topic topic, AsyncCallback callback);

	void saveTopicLocation(long tagId, long topicId, double xpct, double ypct, AsyncCallback callback);

	void saveCommand(AbstractCommand command, AsyncCallback callback);
	void createNew(String title,boolean isIsland, AsyncCallback callback);
	
	//Weblink
	void getWebLinkForURL(String url, AsyncCallback callback);
	
	void changeState(long topicID, boolean toIsland,AsyncCallback callback);


	void deleteOccurrence(long id, AsyncCallback callback);

	void getAllMetas(AsyncCallback callback);
}


