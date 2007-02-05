package com.aavu.client.service;

import java.util.List;

import org.gwm.client.FramesManager;
import org.gwm.client.FramesManagerFactory;
import org.gwm.client.GInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.HippoTest;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.IslandDetailsWindow;
import com.aavu.client.gui.LoginWindow;
import com.aavu.client.gui.MainMap;
import com.aavu.client.gui.NewTagNameWindow;
import com.aavu.client.gui.SearchResultsWindow;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.gui.TopicWindow;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.gui.glossary.GlossaryWindow;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.strings.Consts;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class Manager implements TopicSaveListener {
	
	private HippoCache hippoCache;
	public static Consts myConstants;
	private MainMap map;
	private User user;
	
	private FramesManager framesManager;
	private TagLocalService tagLocalService;
	private MainMap mainMap;
	private Glossary glossary; 

	private boolean focussed = false;
	
	public Manager(HippoCache hippoCache){
		this.hippoCache = hippoCache;
		initConstants();
		hippoCache.getTopicCache().addSaveListener(this);
		
		
		
		framesManager = new FramesManagerFactory().createFramesManager(); 
		
		mainMap = new MainMap(this);
		
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
	public void bringUpChart(final Topic topic, boolean editMode) {
		
		if(topic instanceof Tag){
			System.out.println("BRINGING UP TAG "+topic.toPrettyString());
			System.out.println(" "+topic.getMetas());
			System.out.println(" "+topic.getMetas().size());
			getTopicCache().getTopicsWithTag(topic.getId(),new StdAsyncCallback(myConstants.oceanIslandLookupAsync()){
				public void onSuccess(Object result) {
					super.onSuccess(result);
					FullTopicIdentifier[] topics = (FullTopicIdentifier[]) result;
					IslandDetailsWindow tcw = new IslandDetailsWindow((Tag) topic,topics,Manager.this);					
				}});
						
		}else{
			System.out.println("BRINGING UP TOPIC "+topic);
			TopicWindow tw = new TopicWindow(this,topic);		
//			if(editMode){
//				//tw.setToEdit();
//			}
			
		}
	}

	//public void show(Topic topic, boolean editMode) {
	
	public void newTopic() {	
		Topic blank = new Topic();
		blank.setTitle(myConstants.topic_new_title());
		bringUpChart(blank,true);		
	}
	/*
	 * window will call createIsland
	 */
	public void newIsland(){
		NewTagNameWindow n = new NewTagNameWindow(this);	
	}
	public void createIsland(final String name) {

		getTopicCache().createNew(name, true, new StdAsyncCallback(Manager.myConstants.save_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);				
				Long res = (Long) result;
				Tag newIsland = new Tag();
				newIsland.setId(res.longValue());
				newIsland.setTitle(name);
				mainMap.growIsland(newIsland);					
			}			
		});
						
	}
	public void showGlossary() {
		if(glossary == null || glossary.isDirty()){
			glossary = new Glossary(this,Orientation.HORIZONTAL);
			glossary.load();
		}
		GlossaryWindow gw = new GlossaryWindow(glossary,newFrame());
	}

//	public void showTagBoard() {
//
//		TagEditorWindow tw = new TagEditorWindow(hippoCache,newFrame());
//		
//	}
	
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
	 * 
	 * 
	 */
	public void showPreviews(final long id) {
		
		System.out.println("showPreviews "+id);
		
		getTopicCache().getTopicByIdA(id,new StdAsyncCallback(myConstants.oceanIslandLookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Topic tag = (Topic) result;		
				mainMap.displayTopic(tag);
				
				//IslandDetailsWindow tcw = new IslandDetailsWindow(tag,topics,Manager.this);						
			}});			
	}	
	public void unselectIsland() {
		mainMap.unselect();
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

	public void growIsland(Tag tag) {
		map.growIsland(tag);
	}
	/**
	 * TODO don't load the entire darn list/use the cache etc
	 */
	public void topicSaved(Topic t) {
		map.updateSidebar(t);
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
	
	public User getUser() {
		return user;
	}
	
	
	/**
	 * Note: LoginWindow has a semaphore to prevent multiple instances
	 * Will bring up a dialog that on success will call loginSuccess()
	 * 
	 */
	public void doLogin() {
		LoginWindow lw = new LoginWindow(this);
	}
	
	
	/**
	 * Call when we've been not logged in, but we've now logged in, and we need to setup the 
	 * GUI elements.
	 *
	 *
	 */
	public void loginSuccess() {
		setup("LOGIN SUCC");
	}
	
	
	/**
	 * Called by HippoTest to replace the load screen with the map. Called before we've even
	 * checked if a user exists.
	 * 
	 * @return
	 */
	public Widget getRootWidget() {
//		AbsolutePanel p = new AbsolutePanel();
//		p.add(mainMap.getOcean(),0,0);
//		p.add(mainMap,0,0);
//		
//		return p;
		return mainMap;
	}
	
	/**
	 * User is logged in. Update GUI.
	 *
	 */
	private void loadGUI() {
		mainMap.load();
	}
	
	/**
	 * This will try to get the current user.
	 * If it suceeds it will run the GUI load scripts.
	 * If it doesn't suceed it will bring up the login dialog.
	 *
	 */
	public void setup(final String caller) {
		
		System.out.println("setup() from "+caller);
		
		hippoCache.getUserService().getCurrentUser(new AsyncCallback(){
			public void onSuccess(Object result) {
				
				user = (User) result;
		System.out.println("succ "+caller);
				if(user != null){
					System.out.println("found a user: "+user.getUsername());	
					
//					try {
//						throw new Exception();
//					} catch (Exception e) {					
//						e.printStackTrace();
//					}
					System.out.println("LoadGUI "+caller);
					loadGUI();
				}else{
					System.out.println("null user ELSE "+caller);
					doLogin();
				}
			}
		
			/*
			 * Expected, since we are preloading in an IFrame whether or not they've logged in
			 * 
			 * (non-Javadoc)
			 * @see com.google.gwt.user.client.rpc.AsyncCallback#onFailure(java.lang.Throwable)
			 */
			public void onFailure(Throwable caught) {	
				
				System.out.println("failed for "+caller);
				Logger.log("GetCurrentUser failed! "+caught+" \nEP:"+HippoTest.getRelativeURL(""));
				doLogin();											
			}						
		});		
	}
	public void delete(final Topic topic,final AsyncCallback callback) {
		getTopicCache().delete(topic,new StdAsyncCallback(Manager.myConstants.delete_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				callback.onSuccess(result);
				
				if(topic instanceof Tag){
					mainMap.removeIsland(topic.getId());
				}
				refreshAll();
			}				
		});
	}
	
	public void setFocussed(boolean focussed) {
		this.focussed = focussed;
		mainMap.showBackToOcean(focussed);
	}
	public boolean isFocussed() {
		return focussed;
	}
	public void unFocus() {
		this.focussed = false;
		mainMap.unFocus();
	}
	public PopupWindow showProgressBar(ProgressBar progressBar) {		
		ProgressPopup win = new ProgressPopup(framesManager.newFrame(),progressBar.getTitle(),progressBar);				
		return win;
	}
	private class ProgressPopup extends PopupWindow{
		public ProgressPopup(GInternalFrame frame, String title, ProgressBar progressBar) {
			super(frame, title,200,100);
			setContent(progressBar);
			
		}		
	}
	public void zoomTo(double scale) {
		mainMap.zoomTo(scale);	
	}
	
	
		


}
