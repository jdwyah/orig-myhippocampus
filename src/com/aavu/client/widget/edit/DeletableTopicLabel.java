package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;

public class DeletableTopicLabel extends ActionableTopicLabel {

	public DeletableTopicLabel(Topic topic) {
		super(topic,Manager.myConstants.deleteMe(),new ClickListener(){

			public void onClick(Widget sender) {
				Window.alert("Not implemented yet :)");
			}});
	}
}
