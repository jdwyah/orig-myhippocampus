package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopicLink extends Label {

	private TopicDetail topicDetail;
	private Topic topic;
	public TopicLink(Topic _topic,TopicDetail _topicDetail){
		this.topicDetail = _topicDetail;
		this.topic = _topic;
		
		addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				topicDetail.load(topic);				
			}});
		
		setText(topic.getTitle());
		setStyleName("a");
	}
}
