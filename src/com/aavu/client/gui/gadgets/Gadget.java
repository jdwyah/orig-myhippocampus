package com.aavu.client.gui.gadgets;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class Gadget extends Composite {

	//@Override
	protected void initWidget(Widget widget) {
		super.initWidget(widget);
		addStyleName("H-Gadget");
	}
	
	public abstract int load(Topic topic);
	
}
