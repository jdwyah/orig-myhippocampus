package com.aavu.client.service;

import java.util.List;

import org.gwm.client.FramesManager;
import org.gwm.client.FramesManagerFactory;
import org.gwm.client.GInternalFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.gui.MainMap;
import com.aavu.client.gui.SearchResultsWindow;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.TagEditorWindow;
import com.aavu.client.gui.IslandDetailsWindow;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.gui.TopicWindow;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.strings.Consts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Manager implements TopicSaveListener {
	
	private HippoCache hippoCache;
	public static Consts myConstants;
	private MainMap map;
	private User user;
	
	private FramesManager framesManager;
	private TagLocalService tagLocalService; 

	public Manager(HippoCache hippoCache, User user){
		this.hippoCache = hippoCache;
		this.user = user;
		initConstants();
		hippoCache.getTopicCache().addSaveListener(this);
		
		
		
		framesManager = new FramesManagerFactory().createFramesManager(); 
		
		
	}
	private void initConstants() {
		myConstants = (Consts) GWT.create(Consts.class);
	}	

	public void bringUpChart(TopicIdentifier ident) {
		hippoCache.getTopicCache().getTopic(ident,new StdAsyncCallback(myConstants.topic_lookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				bringUpChart((Topic) result);				
			}});

	}
	public void bringUpChart(Topic topic) {
		bringUpChart(topic, false);
	}
	public void bringUpChart(Topic topic, boolean editMode) {
		
		if(topic instanceof Tag){
			showTopicsForTag((Tag)topic);
		}else{
			TopicWindow tw = new TopicWindow(this,topic,newFrame());		
			if(editMode){
				tw.setToEdit();
			}
		}
	}

	public void newTopic() {
		Topic blank = new Topic();
		blank.setTitle(myConstants.topic_new_title());
		bringUpChart(blank,true);		
	}
	public void newIsland() {
		Tag blank = new Tag();
		blank.setTitle(myConstants.topic_new_title());
		bringUpChart(blank,true);		
	}

	public void showTagBoard() {

		TagEditorWindow tw = new TagEditorWindow(hippoCache,newFrame());
		
	}
	public void doSearch(String text) {
		System.out.println("Search "+text);
		hippoCache.getTopicCache().search(text,new StdAsyncCallback(myConstants.searchCallback()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				System.out.println("ssss");
				List searchRes = (List) result;
				
				SearchResultsWindow tw = new SearchResultsWindow(Manager.this,searchRes);
					
			}			
		});
	}
	
	
	public void showTimeline() {
//		List timeLinesObjs = new ArrayList();
//		for(int i=0;i<10;i++){
//			Date d = new Date(2005,i,22);
//			Date e = null;
//			if(i % 2 == 0){
//				e = new Date(2006,i,22);
//			}
//			timeLinesObjs.add(new TimeLineObj(null,d,e));
//		}
//		OldTimeLine tl = new OldTimeLine(this,timeLinesObjs);
//		tl.setPopupPosition(DEF_X,DEF_Y);
//		tl.show();
//		
		
		HippoTimeLine hippoTime = new HippoTimeLine(this,null);				
		
	}

	/*
	 * TODO this works, but slow nested Async
	 * 
	 * Used By Flash Ocean
	 */
	public void showTopicsForTag(long id) {
		
		getTopicCache().getTopicByIdA(id, new StdAsyncCallback(myConstants.oceanIslandLookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				showTopicsForTag((Tag)result);		
			}});		
	}

	public void showTopicsForTag(final Tag tag) {
		getTopicCache().getTopicsWithTag(tag,new StdAsyncCallback(myConstants.oceanIslandLookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				TopicIdentifier[] topics = (TopicIdentifier[]) result;

				IslandDetailsWindow tcw = new IslandDetailsWindow(tag,topics,Manager.this);
						
			}});				
	}
	
		

	/**
	 * we can goto a topic linked by either Name, or ID.
	 * Parse the history token, ie HippoTest.html#453 or HippoTest.html#MyTopicName
	 * 
	 * @param historyToken
	 */
	public void gotoTopic(String historyToken) {

		long l = -2;//Will be -2 if we're loading by name
		try{
			l = Long.parseLong(historyToken);
		}catch(NumberFormatException e){

		}
		System.out.println("|"+historyToken+"|"+l);
		if(l == -2){
			hippoCache.getTopicCache().getTopicForNameA(historyToken, new StdAsyncCallback("GotoTopicStr "+l){
				public void onSuccess(Object result) {
					super.onSuccess(result);
					Topic t = (Topic) result;
					bringUpChart(t);
				}});
		}else if(l != -1){// == HippoTest.EMPTY
			hippoCache.getTopicCache().getTopicByIdA(l,new StdAsyncCallback("GotoTopicID "+l){
				public void onSuccess(Object result) {
					super.onSuccess(result);
					Topic t = (Topic) result;
					bringUpChart(t);
				}});
		}
	}



	public void setMap(MainMap map) {
		this.map = map;
	}
	public TopicCache getTopicCache() {
		return hippoCache.getTopicCache();
	}
	public TagCache getTagCache() {
		return hippoCache.getTagCache();
	}
	public User getUser() {
		return user;
	}
	public void growIsland(Tag tag) {
		map.growIsland(tag);
	}
	/**
	 * TODO don't load the entire darn list/use the cache etc
	 */
	public void topicSaved(Topic t) {
		map.updateSidebar();
	}
	public void updateStatus(int i, String call, StatusCode send) {
		map.updateStatusWindow(i, call, send);
	}
	public GWTSubjectServiceAsync getSubjectService() {
		return hippoCache.getSubjectService();
	}
	public void addDeliciousTags(String username, String password,AsyncCallback callback) {
		hippoCache.getSubjectService().addDeliciousTags(username, password, callback);		
	}
	public void refreshAll(){
		map.updateSidebar();
		map.refreshIslands();
	}
	public GInternalFrame newFrame() {
		return framesManager.newFrame();
	}
	public TagLocalService getTagLocalService() {
		if(tagLocalService == null){
			tagLocalService = new TagLocalService();
		}
		return tagLocalService;
	}
	
	
	


}
