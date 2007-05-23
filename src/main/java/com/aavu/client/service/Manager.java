package com.aavu.client.service;

import java.util.List;

import org.gwm.client.GInternalFrame;
import org.gwtwidgets.client.ui.ProgressBar;

import com.aavu.client.HippoTest;
import com.aavu.client.async.EZCallback;
import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.CreateNewWindow;
import com.aavu.client.gui.EditMetaWindow;
import com.aavu.client.gui.EntryEditWindow;
import com.aavu.client.gui.MainMap;
import com.aavu.client.gui.SearchResultsWindow;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.TopicSaveListener;
import com.aavu.client.gui.ViewMemberWindow;
import com.aavu.client.gui.ext.DefaultGInternalFrameHippoExt;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.gadgets.GadgetManager;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.help.HelpWindow;
import com.aavu.client.help.UserHelper;
import com.aavu.client.images.Images;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.cache.TagCache;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.service.local.TagLocalService;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.strings.Consts;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class Manager implements TopicSaveListener, LoginListener  {
	
	private HippoCache hippoCache;	
	private MainMap map;
	private User user;
	
	//private FramesManager framesManager;
	//private DefaultGDesktopPane desktop;
	
	private TagLocalService tagLocalService;
	//private MainMap mainMap;
	private Glossary glossary; 

	private boolean focussed = false;

	private GadgetManager gadgetManager;
	private UserActionListener userActionListener;
	
	public Manager(HippoCache hippoCache){
		this.hippoCache = hippoCache;
		initConstants();
		hippoCache.getTopicCache().addSaveListener(this);
		
		gadgetManager = new GadgetManager(this);
		
		userActionListener = new UserHelper(this,user);
		
		//Note what is the purpose of "Desktop"? 
		//If we implement that, note that it messed with our 
		//theme application
		//		
		//framesManager = new FramesManagerFactory().createFramesManager(); 
		
		//desktop = new DefaultGDesktopPane();	   
				
	    //RootPanel.get(HippoTest.MAIN_DIV).add((Widget) desktop);
	    		
		map = new MainMap(this);
		
		
	}
	private void initConstants() {
		ConstHolder.myConstants = (Consts) GWT.create(Consts.class);
		ConstHolder.images = (Images) GWT.create(Images.class);
	}	

	
	public void bringUpChart(long id) {
		hippoCache.getTopicCache().getTopicByIdA(id,new StdAsyncCallback(ConstHolder.myConstants.topic_lookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				bringUpChart((Topic) result);				
			}});
	}
	public void bringUpChart(TopicIdentifier ident) {
		
		map.clearForLoading();
		
		hippoCache.getTopicCache().getTopic(ident,new StdAsyncCallback(ConstHolder.myConstants.topic_lookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				bringUpChart((Topic) result);				
			}});

	}
	public void bringUpChart(Topic topic) {		
		map.displayTopic(topic);
		
		//only zoom if we find something good to center on
		if(map.centerOn(topic)){
			map.ensureZoomOfAtLeast(4);
		}
	}
	

	//public void show(Topic topic, boolean editMode) {
	
	public void newTopic() {			
		
		CreateNewWindow n = new CreateNewWindow(this,ConstHolder.myConstants.topic_new(), new EZCallback(){
			public void onSuccess(Object result) {
				createTopic((String) result, false);
			}});			
	}
	/*
	 * window will call createIsland
	 */
	public void newIsland(){
		CreateNewWindow n = new CreateNewWindow(this,ConstHolder.myConstants.island_new(), new EZCallback(){
			public void onSuccess(Object result) {
				createTopic((String) result, true);
			}});			
	}
	/**
	 * pass the type of meta you'd like created and. 
	 * 1) we'll put up the create window box.
	 * 2) we'll go create it.
	 * 3) we'll call your AsyncCallback which will receive the TopicIdentifier of the newly created Meta.
	 * @param meta
	 * @param callback
	 */
	public void newMeta(final Meta meta,final AsyncCallback callback) {
		CreateNewWindow n = new CreateNewWindow(this,ConstHolder.myConstants.meta_new(), new EZCallback(){
				public void onSuccess(Object result) {
					getTopicCache().createNew((String)result, meta, new StdAsyncCallback(ConstHolder.myConstants.save_async()){
						public void onSuccess(Object result) {
							super.onSuccess(result);				
							callback.onSuccess(result);										
						}});
				}});
	}
	
	
	public void createTopic(final String name,final boolean isIsland) {

		getTopicCache().createNew(name, isIsland, new StdAsyncCallback(ConstHolder.myConstants.save_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);				
				TopicIdentifier res = (TopicIdentifier) result;				
								
				if(isIsland){
					Tag newIsland = new Tag();
					newIsland.setId(res.getTopicID());
					newIsland.setTitle(res.getTopicTitle());
					map.growIsland(newIsland);
					
					map.displayTopic(newIsland);
					
					fireIslandCreated();
				}else{
					Topic newTopic = new Topic();
					newTopic.setId(res.getTopicID());
					newTopic.setTitle(res.getTopicTitle());
					bringUpChart(newTopic);
					
					fireTopicCreated();
				}				
			}			
		});						
	}
	
	public void showGlossary() {
		ViewMemberWindow gw = new ViewMemberWindow(this,newFrame());
	}



	
//	public void showTagBoard() {
//
//		TagEditorWindow tw = new TagEditorWindow(hippoCache,newFrame());
//		
//	}
	
	public void doSearch(String text) {
		System.out.println("Search "+text);
		hippoCache.getTopicCache().search(text,new StdAsyncCallback(ConstHolder.myConstants.searchCallback()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				System.out.println("ssss");
				List searchRes = (List) result;
				
				SearchResultsWindow tw = new SearchResultsWindow(Manager.this,searchRes);
					
			}			
		});
	}
	
	
	

	/*
	 * 
	 * 
	 */
	public void showPreviews(final long id) {
		
		System.out.println("showPreviews "+id);
		
		getTopicCache().getTopicByIdA(id,new StdAsyncCallback(ConstHolder.myConstants.oceanIslandLookupAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Topic tag = (Topic) result;		
				map.displayTopic(tag);
				
				//IslandDetailsWindow tcw = new IslandDetailsWindow(tag,topics,Manager.this);						
			}});			
	}	
	public void unselect() {
		map.unselect();
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
	 * Topic was saved with this command. 
	 * 
	 * Main responsibility is for the map to redraw everything involving these topics.
	 * 
	 */
	public void topicSaved(Topic t,AbstractCommand command) {
		map.update(t,command);
		
		System.out.println("TOPIC SAVED");
		if(command instanceof SaveTagtoTopicCommand){
			System.out.println("TAG COMMAND");
			Tag tag = (Tag) command.getTopic(1);
			System.out.println("GROW "+tag);
			map.growIsland(tag);
		}
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
		map.refreshIslands();
	}
	
	public GInternalFrame newFrame() {
		return newFrame("");
	}
	private GInternalFrame newFrame(String title) {
		
		GInternalFrame frame = new DefaultGInternalFrameHippoExt(title,map);

		map.addFrame(frame);
		
		return frame;
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
		return map;
	}
	
	/**
	 * User is logged in. Update GUI.
	 *
	 */
	private void loadGUI() {		
		map.load();
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
	public void doLogin(){
		LoginService.doLogin(newFrame(),Manager.this);
	}
	public void delete(final Topic topic,final AsyncCallback callback) {
		getTopicCache().delete(topic,new StdAsyncCallback(ConstHolder.myConstants.delete_async()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				callback.onSuccess(result);
				
				if(topic instanceof Tag){
					map.removeIsland(topic.getId());
				}
				unselect();
				refreshAll();
			}				
		});
	}
	
	
	public PopupWindow showProgressBar(ProgressBar progressBar) {		
		ProgressPopup win = new ProgressPopup(newFrame(progressBar.getTitle()),progressBar.getTitle(),progressBar);				
		return win;
	}
	
	private class ProgressPopup extends PopupWindow{
		public ProgressPopup(GInternalFrame frame, String title, ProgressBar progressBar) {
			super(frame, title,200,100);
			setContent(progressBar);
			
		}		
	}
	public void zoomTo(double scale) {
		map.zoomTo(scale);	
	}
	public void zoomOut() {
		map.zoomOut();
	}
	public void zoomIn() {
		map.zoomIn();
	}
	public void editEntry(Topic topic) {				
		EntryEditWindow gw = new EntryEditWindow(topic,this,newFrame());						
	}

	public void editMetas(AsyncCallback callback,final Meta type) {
		EditMetaWindow ew = new EditMetaWindow(this,newFrame(),type, callback);
	}	
	
	public void viewMembers(Topic myTag, List topics) {

		ViewMemberWindow gw = new ViewMemberWindow(myTag, topics,this,newFrame());
		
	}
	public GadgetManager getGadgetManager() {
		return gadgetManager;
	}
	
	public void fireOceanLoaded(int num_islands) {		
		if(userActionListener != null){
			userActionListener.oceanLoaded(num_islands);
		}
	}
	public void fireIslandCreated() {	
		if(userActionListener != null){
			userActionListener.islandCreated();
		}
	}
	public void fireTopicCreated() {		
		if(userActionListener != null){
			userActionListener.topicCreated();
		}
	}
	public void changeState(final Topic topic, final boolean toIsland, final StdAsyncCallback callback) {

		AsyncCallback wrapper = new AsyncCallback(){
			public void onFailure(Throwable caught) {				
				callback.onFailure(caught);
			}

			public void onSuccess(Object result) {				
				callback.onSuccess(result);
				
				if(toIsland){
					Tag newIsland = new Tag();
					topic.copyPropsIntoParam(newIsland);					
					growIsland(newIsland);
					
					map.displayTopic(newIsland);						
				}else{
					map.removeIsland(topic.getId());
					
					//change type to topic 
					Topic notAnIsland = new Topic();
					topic.copyPropsIntoParam(notAnIsland);
					map.displayTopic(notAnIsland);						
				}
				
				
			}};
		
		getTopicCache().changeState(topic.getId(), toIsland, wrapper);	
	}
	
	public void showHelp() {
		HelpWindow hw = new HelpWindow(this,newFrame());
	}
	
	
	/**
	 * Simple warning dialog wrapper
	 * 
	 * NOTE: css style in public/themes/alphacube.css
	 * @param warning
	 */
	public void displayInfo(String warning){
		GInternalFrame f = newFrame();
		PopupWindow w = new PopupWindow(f,ConstHolder.myConstants.displayInfoTitle(),true);			
		f.setContent(warning);
	}
	
	public void userNeedsToUpgrade() {
		displayInfo(ConstHolder.myConstants.userNeedsToUpgrade());		
	}
	
	


}
