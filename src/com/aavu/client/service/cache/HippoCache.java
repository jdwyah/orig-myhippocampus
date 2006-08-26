package com.aavu.client.service.cache;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.service.remote.GWTUserServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class HippoCache {

	
	public static final ReturnTypeConstant TOPIC = new ReturnTypeConstant(1);
	public static final ReturnTypeConstant TOPIC_LIST = new ReturnTypeConstant(2);
	public static final ReturnTypeConstant TAG = new ReturnTypeConstant(3);
	public static final ReturnTypeConstant TAG_LIST = new ReturnTypeConstant(4);
	
	private GWTTopicServiceAsync topicService;
	private GWTTagServiceAsync tagService;
	private GWTUserServiceAsync userService;
	
		
	private Map topicByName = new HashMap(); 
	private Map topicByID = new HashMap();
	
	
	public HippoCache(GWTTopicServiceAsync topicService, GWTTagServiceAsync tagService, GWTUserServiceAsync userService) {
		this.topicService = topicService;
		this.tagService = tagService;
		this.userService = userService;
				
	}

	public void getTopicForNameA(String topicName, AsyncCallback callback) {
		
		Topic t = (Topic) topicByName.get(topicName);
		
		if(t != null){
			System.out.println("hit "+topicName);
			callback.onSuccess(t);
		}else{
			System.out.println("miss "+topicName);
			topicService.getTopicForName(topicName, new TopicNameCallBack(TOPIC,callback));			
		}			
		
	}



	
	
	public Topic getTopicForName(String topicName) {
		return (Topic) topicByName.get(topicName);		
	}

	public Topic getTopicById(long id) {	
		return (Topic) topicByID.get(new Long(id));
	}
	
	
	/**
	 * a callback that wraps the real callback, but caches the returned topic
	 * 
	 * you know, closures/first order functions could make this wayyy cooler.
	 * 
	 * @author Jeff Dwyer
	 *
	 */
	private class TopicNameCallBack implements AsyncCallback {

		private AsyncCallback callback;
		private ReturnTypeConstant rtn;

		public TopicNameCallBack(ReturnTypeConstant rtn, AsyncCallback callback) {
			this.callback = callback;	
			this.rtn = rtn;
		}
		public void onFailure(Throwable caught) {
			callback.onFailure(caught);
		}
		public void onSuccess(Object result) {
			
			if(rtn == TOPIC){
				Topic t = (Topic) result;
				topicByName.put(t.getTitle(), t);
					
			}else if(rtn == TOPIC_LIST){
				Topic[] t = (Topic[]) result;
				for (int i = 0; i < t.length; i++) {
					Topic topic = t[i];
					topicByName.put(topic.getTitle(), topic);	
				}				
			}
			
			
			callback.onSuccess(result);
		}
		
	}


}
