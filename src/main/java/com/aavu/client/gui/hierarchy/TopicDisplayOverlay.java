package com.aavu.client.gui.hierarchy;

import java.util.Iterator;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class TopicDisplayOverlay extends PopupPanel {

	public TopicDisplayOverlay(Topic topic) {

		super(true);

		AbsolutePanel mainP = new AbsolutePanel();

		mainP.setPixelSize(400, 400);

		//
		// mainP.add(new Label("Crying of Lot 49"), 0, 50);
		//
		// mainP.add(new Label("Shrek"), 75, 50);
		//
		// mainP.add(new Label("X-Men"), 150, 50);

		int dx = 0;
		for (Iterator iterator = topic.getInstances().iterator(); iterator.hasNext();) {

			TopicTypeConnector conn = (TopicTypeConnector) iterator.next();
			Topic child = conn.getTopic();
			mainP.add(new Label(child.getTitle()), dx, 50);
			dx += 50;
		}

		mainP.add(new Label("foo"), 150, 50);

		add(mainP);
	}

}
