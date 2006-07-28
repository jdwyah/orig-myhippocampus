package com.aavu.client;

import com.aavu.client.domain.Topic;
import com.aavu.client.widget.browse.BrowseView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SourcesTabEvents;
import com.google.gwt.user.client.ui.TabListener;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HippoTest implements EntryPoint, TabListener  {

	private DockPanel panel = new DockPanel();
	private TabPanel viewContainer = new TabPanel();
	private HTML description = new HTML();
	private TagBoard tagBoard;

	private TopicServiceAsync topicService;
	private TagServiceAsync tagService;
	
	
	private TagOrganizerView tagView;
	private ComposeViewVal composeView;
	private BrowseView browseView;
	
	private void initServices(){
		topicService = (TopicServiceAsync) GWT.create(TopicService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) topicService;
		//endpoint.setServiceEntryPoint("http://localhost:8080/HippoTestW/service/TopicService");
		endpoint.setServiceEntryPoint("/topicService");
		
		tagService = (TagServiceAsync) GWT.create(TagService.class);
		ServiceDefTarget endpointTAG = (ServiceDefTarget) tagService;
		endpointTAG.setServiceEntryPoint("/tagService");
		
		
	}


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		initServices();		
		tagBoard = new TagBoard(tagService);


		Label title = new Label("Add Article");
		title.setStyleName("ta-Title");
		HorizontalPanel titlePanel = new HorizontalPanel();
		titlePanel.add(title);
		titlePanel.setCellWidth(title, "100%");

		DockPanel logoPanel = new DockPanel();
		Image logo = new Image("images/HippoLogo.jpg");
		//logo.setSize();
		logoPanel.add(logo);
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
		viewContainer.setStyleName("ks-Sink");

		VerticalPanel vp = new VerticalPanel();
		vp.setWidth("100%");
		vp.add(description);
		vp.add(viewContainer);	    

		description.setStyleName("ks-Info");

		panel.add(top, DockPanel.NORTH);
		panel.add(tagBoard, DockPanel.WEST);
		panel.add(vp, DockPanel.CENTER);

		panel.setCellVerticalAlignment(tagBoard, HasAlignment.ALIGN_TOP);
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


		tagView = new TagOrganizerView(tagService);
		composeView = new ComposeViewVal();
		browseView = new BrowseView(topicService,tagService);
		
		viewContainer.add(composeView,"Edit Topic");
		viewContainer.add(tagView, "Manage Tags");
		viewContainer.add(browseView, "Browse");

		viewContainer.selectTab(0);
	}



	//*******  TabListener methods  *******

	public boolean onBeforeTabSelected(SourcesTabEvents sender, int tabIndex){
		return true;
	}
	public void onTabSelected(SourcesTabEvents sender, int tabIndex){

		Widget w = viewContainer.getWidget(tabIndex);
		
		if(w == tagView){
			((TagOrganizerView)viewContainer.getWidget(1)).populateTagList();	
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
