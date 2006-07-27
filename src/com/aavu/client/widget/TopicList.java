package com.aavu.client.widget;

import com.aavu.client.TopicServiceAsync;
import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicList extends Composite {


	private TopicServiceAsync topicService;

	private VerticalPanel panel = new VerticalPanel();

	private ComposeView composeView;
	
	public void setComposeView(ComposeView cv) {
		this.composeView = cv;
	}


	public TopicList(TopicServiceAsync topicService){
		this.topicService = topicService;
		load();
		setWidget(panel);
	}
	

	private void load() {
		topicService.getAllTopics(0,0,new AsyncCallback() {
			public void onFailure(Throwable caught) {
				panel.clear();
				panel.add(new Label("Error "+caught));	    	            
			}

			public void onSuccess(Object result) {
				panel.clear();
				final Topic[] l = (Topic[]) result;
				for(int i=0; i<l.length; i++){
					
					TopicLabel h = new TopicLabel(l[i]);
					h.addClickListener(new ClickListener() {					
						public void onClick(Widget sender) { 
							composeView.load(((TopicLabel)sender).getT());
						}});
					panel.add(h);
					
				}
			}

		});	
	}


	public void setTopicService(TopicServiceAsync topicService) {
		this.topicService = topicService;
	}

}
