package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.GoogleData;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.google.gwt.user.client.Window;

public class GoogOccBubble extends OccBubble {

	public GoogOccBubble(TopicOccurrenceConnector owl, String string, int width, int height,
			HierarchyDisplay display) {
		super(owl, string, width, height, display);

	}


	// @Override
	protected void dblClick() {
		super.dblClick();
		GoogleData data = (GoogleData) owl.getOccurrence();
		Window.open(data.getUri(), data.getTitle(), null);
	}

}
