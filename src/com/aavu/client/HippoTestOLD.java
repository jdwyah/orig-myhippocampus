package com.aavu.client;


import com.aavu.client.domain.Topic;
import com.aavu.client.widget.ComposeView;
import com.aavu.client.widget.TopicList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HippoTestOLD implements EntryPoint {

	/**
	 * This is the entry point method.
	 */

	private TextArea title = new TextArea();
	private TextBox text = new TextBox();
	private TextArea rpc = new TextArea();
	private ComposeView cv;
	
	private TopicServiceAsync topicService;

	private void initServices(){
		topicService = (TopicServiceAsync) GWT.create(TopicService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) topicService;
		//endpoint.setServiceEntryPoint("http://localhost:8080/HippoTestW/service/TopicService");
		endpoint.setServiceEntryPoint("/topicService");

		
		
	}
	
	
	public void onModuleLoad() {

		initServices();
		
		
		
		
		
		
		
		VerticalPanel panel = new VerticalPanel();
	
		panel.setSpacing(8);

		TopicList tl = new TopicList(topicService);
		//TopicDetail td = new TopicDetail(null);
		
		cv = new ComposeView();
		cv.setTopicServiceA(topicService);
		tl.setComposeView(cv);		
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		Button addNew = new Button("Add New");
		addNew.addClickListener(new ClickListener() {					
			public void onClick(Widget sender) { 
				cv.load(new Topic());
			}});
		buttonPanel.add(addNew);
		
		panel.add(buttonPanel);
		panel.add(tl);
	//	panel.add(cv);
		
		

		RootPanel.get("s3").add(panel);
	}
}
