package com.aavu.client.service.remote;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GWTTopicServiceAsync {

	//remember, these MUST BE VOID! returns
	//


	/**
	 * @deprecated this method is deprecated
	 */
	void getAllTopics(int startIndex, int maxCount, AsyncCallback callback);
	void getAllTopicIdentifiers(AsyncCallback callback);

	void save(Topic topic, AsyncCallback callback);
	//void getTopicsStarting(String match, AsyncCallback callback);
	void match(String match, AsyncCallback callback);	

	//void save(Topic topic, String[] seeAlsos, AsyncCallback callback);
	void getBlogTopics(int start, int numberPerScreen, AsyncCallback callback);
	void getTopicForName(String topicName, AsyncCallback callback);
	void getTopicIdsWithTag(Tag tag, AsyncCallback callback);
	void getTopicByID(long topicID, AsyncCallback back);	

}


