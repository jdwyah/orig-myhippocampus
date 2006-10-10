package com.aavu.client.widget;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HeaderLabel extends Composite {

	public HeaderLabel(String string) {
		Label l = new Label(string);		
		l.setStyleName("H-HeaderLabel");		
		initWidget(l);
	}
}
