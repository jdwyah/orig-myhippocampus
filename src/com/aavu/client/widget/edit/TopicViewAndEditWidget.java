package com.aavu.client.widget.edit;


import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.aavu.client.service.remote.GWTTopicServiceAsync;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicViewAndEditWidget extends Composite implements ClickListener{
	
	
	private Button editTextButton = new Button("Edit Text");
	private Button cancelButton = new Button("Cancel");	
		
	private TopicWidget topicWidget;
	private TopicEditWidget topicEditWidget;

	private VerticalPanel topicPanel;
	private VerticalPanel lp;
	
	public Topic topic;
	private TopicList topicList;
	private HippoCache hippoCache;	
	
	
	public TopicViewAndEditWidget(HippoCache hippoCache) {
		this.hippoCache = hippoCache;
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		
		lp = new VerticalPanel();
		
		topicPanel = new VerticalPanel();
		
		cancelButton.addClickListener(this);
		editTextButton.addClickListener(this);
		
		mainPanel.add(lp);
		mainPanel.add(topicPanel);
		
		
		initWidget(mainPanel);
	}	
	
	
	public void load(Topic topic){
		this.topic = topic;
		topicWidget = new TopicWidget(topic);
		topicEditWidget = new TopicEditWidget(this,hippoCache,topic);
		
		activateMainView();

		Effect.highlight(topicPanel);
	}
		
	

	public void onClick(Widget source){
		if (source == cancelButton){
			activateMainView();
		}
		else if (source == editTextButton){
			activateEditView();
		}
		
	}
	
	public void activateMainView(){
	
		topicPanel.clear();
		topicPanel.add(topicWidget);				

		lp.clear();
		lp.add(editTextButton);
		
	}
	
	public void activateEditView() {
		
		topicPanel.clear();
		topicPanel.add(topicEditWidget);				
				
		lp.clear();
		lp.add(cancelButton);
	}


	public void setTopicList(TopicList topicList) {
		this.topicList = topicList;
	}
	
	

	public void save(Topic topic2) {
		
		load(topic2);
		
		hippoCache.getTopicCache().save(topic2, new StdAsyncCallback("topicDetail save") {				
			
			public void onSuccess(Object result) {																	
				topicList.load();
				activateMainView();
			}
		});	

	}
	
}
