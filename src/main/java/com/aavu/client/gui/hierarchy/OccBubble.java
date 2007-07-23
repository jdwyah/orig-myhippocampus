package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class OccBubble extends AbstractBubbleParent implements TopicDisplayObj, ClickListener {

	private TopicOccurrenceConnector owl;
	private int unscaledWidth;
	private int unscaledHeight;

	/**
	 * default to the green ball image
	 * 
	 * @param owl
	 * @param display
	 */
	public OccBubble(TopicOccurrenceConnector owl, HierarchyDisplay display) {
		this(owl, ImageHolder.getImgLoc("hierarchy/") + "ball_green.png", 50, 50, display);
	}

	public OccBubble(TopicOccurrenceConnector owl, String imgLocation, int width, int height,
			HierarchyDisplay display) {
		super(owl.getLongitude(), owl.getLatitude(), owl.getOccurrence().getTitle(), new Image(
				imgLocation), display);

		this.owl = owl;
		this.unscaledWidth = width;
		this.unscaledHeight = height;

		addClickListener(this);
	}

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

	// @Override
	protected int getUnscaledHeight() {
		return unscaledHeight;
	}

	// @Override
	protected int getUnscaledWidth() {
		return unscaledWidth;
	}

	// @Override
	protected void hideDetails() {
		// TODO Auto-generated method stub

	}

	// @Override
	protected void showDetails() {
		// TODO Auto-generated method stub

	}

	// @Override
	protected boolean isDetailsShowing() {
		// TODO Auto-generated method stub
		return false;
	}

}
