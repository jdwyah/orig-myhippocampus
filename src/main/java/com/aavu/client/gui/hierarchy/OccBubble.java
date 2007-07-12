package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class OccBubble extends AbstractDraggableBubble implements TopicDisplayObj, ClickListener {

	private TopicOccurrenceConnector owl;

	public OccBubble(TopicOccurrenceConnector owl, HierarchyDisplay display) {

		super(owl.getLongitude(), owl.getLatitude(), owl.getOccurrence().getTitle(), new Image(
				ImageHolder.getImgLoc("hierarchy/") + "ball_green.png"), display);

		this.owl = owl;

		addClickListener(this);

		// addMouseListener(new OccDisplayListener(owl.getOccurrence()));
	}

	// @Override

	public TopicIdentifier getIdentifier() {
		return owl.getOccurrence().getIdentifier();
	}

	public void onClick(Widget sender) {
		System.out.println("OccBubble onClick");
		getDisplay().getManager().editOccurrence(owl.getOccurrence());
	}

	public void receivedDrop(TopicDisplayObj bubble) {
		// TODO Auto-generated method stub

	}

	protected void saveLocation() {

		getDisplay().getManager().getTopicCache().saveOccLocationA(
				getDisplay().getCurrentRoot().getId(), owl.getOccurrence().getId(), getTop(),
				getLeft(), new StdAsyncCallback("Save Occurrence LatLong") {
				});
	}

	// @Override
	protected void hover() {
		// TODO Auto-generated method stub

	}

	// @Override
	protected void unhover() {
		// TODO Auto-generated method stub

	}

}
