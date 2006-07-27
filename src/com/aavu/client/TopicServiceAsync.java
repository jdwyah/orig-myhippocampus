package com.aavu.client;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TopicServiceAsync {

	//remember, these MUST BE VOID! returns
	//
	
	void getAllTopics(int startIndex, int maxCount, AsyncCallback callback);
	void save(Topic topic, AsyncCallback callback);
	//void getTopicsStarting(String match, AsyncCallback callback);
	void match(String match, AsyncCallback callback);
}


