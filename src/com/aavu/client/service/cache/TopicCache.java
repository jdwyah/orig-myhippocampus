package com.aavu.client.service.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TopicCache {

	public static final ReturnTypeConstant TOPIC = new ReturnTypeConstant(1);
	public static final ReturnTypeConstant TOPIC_LIST = new ReturnTypeConstant(2);

	private Map topicByName = new HashMap(); 
	private Map topicByID = new HashMap();
	private GWTTopicServiceAsync topicService;

	private List topicIdentifiers = new ArrayList();

	public TopicCache(GWTTopicServiceAsync topicService) {
		this.topicService = topicService;
	}



	public Topic getTopicForName(String topicName) {
		return (Topic) topicByName.get(topicName);		
	}

	public Topic getTopicById(long id) {	
		return (Topic) topicByID.get(new Long(id));
	}



	public void getTopic(TopicIdentifier ident, StdAsyncCallback callback) {				
		Topic t = (Topic) topicByID.get(new Long(ident.getTopicID()));		
		if(t != null){
			System.out.println("ti - hit "+ident.getTopicTitle());
			callback.onSuccess(t);
		}else{
			System.out.println("ti - miss "+ident.getTopicTitle());
			topicService.getTopicForName(ident.getTopicTitle(), new TopicNameCallBack(TOPIC,callback));			
		}			
	}


	public void getTopicByIdA(long topicID, AsyncCallback callback) {

		Topic t = (Topic) topicByID.get(new Long(topicID));

		if(t != null){
			System.out.println("hit "+topicID);
			callback.onSuccess(t);
		}else{
			System.out.println("miss "+topicID);
			topicService.getTopicByID(topicID, new TopicNameCallBack(TOPIC,callback));			
		}			

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


	/**
	 * return the identifier list or set it if 1st call
	 * 
	 * @param callback
	 */
	public void getAllTopicIdentifiers(final StdAsyncCallback callback) {
		if(topicIdentifiers.size() != 0){
			callback.onSuccess(topicIdentifiers);			
		} else {
			topicService.getAllTopicIdentifiers(new AsyncCallback(){
				public void onSuccess(Object result) {
					TopicIdentifier[] topicIdents = (TopicIdentifier[]) result;
					System.out.println("rec "+topicIdents.length);
					topicIdentifiers = new ArrayList();
					for (int i = 0; i < topicIdents.length; i++) {
						System.out.println("adding! "+i);
						topicIdentifiers.add(topicIdents[i]);						
					}					
					callback.onSuccess(topicIdentifiers);
				}
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}});		
		}
	}


	/**
	 * @deprecated
	 * 
	 */
	public void getAllTopics(int i, int j, StdAsyncCallback callback) {		
		topicService.getAllTopics(i, j, new TopicNameCallBack(TOPIC_LIST,callback));		
	}

	public void getBlogTopics(int start, int numberPerScreen, StdAsyncCallback callback) {
		topicService.getBlogTopics(start, numberPerScreen, callback);		
	}

	public void save(Topic topic2, AsyncCallback callback) {
		System.out.println("client saving "+topic2.toPrettyString());

		topicService.save(topic2, callback);
	}


	public void match(String match, AsyncCallback call) {
		topicService.match(match, call);		
	}


	public void getTopicsWithTag(Tag tag, StdAsyncCallback callback) {
		topicService.getTopicIdsWithTag(tag,callback);
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
				System.out.println("single adding to cache "+t.getTitle());
				topicByName.put(t.getTitle(), t);
				topicByID.put(new Long(t.getId()), t);

			}else if(rtn == TOPIC_LIST){
				Topic[] t = (Topic[]) result;
				//	if(t.)
				for (int i = 0; i < t.length; i++) {					
					Topic topic = t[i];
					System.out.println("list adding to cache "+topic.getTitle());
					topicByName.put(topic.getTitle(), topic);
					topicByID.put(new Long(topic.getId()), topic);
				}				
			}


			callback.onSuccess(result);
		}

	}


	public int getNumberOfTopics() {
		return topicIdentifiers.size();
	}


	/**
	 * returns a topicID to callback
	 *  
	 * @param linkTo
	 * @param callback
	 */
	public void getTopicIdForNameOrCreateNew(String linkTo, final StdAsyncCallback callback) {

		//TODO replace with lookup in topicIdentifiers, since we don't really need a topic
		//obj here, just a TopicIdentifier
		//
		
		TopicIdentifier found = CacheUtils.searchTopics(topicIdentifiers,linkTo);
				
		if(found != null){
			callback.onSuccess(new Long(found.getTopicID()));			
		}
		else{
			System.out.println("Create New! ");
			Topic toSave = new Topic();
			toSave.setTitle(linkTo);
			save(toSave, new AsyncCallback(){
				public void onSuccess(Object result) {
					Topic saved = (Topic) result;
					callback.onSuccess(new Long(saved.getId()));
				}
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}});
		}
		
	}



}