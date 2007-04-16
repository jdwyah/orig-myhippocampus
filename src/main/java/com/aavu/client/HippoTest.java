package com.aavu.client;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.remote.GWTSubjectService;
import com.aavu.client.service.remote.GWTSubjectServiceAsync;
import com.aavu.client.service.remote.GWTTagService;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.client.service.remote.GWTUserServiceAsync;
import com.aavu.client.util.Logger;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HippoTest implements EntryPoint, HistoryListener {

	public static final String EMPTY = "-1";
	public static final String UPLOAD_PATH = "service/upload.html";//"site/secure/upload.html";
	public static final String FILE_PATH = "service/showFile.html?key=";
	
	public static final String LOCAL_HOST = "http://localhost:8080/";
	public static final String REMOTE_HOST = "http://www.myhippocampus.com/";
	public static final String MAIN_DIV = "slot1";
	
	private HippoCache hippoCache;
	private Manager manager;
	private static boolean have_initted_semaphore = false;
	

	/**
	 * Switch between localhost for testing & 
	 */
	public static String getRelativeURL(String url) {
		String realModuleBase;
		
		if(GWT.isScript()){			
			
			Logger.log("ModuleBaseURL: "+GWT.getModuleBaseURL());
			
			String moduleBase = GWT.getModuleBaseURL();
			
			//Use for Deployment to production server
			//
			realModuleBase = REMOTE_HOST;
					
			//Use to test compiled browser locally
			//
			if(moduleBase.indexOf("myhippocampus.com") == -1){	
				Logger.log("Testing. Using Localhost");
				realModuleBase = LOCAL_HOST;
			}
			
			
		}else{
			//realModuleBase = GWT.getModuleBaseURL();
			
			//This is the URL for GWT Hosted mode 
			//
			realModuleBase = LOCAL_HOST;
		}
		
		return realModuleBase+url;
	}
	
	private void initServices(){
		//if(9==9)
		//throw new RuntimeException("sdfs");
		
		//Window.alert("1");
		GWTTopicServiceAsync topicService;
		GWTTagServiceAsync tagService;
		GWTUserServiceAsync userService;
		GWTSubjectServiceAsync subjectService;
		
		topicService = (GWTTopicServiceAsync) GWT.create(GWTTopicService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) topicService;
		//endpoint.setServiceEntryPoint("http://localhost:8080/HippoTestW/service/TopicService");		
		
		//Window.alert("2");
		
		//realModuleBase = "";
	
		String pre = getRelativeURL("service/");
		
		//Window.alert("3");
		
		endpoint.setServiceEntryPoint(pre + "topicService");		
		
		tagService = (GWTTagServiceAsync) GWT.create(GWTTagService.class);
		ServiceDefTarget endpointTAG = (ServiceDefTarget) tagService;
		endpointTAG.setServiceEntryPoint(pre + "tagService");
		
		
		userService = (GWTUserServiceAsync) GWT.create(GWTUserService.class);
		ServiceDefTarget endpointUser = (ServiceDefTarget) userService;
		endpointUser.setServiceEntryPoint(pre + "userService");
		
		subjectService = (GWTSubjectServiceAsync) GWT.create(GWTSubjectService.class);
		ServiceDefTarget endpointSubject = (ServiceDefTarget) subjectService;
		endpointSubject.setServiceEntryPoint(pre+ "subjectService");
		
		
		//Window.alert("4");
		if(topicService == null
				||
				tagService == null
				|| 
				userService == null
				|| 
				subjectService == null){
			Logger.error("Service was null.");
		}
		
		hippoCache = new HippoCache(topicService,tagService,userService,subjectService);
		

		manager = new Manager(hippoCache);

		
		//static service setters.
		//hopefully replace with Spring DI
		//
		//TopicCompleter.setTopicService(hippoCache.getTopicCache());
		MetaDate.setTopicService(hippoCache.getTopicCache());
		StdAsyncCallback.setManager(manager);
		
		//Window.alert("5");
		
		loadGUI(manager.getRootWidget());
		
		manager.setup("ORIG");
		
	}

	private void loadGUI(Widget widget) {
		RootPanel.get("loading").setVisible(false);
		RootPanel.get(MAIN_DIV).add(widget);					
	}


	/**
	 * This is the entry point method.
	 * 
	 * -T-ODO HIGH we're running map setup TWICE. once 
	 * from LoginWindow. secondly because onModuleLoad() is getting called 
	 * right after the login. Why is this? It couldn't be the addition of the  
	 * iframe code could it?
	 * 
	 * SOLVED. It was the IFRAME. let's make another EntryPoint that just pre-loads 
	 * stuff.
	 * 
	 * NOTE the semaphore code below is inefectual even with the var static.
	 * have_initted is false both times.
	 * It looks like we're re-initting the class. this reports different objectIDs
	 * 
	 * 
	 * 
	 */
	public void onModuleLoad() {

		try{
			System.out.println("Module load "+have_initted_semaphore+" "+this);
			if(!have_initted_semaphore){
				
				have_initted_semaphore = true;
				System.out.println("set to "+have_initted_semaphore);
				
				initServices();		

				String initToken = History.getToken();
				if (initToken.length() > 0){
					onHistoryChanged(initToken); 
				}

				History.addHistoryListener(this);
				
				
			}
		    
		}catch(Exception e){
			Logger.log("e: "+e);
					
			e.printStackTrace();
			
			VerticalPanel panel = new VerticalPanel();
			
			panel.add(new Label("Error"));
			panel.add(new Label(e.getMessage()));
			
			RootPanel.get("loading").setVisible(false);
			RootPanel.get("slot1").add(panel);
			
		}
		
	}



	public void onHistoryChanged(String historyToken) {
	    // This method is called whenever the application's history changes. Set
	    // the label to reflect the current history token.
		System.out.println("history changed to "+historyToken);
		
		//manager == null if we open directly to the page with a #link
		//
		if(historyToken != EMPTY && manager != null){
			manager.gotoTopic(historyToken);
			
			/* huh... seems to be fine in IE, but FF fires a reload and the request fails.
			 * change to "-1"
			 * 
			 * also if we open topic 193. then close the window. then open 193, it will think we've
			 * already opened it. 		
			 */
			History.newItem(EMPTY);
		}
	  }


	

}