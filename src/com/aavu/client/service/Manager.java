package com.aavu.client.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.gui.MainMap;
import com.aavu.client.gui.TopicDisplayWindow;
import com.aavu.client.gui.TagEditorWindow;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.gui.TopicWindow;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.gui.timeline.OldTimeLine;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.Consts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

public class Manager implements TopicSaveListener {

	private static final int DEF_Y = 200;
	private static final int DEF_X = 200;
	private HippoCache hippoCache;
	public static Consts myConstants;
	private MainMap map;
	private User user;

	

	public Manager(HippoCache hippoCache, User user){
		this.hippoCache = hippoCache;
		this.user = user;
		initConstants();
		hippoCache.getTopicCache().addSaveListener(this);
	}
	private void initConstants() {
		myConstants = (Consts) GWT.create(Consts.class);
	}	

	public void bringUpChart(TopicIdentifier ident) {
		hippoCache.getTopicCache().getTopic(ident,new StdAsyncCallback("BringUpChart"){
			public void onSuccess(Object result) {
				bringUpChart((Topic) result);				
			}});

	}
	public void bringUpChart(Topic topic) {
		TopicWindow tw = new TopicWindow(this,topic);
		tw.setPopupPosition(DEF_X,DEF_Y);
		tw.show();		
	}

	public void newTopic() {
		Topic blank = new Topic();
		blank.setTitle("new");
		bringUpChart(blank);		
	}


	public void showTagBoard() {

		TagEditorWindow tw = new TagEditorWindow(hippoCache);
		tw.setPopupPosition(DEF_X,DEF_Y);
		tw.show();
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
		hippoTime.setPopupPosition(DEF_X,DEF_Y);
		hippoTime.show();
		
	}

	/*
	 * TODO this works, but relies on us loading tags by ID on server side
	 * Perhaps getTopicsWithTag should take just the ID?
	 */
	public void showTopicsForTag(int arg) {
		Tag t = new Tag();
		t.setId(arg);
		showTopicsForTag(t);
	}
	public void showTopicsForTag(String completeText) {
		hippoCache.getTagCache().getTagForName(completeText,new StdAsyncCallback("Get Tag For Name"){
			public void onSuccess(Object result) {
				final Tag tag = (Tag) result;
				showTopicsForTag(tag);	
			}});
	}
	public void showTopicsForTag(final Tag tag) {
		getTopicCache().getTopicsWithTag(tag,new StdAsyncCallback("Get Topics with Tag"){
			public void onSuccess(Object result) {
				TopicIdentifier[] topics = (TopicIdentifier[]) result;

				TopicDisplayWindow tcw = new TopicDisplayWindow(tag.getName(),topics,Manager.this);
				tcw.setPopupPosition(DEF_X,DEF_Y);
				tcw.show();		
			}});				
	}
	
	
	public void doIslands() {
		map.doIslands();
	}

	/**
	 * we can goto a topic linked by either Name, or ID.
	 * Parse the history token, ie HippoTest.html#453 or HippoTest.html#MyTopicName
	 * 
	 * @param historyToken
	 */
	public void gotoTopic(String historyToken) {

		long l = -1;
		try{
			l = Long.parseLong(historyToken);
		}catch(NumberFormatException e){

		}
		System.out.println("|"+historyToken+"|"+l);
		if(l == -1){

			hippoCache.getTopicCache().getTopicForNameA(historyToken, new StdAsyncCallback("GotoTopicStr"){
				public void onSuccess(Object result) {
					Topic t = (Topic) result;
					bringUpChart(t);
				}});
		}else{
			hippoCache.getTopicCache().getTopicByIdA(l,new StdAsyncCallback("GotoTopicID"){
				public void onSuccess(Object result) {
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
	


}
