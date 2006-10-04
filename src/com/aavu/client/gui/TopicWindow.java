package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;

public class TopicWindow extends PopupWindow {

	public TopicWindow(Manager manager,Topic t) {
		super(t.getTitle());

		TopicViewAndEditWidget widg = new TopicViewAndEditWidget(manager);
		widg.load(t);
		
		setContentPanel(widg);
		
	}

}
