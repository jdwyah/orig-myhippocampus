package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.util.Logger;
import com.aavu.client.widget.edit.TopicWidget;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

public class EntryDisplay extends AbstractDraggableBubble implements TopicDisplayObj, ClickListener {


	private TopicOccurrenceConnector owl;
	private HierarchyDisplay display;

	private int unscaledHeight;
	private int unscaledWidth;
	private TopicWidget entryPreview;
	private AbsolutePanel mainPanel;

	public EntryDisplay(TopicOccurrenceConnector topicOccurrenceConnector,
			HierarchyDisplay hierarchyDisplay) {

		super(topicOccurrenceConnector.getLongitude(), topicOccurrenceConnector.getLatitude(),
				topicOccurrenceConnector.getOccurrence().getTitle(), hierarchyDisplay);

		this.owl = topicOccurrenceConnector;
		this.display = hierarchyDisplay;


		init();

		setPixelSize(unscaledWidth, unscaledHeight);

		setStyleName("H-EntryDisplay");
		// addStyleName("H-BlueFade");


		addClickListener(this);

	}

	public void onClick(Widget sender) {
		System.out.println("EntryDisplay onClick");
		display.getManager().editOccurrence(owl.getOccurrence());
	}


	public DropController getDropController() {
		// TODO Auto-generated method stub
		return null;
	}

	public Widget getDropTarget() {
		return this;
	}


	public TopicIdentifier getIdentifier() {
		return owl.getOccurrence().getIdentifier();
	}

	public String getTitle() {
		return owl.getOccurrence().getTitle();
	}



	/**
	 * TODO PEND HIGH dupe of code in AbstractDraggableBubble
	 */
	protected void saveLocation() {
		getDisplay().getManager().getTopicCache().saveOccLocationA(
				getDisplay().getCurrentRoot().getId(), owl.getOccurrence().getId(), getTop(),
				getLeft(), new StdAsyncCallback("Save Occurrence LatLong") {
				});
	}



	public void receivedDrop(TopicDisplayObj bubble) {
		// TODO Auto-generated method stub

	}


	public void update(Topic t) {
		if (t instanceof Entry) {
			Entry entry = (Entry) t;
			entryPreview.load(entry);
		} else {
			Logger.error("EntryDisplay. Update with non-entry");
		}
	}

	public void zoomToScale(double currentScale) {

		// our div
		setPixelSize((int) (unscaledWidth * currentScale), (int) (unscaledHeight * currentScale));

		// cropper. we need an absolute panel, if we're to crop.
		mainPanel.setPixelSize((int) (unscaledWidth * currentScale),
				(int) (unscaledHeight * currentScale));

		// inner widget
		entryPreview.setPixelSize((int) (unscaledWidth * currentScale),
				(int) (unscaledHeight * currentScale));

	}



	public Widget getWidget() {
		return this;
	}

	// @Override
	protected Widget getOurWidget() {
		entryPreview = new TopicWidget();
		entryPreview.load((Entry) owl.getOccurrence());

		unscaledWidth = 150;
		unscaledHeight = 50;


		mainPanel = new AbsolutePanel();

		mainPanel.add(entryPreview);

		return mainPanel;
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
