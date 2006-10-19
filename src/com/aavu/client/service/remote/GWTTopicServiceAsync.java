package com.aavu.client.service.remote;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTTopicServiceAsync {

	//remember, these MUST BE VOID! returns
	//



	void getAllTopicIdentifiers(AsyncCallback callback);

	void save(Topic topic, AsyncCallback callback);

	void save(Topic[] topics, AsyncCallback callback);
	
	//void getTopicsStarting(String match, AsyncCallback callback);
	void match(String match, AsyncCallback callback);	

	void getTopicForName(String topicName, AsyncCallback callback);
	void getTopicIdsWithTag(Tag tag, AsyncCallback callback);
	
	/*
	 * @Deprecated
	 */
	void getTopicByID(long topicID, AsyncCallback back);
	void getTimelineObjs(AsyncCallback callback);	

}


