package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class DeletableTopicLabel extends ActionableTopicLabel implements ClickListener {

	private RemoveListener remover;
	private Topic topic;


	public DeletableTopicLabel(final Topic topic, final RemoveListener remover) {
		super(topic,ConstHolder.myConstants.deleteMe());
		this.remover = remover;
		this.topic = topic;
		addActionListener(this);
	}


	public void onClick(Widget sender) {
		remover.remove(topic,this);
	}

}
