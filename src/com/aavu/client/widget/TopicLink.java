package com.aavu.client.widget;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.TooltipListener;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TopicLink extends Composite {

	private static final int DEFAULT_MAX_STRING = 40;

	public TopicLink(final Topic to) {
		this(to.getTitle(),to.getId());
	}

	public TopicLink(TopicIdentifier topic) {
		this(topic.getTopicTitle(), topic.getTopicID());
	}
	private TopicLink(String title, final long id){
		this(title,id,DEFAULT_MAX_STRING);
	}
	public TopicLink(TopicIdentifier topic, int maxStringLength) {
		this(topic.getTopicTitle(),topic.getTopicID(),maxStringLength);
	}

	private TopicLink(String title, final long id, int maxStringLength){

		Label l = null;
		if(title.length() > maxStringLength){
			l = new Label(title.substring(0, maxStringLength-3)+"...");
			l.addMouseListener(new TooltipListener(0,0,title));
		}else{
			l = new Label(title);
		}
		l.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				History.newItem(id+"");
			}});


		l.setStyleName("H-TopicLink");

		initWidget(l);
	}

}
