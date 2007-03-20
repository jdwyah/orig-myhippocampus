package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.DeleteButton;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DeletableTopicLabel extends Composite implements ClickListener {

	private RemoveListener remover;
	private Topic topic;


	public DeletableTopicLabel(final Topic topic, final RemoveListener remover) {
		this.remover = remover;		
		this.topic = topic;
		
		HorizontalPanel mainP = new HorizontalPanel();
		TopicLink link = new TopicLink(topic);
		DeleteButton deleteButton = new DeleteButton();
		deleteButton.addClickListener(this);
		
		mainP.add(link);
		mainP.add(deleteButton);
		
		initWidget(mainP);
	}


	public void onClick(Widget sender) {
		remover.remove(topic,this);
	}

}
