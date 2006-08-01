package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.ui.Label;

public class TopicLabel extends Label {

	private Topic t;
	
	public TopicLabel(Topic t){
		this.t = t;
		setText(t.getTitle());
	}

	public Topic getT() {
		return t;
	}
	
}
