package com.aavu.client.service.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TopicCache {

	public static final ReturnTypeConstant TOPIC = new ReturnTypeConstant(1);
	public static final ReturnTypeConstant TOPIC_LIST = new ReturnTypeConstant(2);

	private Map topicByName = new HashMap(); 
	private Map topicByID = new HashMap();
	private GWTTopicServiceAsync topicService;

	/**
	 * map with null values. we just need something sortable.
	 * Map<TopicIdentifiers,null>
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
	public void getAllTopicIdentifiers(final AsyncCallback callback) {
		if(!topicIdentifiersDirty){
			callback.onSuccess(topicIdentifiers.getKeyList());			
		} else {
			topicService.getAllTopicIdentifiers(new StdAsyncCallback(ConstHolder.myConstants.topic_getAllAsync()){
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

	/**
	 * Ok, here's how this works. We don't want to serialize the whole topic, send it to the 
	 * server and then hope/pray/hack that things get saved right with respect to all of the
	 * lazy loading / persistent set / CGLIB etc munging that we did on the way to the client.
	 * 
	 * Instead we implement our logic in commands. 
	 * 
	 * These nuggets have everything they need to affect the changes. 
	 * We'll run them here on the client, to update our local state, but then we'll serialize
	 * them and send them over to the server where they will be hydrated, run and saved.
	 * 
	 * @param topic
	 * @param command
	 * @param callback
	 */
	public void executeCommand(Topic topic, AbstractCommand command, AsyncCallback callback) {
		
		try {
			command.executeCommand();
		} catch (HippoBusinessException e) {
			Logger.log("command execution problem: "+e);
		}
		
		topicService.saveCommand(command,new SaveCallbackWrapper(topic,command,callback));		
	}	
	
	/**
	 * Call the save listeners on success, then pass the results back to the original caller.
	 * @author Jeff Dwyer
	 *
	 */
	private class SaveCallbackWrapper implements AsyncCallback {
		private AsyncCallback callback;
		private Topic topic;
		private AbstractCommand command;
		
		public SaveCallbackWrapper(Topic topic, AbstractCommand command, AsyncCallback callback) {
			this.topic = topic;
			this.command = command;
			this.callback = callback;			
		}
		public void onFailure(Throwable caught) {			
			Logger.log("caught "+caught);
			callback.onFailure(caught);
		}
		public void onSuccess(Object result) {
			Logger.log("Save callback rtn "+result);
			
			for (Iterator iter = saveListeners.iterator(); iter.hasNext();) {
				TopicSaveListener listener = (TopicSaveListener) iter.next();
				listener.topicSaved(topic,command);
			}			
			
			callback.onSuccess(result);
		}
	}
	

//	public void saveNotifyListeners(Topic topic, SaveTitleCommand command, StdAsyncCallback callback) {
//		if(command.updatesTitle()){
//			for (Iterator iter = saveListeners.iterator(); iter.hasNext();) {
//				TopicSaveListener listener = (TopicSaveListener) iter.next();
//				listener.topicSaved(new TopicIdentifier(command.getTopicID(),command.getData()));
//			}			
//		}
//		topicService.saveCommand(command,callback);		
//	}


	
	
//	public void save_OLD(Topic topic, AsyncCallback callback) {
//		save_OLD(topic,null,callback);
//	}
//	public void save_OLD(Topic topic, Set otherTopicsToSave, AsyncCallback callback) {
//		System.out.println("client saving "+topic.toPrettyString());
//		
//		if(otherTopicsToSave == null){
//			Topic[] listToSave = new Topic[1];
//			listToSave[0] = topic;			
//			System.out.println("saving single "+listToSave.length);
//	//		topicService.save(listToSave, new SaveCallback(callback));
//		}else{
//			Topic[] listToSave = new Topic[otherTopicsToSave.size() + 1];
//			listToSave[0] = topic;
//			
//			Iterator iter = otherTopicsToSave.iterator();
//			for(int i = 1;i < listToSave.length; i++){			
//				listToSave[i] = (Topic) iter.next();
//			}
//			System.out.println("Saving list ");
//			for(int i = 0;i < listToSave.length; i++){			
//				System.out.println("i:"+i+" "+listToSave[i]);
//			}
//		//	topicService.save(listToSave, new SaveCallback(callback));	
//		}
//				
//	}
	
//	private class SaveCallback implements AsyncCallback {
//		private AsyncCallback callback;
//		public SaveCallback(AsyncCallback callback) {
//			this.callback = callback;			
//		}
//		public void onFailure(Throwable caught) {
//			System.out.println("AAAAAAAAAAAAA");
//			Logger.log("SAVE CALLBACK FAILING");
//			Logger.log("caugt "+caught);
//			callback.onFailure(caught);
//		}
//		public void onSuccess(Object result) {
//			System.out.println("BBBBBBBBBBBBB");
//			Logger.log("SAVE CALLBACK SUCEEED");
//			Logger.log("rtn "+result);
//			Topic[] resA = (Topic[]) result;
//			
//			if(resA == null){
//				callback.onFailure(new Throwable("Save Returned Null"));
//			}
//			
//			System.out.println("result length "+resA.length);
//			
//			for (int i = 0; i < resA.length; i++) {
//				Topic res = resA[i];
//
//				System.out.println("res "+res);
//				
//				if(res == null){
//					continue;
//				}
//				System.out.println("R/A "+res.getIdentifier());
//				
//				//TODO bogus, need to check!!
//				topicIdentifiers.remove(res.getIdentifier());
//				topicIdentifiers.put(res.getIdentifier(),null);
//				
//				//topicByName.put(res.getTitle(), res);
//				//topicByID.put(res.getId(), res);
//				
//				for (Iterator iter = saveListeners.iterator(); iter.hasNext();) {
//					TopicSaveListener listener = (TopicSaveListener) iter.next();
//					listener.topicSaved(res);
//				}			
//								
//			}
//			callback.onSuccess(result);
//		}
//
//	}

	public void match(String match, AsyncCallback call) {
		topicService.match(match, call);		
	}

	/**
	 * callback for List<TopicIdentifier>
	 * @param id
	 * @param callback
	 */
	public void getTopicsWithTag(long id, StdAsyncCallback callback) {
		topicService.getTopicIdsWithTag(id,callback);
	}


	/**
	 * returns List<List<FullTopicIdentifier>>
	 * @param shoppingList
	 * @param callback
	 */
	public void getTopicsWithTag(List shoppingList, StdAsyncCallback callback) {
		topicService.getTopicsWithTags(shoppingList,callback);
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
				if(t != null){
					//Logger.debug("rec: "+t.toPrettyString());
					System.out.println("single adding to cache title:"+t.getTitle());
				}
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
	 * Since it's possible that we'll need to init the TopicIdentifiers list first, create our own callback 
	 * to wrap the functionality.
	 *  
	 * @param linkTo
	 * @param callback
	 */
	public void getTopicIdentForNameOrCreateNew(String linkTo, final StdAsyncCallback callback) {

		TopicLookupOrNewCallback ourCall = new TopicLookupOrNewCallback(callback,linkTo);
		
		if(topicIdentifiersDirty){
			getAllTopicIdentifiers(ourCall);
		}else{
			ourCall.onSuccess(null);
		}		
	}
	/**
	 * This callback expects to be onSuccessed once the topicIdentifiers have been loaded
	 * 
	 *  onSuccess shoudl be called with null.
	 */
	private class TopicLookupOrNewCallback implements AsyncCallback {

		private AsyncCallback originalCallback;
		private String linkTo;

		public TopicLookupOrNewCallback(AsyncCallback originalCallback,String linkTo) {
			this.originalCallback = originalCallback;
			this.linkTo = linkTo;
		}
		public void onFailure(Throwable caught) {					
			originalCallback.onFailure(caught);
		}
		public void onSuccess(Object result) {
			TopicIdentifier found = CacheUtils.searchTopics(topicIdentifiers,linkTo);
			
			if(found != null){
				System.out.println("Found "+found);
				originalCallback.onSuccess(found);			
			}
			else{
				System.out.println("Create New! ");
				createNew(linkTo, false, originalCallback);
								
			}
		}
		
	}



	public void getTimelineObjs(long meta_id, AsyncCallback callback) {
		topicService.getTimelineObjs(meta_id,callback);
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



	public void saveTopicLocationA(long tagId, long topicId, double xpct, double ypct, StdAsyncCallback callback) {
		topicService.saveTopicLocation(tagId,topicId,xpct,ypct,callback);
	}



	/**
	 * All new topic creation shoudl route through here
	 * 
	 * @param title
	 * @param isIsland
	 * @param callback
	 */
	public void createNew(String title, boolean isIsland, AsyncCallback callback) {
		//TODO inefficient. We should wrap & insert instead of doing a full lookup
		topicIdentifiersDirty = true;
		
		if(isIsland){
			createNew(title, new Tag(),callback);
		}else{
			createNew(title, new Topic(),callback);
		}
	}
	public void createNew(String title, Topic topicOrTagOrMeta, AsyncCallback callback) {
				
		topicService.createNew(title, topicOrTagOrMeta, callback);		
	}



	public void changeState(long id, boolean b, AsyncCallback callback) {
		topicService.changeState(id, b, callback);
	}



	public void deleteOccurrence(WebLink link, AsyncCallback callback) {
		topicService.deleteOccurrence(link.getId(), callback);		
	}



	public void getAllMetasOfType(Meta type, AsyncCallback callback) {		
		topicService.getAllMetas(callback);
	}





}
