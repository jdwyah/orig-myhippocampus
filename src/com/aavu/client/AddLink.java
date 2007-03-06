package com.aavu.client;

import java.util.List;

import org.gwm.client.GDesktopPane;
import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.impl.DefaultGFrame;
import org.gwtwidgets.client.util.WindowUtils;

import com.aavu.client.LinkPlugin.AddLinkManager;
import com.aavu.client.domain.User;
import com.aavu.client.gui.ext.DefaultGInternalFrameHippoExt;
import com.aavu.client.gui.ext.LocationSetter;
import com.aavu.client.service.LoginListener;
import com.aavu.client.service.LoginService;
import com.aavu.client.service.remote.GWTTagService;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicService;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.client.service.remote.GWTUserServiceAsync;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.strings.Consts;
import com.aavu.client.util.Logger;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AddLink implements EntryPoint {

	public static final String EMPTY = "-1";
	public static final String UPLOAD_PATH = "service/upload.html";//"site/secure/upload.html";
	public static final String FILE_PATH = "service/showFile.html?key=";
	
	public static final String LOCAL_HOST = "http://localhost:8080/";
	public static final String REMOTE_HOST = "http://www.myhippocampus.com/";
	public static final String MAIN_DIV = "slot1";
	private static final String FRAME_DIV = "frameSlot";
	
	
	private static boolean have_initted_semaphore = false;

	
	private User user;

	/**
	 * Switch between localhost for testing & 
	 */
	public static String getRelativeURL(String url) {
		String realModuleBase;
		
		if(GWT.isScript()){			
			
			Logger.log("ModuleBaseURL: "+GWT.getModuleBaseURL());
			
			//Use for Deployment to production server
			//
			realModuleBase = REMOTE_HOST;
			//realModuleBase = REMOTE_HOST + "DEV/";
					
			//Use to test compiled browser locally
			//
			realModuleBase = LOCAL_HOST;
			
		}else{
			//realModuleBase = GWT.getModuleBaseURL();
			
			//This is the URL for GWT Hosted mode 
			//
			realModuleBase = LOCAL_HOST;
		}
		
		return realModuleBase+url;
	}


	private GWTTopicServiceAsync topicService;
	private GWTTagServiceAsync tagService;
	private GWTUserServiceAsync userService;
	private AddLinkManager addL;
	private String url;
	private String notes;
	private String description;

	
	public AddLink(){
		initConstants();
	}
	
	private boolean initServices(){
		//if(9==9)
		//throw new RuntimeException("sdfs");
		
		//Window.alert("1");
				
		topicService = (GWTTopicServiceAsync) GWT.create(GWTTopicService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) topicService;
		
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
		
		
		//Window.alert("4");
		if(topicService == null
				||
				tagService == null
				|| 
				userService == null){
			Logger.error("Service was null.");
			return false;
		}
		
		return true;
		
		
	}
	private void initConstants() {
		ConstHolder.myConstants = (Consts) GWT.create(Consts.class);
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
	 */
	public void onModuleLoad() {		
		
		url = WindowUtils.getLocation().getParameter("url");
		notes = WindowUtils.getLocation().getParameter("notes");
		description = WindowUtils.getLocation().getParameter("description");
		
		System.out.println("url "+url+" notes "+notes+" desc "+description);
		
		try{
			
			if(initServices()){
				
				addL = new AddLinkManager(topicService,tagService,url,notes,description);
				
				init();
				
				
				//loadGUI(addL.getWidget());
				
//				
//				userService.getCurrentUser(new AsyncCallback(){
//					public void onSuccess(Object result) {
//						
//						user = (User) result;
//						
//						if(user != null){
//							System.out.println("found a user: "+user.getUsername());							
//							loadGUI(new AddLinkPanel(addL,url,notes,description));							
//						}else{						
//							doLogin();							
//						}
//					}
//
//					public void onFailure(Throwable caught) {
//						doLogin();
//					}});
						
			}else{
				fail(new Exception("Service Load Failure"));
			}
		
		}catch(Exception e){
			fail(e);
		}
		
	}

	/**
	 * Actual GUI initialization
	 * addL will try to lookup the URL, if not logged in that call will fail and we'll
	 * run doLogin() here.
	 * 
	 * If that call suceeds, we'll be returned the AddLinkPanel widget.
	 */
	private void init(){		
		System.out.println("init()");
		addL.init(new AsyncCallback(){
			public void onSuccess(Object result) {		
				System.out.println("init success -> loadGUI");
				loadGUI((Widget) result);												
			}

			public void onFailure(Throwable caught) {
				System.out.println("init fail -> do login");
				doLogin();
			}});
	}
	
	/**
	 * should only be used in testing where we're not already logged in
	 * since acegi should ensure we're logged in for this
	 * 
	 * That's why the stub impls are ok.
	 * 
	 */
	private void doLogin(){		
		GInternalFrame frame = new DefaultGInternalFrameHippoExt("",new LocationSetter(){
			public void setLocation(DefaultGInternalFrameHippoExt ext, int left, int top) {}
				});
		
		frame.setDesktopPane(new GDesktopPane(){
			public void addFrame(GInternalFrame internalFrame) {}
			public void closeAllFrames() {}
			public void deIconify(GFrame minimizedWindow) {}
			public List getAllFrames() {return null;}
			public void iconify(GFrame internalFrame) {}
			});
		
		RootPanel.get(FRAME_DIV).add((DefaultGFrame) frame);
		
		LoginService.doLogin(frame, new LoginListener(){
			public void loginSuccess() {
				init();
			}});
	}
	
	private void fail(Exception e) {
		Logger.log("e: "+e);
		
		e.printStackTrace();
		
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(new Label("Error"));
		panel.add(new Label(e.getMessage()));
		
		RootPanel.get("loading").setVisible(false);
		RootPanel.get("slot1").add(panel);
		
	}




	

}
