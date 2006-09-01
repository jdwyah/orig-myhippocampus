package com.aavu.client;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.MetaTopicList;
import com.aavu.client.domain.User;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DockPanel.DockLayoutConstant;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HippoTest implements EntryPoint, TabListener  {

	private DockPanel panel = new DockPanel();
	private TabPanel viewContainer = new TabPanel();
	private HTML description = new HTML();

	
	
	private TagOrganizerView tagView;
	private AddEditView addEditView;
	private BrowseView browseView;
	
	private String msg = "";
	
	private User user;
	
	private HippoCache hippoCache;
	
	
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
		title.setStyleName("ta-Title");
		HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.add(title);
		titlePanel.setCellWidth(title, "100%");

		DockPanel logoPanel = new DockPanel();
		Image logo = new Image("images/HippoLogo.jpg");
		//logo.setSize();
		
		logoPanel.add(logo,DockPanel.WEST);
		logoPanel.setStyleName("ta-logo");

		HorizontalPanel top = new HorizontalPanel();
		top.add(logoPanel);
		top.add(titlePanel);
		top.setCellWidth(logoPanel,"20%");
		top.setCellWidth(titlePanel,"80%");
		top.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
		top.addStyleName("ta-TopPanel");

		viewContainer = new TabPanel();
		viewContainer.addTabListener(this);
		//viewContainer.setStyleName("ks-Sink");

		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.add(description);
		vp.add(viewContainer);	    

		//description.setStyleName("ks-Info");

		panel.add(top, DockPanel.NORTH);

		panel.add(vp, DockPanel.CENTER);

		panel.setCellWidth(vp, "100%");

		RootPanel.get().add(panel);

		// Show the initial screen.
		/* String initToken = History.getToken();
		 if (initToken.length() > 0)
		 onHistoryChanged(initToken);
		 else
		 showInfo();*/
		//viewContainer.add(ComposeView.init().getInstance(), "Edit Topic");
		//viewContainer.add(TagOrganizerView.init().getInstance(), "Manage Tags");


		tagView = new TagOrganizerView(hippoCache);

		addEditView = new AddEditView(hippoCache);
		
		browseView = new BrowseView(hippoCache);
		
		viewContainer.add(addEditView, "Main");
		viewContainer.add(browseView, "Browse");				
		viewContainer.add(tagView, "Tags");

		viewContainer.selectTab(0);	
	}


	//*******  TabListener methods  *******

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex){
		return true;
	}
	public void onTabSelected(SourcesTabEvents sender, int tabIndex){

		Widget w = viewContainer.getWidget(tabIndex);
		
		if(w == tagView){
			((TagOrganizerView)w).populateTagList();	
		}
		
		
		
	}

	//**** End TabListener methods *******

	private class EditListener implements ClickListener {
		public void onClick(Widget source){
			if (((Button)source).getText()=="Preview")
			{
				//show(FinalizeView.init(this));
			}				  
		}
	}
}
