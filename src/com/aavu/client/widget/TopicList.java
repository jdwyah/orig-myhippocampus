package com.aavu.client.widget;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.TopicServiceAsync;
import com.aavu.client.async.StdAsyncCallback;
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

	private TopicDetail topicDetail;
	


	public TopicList(TopicServiceAsync topicService,TopicDetail topicDetail){
		setTopicService(topicService);
		setTopicDetail(topicDetail);
		
		load();
		setWidget(panel);
	}
	

	public void load() {
		topicService.getAllTopics(0,0,new StdAsyncCallback() {
			
			public void onSuccess(Object result) {
				panel.clear();
				final Topic[] l = (Topic[]) result;
				for(int i=0; i<l.length; i++){
					
					TopicLabel h = new TopicLabel(l[i]);
					h.addClickListener(new ClickListener() {					
						public void onClick(Widget sender) { 
							topicDetail.load(((TopicLabel)sender).getT());
						}});
					panel.add(h);
					
				}
			}

		});	
	}


	public void setTopicService(TopicServiceAsync topicService) {
		this.topicService = topicService;
	}
	public void setTopicDetail(TopicDetail topicDetail) {
		this.topicDetail = topicDetail;
	}

}
