package com.aavu.client.widget.edit;

import com.aavu.client.domain.Tag;
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

public class DeletableTopicLabel extends ActionableTopicLabel implements ClickListener {

	private RemoveListener remover;
	private Tag tag;


	public DeletableTopicLabel(final Tag tag, final RemoveListener remover) {
		super(tag,Manager.myConstants.deleteMe());
		this.remover = remover;
		this.tag = tag;
		addActionListener(this);
	}


	public void onClick(Widget sender) {
		remover.remove(tag,this);
	}

}
