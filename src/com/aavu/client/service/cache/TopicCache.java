package com.aavu.client.service.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TopicCache {

	public static final ReturnTypeConstant TOPIC = new ReturnTypeConstant(1);
	public static final ReturnTypeConstant TOPIC_LIST = new ReturnTypeConstant(2);

	private Map topicByName = new HashMap(); 
	private Map topicByID = new HashMap();
	private GWTTopicServiceAsync topicService;

	/**
	 * map with null values. we just need something sortable.
	 */
	private GWTSortedMap topicIdentifiers = new GWTSortedMap();
	
	private List saveListeners = new ArrayList();
	private boolean topicIdentifiersDirty = true;
	

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
		if(!topicIdentifiersDirty){
			callback.onSuccess(topicIdentifiers.getKeyList());			
		} else {
			topicService.getAllTopicIdentifiers(new StdAsyncCallback(Manager.myConstants.topic_getAllAsync()){
				public void onSuccess(Object result) {
					super.onSuccess(result);
					TopicIdentifier[] topicIdents = (TopicIdentifier[]) result;
					System.out.println("rec "+topicIdents.length);
					topicIdentifiers.clear();
					for (int i = 0; i < topicIdents.length; i++) {
						System.out.println("adding! "+i+" id:"+topicIdents[i].getTopicID()+" "+topicIdents[i].getTopicTitle());
						topicIdentifiers.put(topicIdents[i],null);						
					}					
					topicIdentifiersDirty = false;
					callback.onSuccess(topicIdentifiers.getKeyList());					
				}
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}});		
		}
	}


	
	public void save(Topic topic, AsyncCallback callback) {
		save(topic,null,callback);
	}
	public void save(Topic topic, Set otherTopicsToSave, AsyncCallback callback) {
		System.out.println("client saving "+topic.toPrettyString());
		
		if(otherTopicsToSave == null){
			Topic[] listToSave = new Topic[1];
			listToSave[0] = topic;			
			System.out.println("saving single "+listToSave.length);
			topicService.save(listToSave, new SaveCallback(callback));
		}else{
			Topic[] listToSave = new Topic[otherTopicsToSave.size() + 1];
			listToSave[0] = topic;
			
			Iterator iter = otherTopicsToSave.iterator();
			for(int i = 1;i < listToSave.length; i++){			
				listToSave[i] = (Topic) iter.next();
			}
			System.out.println("Saving list ");
			for(int i = 0;i < listToSave.length; i++){			
				System.out.println("i:"+i+" "+listToSave[i]);
			}
			topicService.save(listToSave, new SaveCallback(callback));	
		}
		
				
	}
	private class SaveCallback implements AsyncCallback {
		private AsyncCallback callback;
		public SaveCallback(AsyncCallback callback) {
			this.callback = callback;			
		}
		public void onFailure(Throwable caught) {
			callback.onFailure(caught);
		}
		public void onSuccess(Object result) {
			Topic[] resA = (Topic[]) result;
			
			if(resA == null){
				callback.onFailure(new Throwable("Save Returned Null"));
			}
			
			System.out.println("result length "+resA.length);
			
			for (int i = 0; i < resA.length; i++) {
				Topic res = resA[i];

				System.out.println("res "+res);
				
				if(res == null){
					continue;
				}
				System.out.println("R/A "+res.getIdentifier());
				
				//TODO bogus, need to check!!
				topicIdentifiers.remove(res.getIdentifier());
				topicIdentifiers.put(res.getIdentifier(),null);
				
				//topicByName.put(res.getTitle(), res);
				//topicByID.put(res.getId(), res);
				
				for (Iterator iter = saveListeners.iterator(); iter.hasNext();) {
					TopicSaveListener listener = (TopicSaveListener) iter.next();
					listener.topicSaved(res);
				}			
								
			}
			callback.onSuccess(result);
		}

	}

	public void match(String match, AsyncCallback call) {
		topicService.match(match, call);		
	}

	/**
	 * callback for List<TopicIdentifier>
	 * @param tag
	 * @param callback
	 */
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
				
				System.out.println("res "+result);
				
				Topic t = (Topic) result;
				
				System.out.println("rec "+t);
				System.out.println("rec: "+t.toPrettyString());
				System.out.println("single adding to cache title:"+t.getTitle());
				//topicByName.put(t.getTitle(), t);
				//topicByID.put(new Long(t.getId()), t);
				

			}else if(rtn == TOPIC_LIST){
				Topic[] t = (Topic[]) result;
				//	if(t.)
				for (int i = 0; i < t.length; i++) {					
					Topic topic = t[i];
					System.out.println("list adding to cache "+topic.getTitle());
					//topicByName.put(topic.getTitle(), topic);
					//topicByID.put(new Long(topic.getId()), topic);
					
					
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
	 * NOTE: this relies on the topicIdentifiers list being a correctly sorted list, otherwise
	 * binary search won't work.
	 *  
	 * @param linkTo
	 * @param callback
	 */
	public void getTopicIdentForNameOrCreateNew(String linkTo, final StdAsyncCallback callback) {

		//TODO replace with lookup in topicIdentifiers, since we don't really need a topic
		//obj here, just a TopicIdentifier
		//
		
		TopicIdentifier found = CacheUtils.searchTopics(topicIdentifiers,linkTo);
				
		if(found != null){
			System.out.println("Found "+found);
			callback.onSuccess(found);			
		}
		else{
			System.out.println("Create New! ");
			Topic toSave = new Topic();
			toSave.setTitle(linkTo);
			save(toSave, new AsyncCallback(){
				public void onSuccess(Object result) {
					Topic[] saved = (Topic[]) result;
					callback.onSuccess(saved[0].getIdentifier());
				}
				public void onFailure(Throwable caught) {
					callback.onFailure(caught);
				}});
		}
		
	}



	public void getTimelineObjs(AsyncCallback callback) {
		topicService.getTimelineObjs(callback);
	}

	public void addSaveListener(TopicSaveListener l){
		saveListeners.add(l);
	}



	public void getLinksTo(Topic topic2, StdAsyncCallback callback) {
		topicService.getLinksTo(topic2, callback);
	}

	public void search(String text, StdAsyncCallback callback) {		
		topicService.search(text, callback);		
	}
	public void getTreeFor(MindTreeOcc treeOcc, StdAsyncCallback callback) {
		topicService.getTree(treeOcc, callback);
	}
	public void saveTree(MindTree mindTree, StdAsyncCallback callback) {
		topicService.saveTree(mindTree, callback);
	}



	public void delete(Topic topic, StdAsyncCallback callback) {
		topicService.delete(topic,callback);
		//TODO update after delete
	}



}
