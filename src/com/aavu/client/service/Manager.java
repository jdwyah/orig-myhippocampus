package com.aavu.client.service;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.gui.MainMap;
import com.aavu.client.gui.TopicDisplayWindow;
import com.aavu.client.gui.TagEditorWindow;
import com.aavu.client.gui.TopicWindow;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.Consts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;

public class Manager {

	private HippoCache hippoCache;
	public Consts myConstants;
	private MainMap map;
	private User user;


	public Manager(HippoCache hippoCache, User user){
		this.hippoCache = hippoCache;
		this.user = user;
		initConstants();
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
		TopicWindow tw = new TopicWindow(hippoCache,topic);
		tw.setPopupPosition(100,100);
		tw.show();		
	}

	public void newTopic() {
		Topic blank = new Topic();
		blank.setTitle("new");
		bringUpChart(blank);		
	}


	public void showTagBoard() {

		TagEditorWindow tw = new TagEditorWindow(hippoCache);
		tw.setPopupPosition(100,100);
		tw.show();
	}


	public void showTopicsForTag(String completeText) {

		hippoCache.getTagCache().getTagForName(completeText,new StdAsyncCallback("Get Tag For Name"){

			public void onSuccess(Object result) {
				final Tag tag = (Tag) result;
				getTopicCache().getTopicsWithTag(tag,new StdAsyncCallback("Get Topics with Tag"){

					public void onSuccess(Object result) {
						TopicIdentifier[] topics = (TopicIdentifier[]) result;

						TopicDisplayWindow tcw = new TopicDisplayWindow(tag.getName(),topics,Manager.this);
						tcw.setPopupPosition(100,100);
						tcw.show();		
					}});								
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

}
