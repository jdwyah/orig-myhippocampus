package com.aavu.client;

import com.aavu.client.domain.MetaTopicList;
import com.aavu.client.domain.User;
import com.aavu.client.gui.MainMap;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.remote.GWTTagService;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.client.service.remote.GWTUserServiceAsync;
import com.aavu.client.widget.browse.BrowseView;
import com.aavu.client.widget.edit.AddEditView;
import com.aavu.client.widget.edit.TopicCompleter;
import com.aavu.client.widget.tags.TagOrganizerView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HippoTest implements EntryPoint, HistoryListener {

	private DockPanel panel = new DockPanel();
	private TabPanel viewContainer = new TabPanel();
	private HTML description = new HTML();

	
	
	private TagOrganizerView tagView;
	private AddEditView addEditView;
	private BrowseView browseView;
	
	private String msg = "";
	
	private User user;
	
	private HippoCache hippoCache;
	private Manager manager;
	
	
	private void initServices(){
		//if(9==9)
		//throw new RuntimeException("sdfs");
		
		//Window.alert("1");
		GWTTopicServiceAsync topicService;
		GWTTagServiceAsync tagService;
		GWTUserServiceAsync userService;
		
		topicService = (GWTTopicServiceAsync) GWT.create(GWTTopicService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) topicService;
		//endpoint.setServiceEntryPoint("http://localhost:8080/HippoTestW/service/TopicService");		
		
		//Window.alert("2");
		
		String pre = "";
		if(GWT.isScript()){
			pre = GWT.getModuleBaseURL();//HippoTest/service/topicService
			pre = "http://www.myhippocampus.com/HippoTest/service/";
		//	pre = "http://localhost:8080/HippoTest/service/";
			
		}else{
			pre = GWT.getModuleBaseURL();
			pre = "http://localhost:8080/HippoTest/service/";
		}
		//Window.alert("3");
		msg = pre+" "+GWT.isScript()+" "+(pre + "topicService");
		
		endpoint.setServiceEntryPoint(pre + "topicService");		
		
		tagService = (GWTTagServiceAsync) GWT.create(GWTTagService.class);
		ServiceDefTarget endpointTAG = (ServiceDefTarget) tagService;
		endpointTAG.setServiceEntryPoint(pre + "tagService");
		
		
		userService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
		ServiceDefTarget endpointUser = (ServiceDefTarget) userService;
		endpointUser.setServiceEntryPoint(pre + "userService");
		
		//Window.alert("4");

		final String user_endpoint_debug = pre + "userService";
		
		hippoCache = new HippoCache(topicService,tagService,userService);
		
		manager = new Manager(hippoCache);
		
		//static service setters.
		//hopefully replace with Spring DI
		//
		TopicCompleter.setTopicService(hippoCache.getTopicCache());
		MetaTopicList.setCache(hippoCache);
		
		
		//Window.alert("5");
				
		userService.getCurrentUser(new AsyncCallback(){
			public void onSuccess(Object result) {
				user = (User) result;

				if(user != null)
				System.out.println("found a user: "+user.getUsername());				
				loadGUI();
			}

			public void onFailure(Throwable caught) {
				if(GWT.isScript()){
					Window.alert("GetCurrentUser failed! "+caught+" \nEP:"+user_endpoint_debug);
				}
				loadGUI();
			}			
			
		});
		
		//Window.alert("6");
	}


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		try{
			initServices();		
			
			System.out.println("Module load");
			
			String initToken = History.getToken();
		    if (initToken.length() > 0){
		      onHistoryChanged(initToken); 
		    }
		    
		    History.addHistoryListener(this);
		    
		}catch(Exception e){
			Window.alert("e: "+e);
			System.out.println("Problem initting services! "+e);
			
			VerticalPanel panel = new VerticalPanel();
			
			panel.add(new Label("Error"));
			panel.add(new Label(e.getMessage()));
			
			RootPanel.get().add(panel);
			
		}
		
	}



	private void loadGUI() {

		String username="none";
		if(user != null){
			username = user.getUsername();
		}
		Label title = new Label("Add Article "+msg+" ||"+" &-"+username);

		
		MainMap mainMap = new MainMap(manager);
		RootPanel.get().add(mainMap);

	
	}

	public void onHistoryChanged(String historyToken) {
	    // This method is called whenever the application's history changes. Set
	    // the label to reflect the current history token.
		System.out.println("history changed to "+historyToken);
		manager.gotoTopic(historyToken);	    
	  }
	

}
