package com.aavu.client.gui.maps.ext;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class ListenerWidget {

	private Widget widget;
	private ClickListener listener;

	public ListenerWidget(ClickListener listener, Widget widget){
		this.listener = listener;
		this.widget = widget;
	}

	public ClickListener getListener() {
		return listener;
	}

	public Widget getWidget() {
		return widget;
	}

}
