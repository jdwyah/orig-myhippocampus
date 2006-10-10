package com.aavu.client.widget;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopicLink extends Composite {

	public TopicLink(final Topic to) {
		this(to.getTitle(),to.getId());
	}

	public TopicLink(TopicIdentifier topic) {
		this(topic.getTopicTitle(), topic.getTopicID());
	}
	
	private TopicLink(String title, final long id){
		Label l = new Label(title);
		l.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				History.newItem(id+"");
			}});
		l.setStyleName("H-TopicLink");		
		initWidget(l);
	}
}
