package com.aavu.client.service;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.MainMap;
import com.aavu.client.gui.TagContentsWindow;
import com.aavu.client.gui.TagEditorWindow;
import com.aavu.client.gui.TopicWindow;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.Consts;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class Manager {

	private HippoCache hippoCache;
	public Consts myConstants;
	private MainMap map;


	public Manager(HippoCache hippoCache){
		this.hippoCache = hippoCache;
		initConstants();
	}
	private void initConstants() {
		myConstants = (Consts) GWT.create(Consts.class);
		Window.alert(myConstants.helloWorld());
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
				Tag tag = (Tag) result;
				TagContentsWindow tcw = new TagContentsWindow(Manager.this,tag);
				tcw.setPopupPosition(100,100);
				tcw.show();
			}});


	}
	public void doIslands() {
		map.doIslands();
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
}
