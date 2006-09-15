package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.google.gwt.user.client.ui.impl.PopupImpl;
import com.google.gwt.user.client.ui.impl.PopupImplIE6;

public class TopicWindow extends PopupWindow {

	public TopicWindow(HippoCache hippoCache,Topic t) {
		super(t.getTitle());

		TopicViewAndEditWidget widg = new TopicViewAndEditWidget(hippoCache);
		widg.load(t);
		
		setContentPanel(widg);
		
	}

}
