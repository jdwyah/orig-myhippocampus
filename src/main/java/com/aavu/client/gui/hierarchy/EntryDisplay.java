package com.aavu.client.gui.hierarchy;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.DblClickListener;
import com.aavu.client.util.Logger;
import com.allen_sauer.gwt.dragdrop.client.HasDragHandle;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

public class EntryDisplay extends AbstractDraggableBubble implements TopicDisplayObj,
		HasDragHandle, ResizeHandler, DblClickListener {

	private static final int DEF_HEIGHT = 0;

	private static final int DEF_WIDTH = 0;

	private static final double MAX_FONT = .8;

	private static final double MIN_FONT = .4;

	private static final double SHOW_ZOOM_BACK = .50;



	private HierarchyDisplay display;
	private Entry entry;
	private EntryRichText entryPreview;
	private double lastScale;

	private AbsolutePanel mainPanel;

	private TopicOccurrenceConnector owl;

	private int unscaledHeight;
	private int unscaledWidth;



	public EntryDisplay(TopicOccurrenceConnector topicOccurrenceConnector,
			HierarchyDisplay hierarchyDisplay) {

		super(topicOccurrenceConnector.getLongitude(), topicOccurrenceConnector.getLatitude(),
				topicOccurrenceConnector.getOccurrence().getTitle(), hierarchyDisplay);

		this.entry = (Entry) topicOccurrenceConnector.getOccurrence();
		this.owl = topicOccurrenceConnector;
		this.display = hierarchyDisplay;



		init();

		setPixelSize(unscaledWidth, unscaledHeight);

		setStyleName("H-EntryDisplay");
		// addStyleName("H-BlueFade");


	}

	// public void onClick(Widget sender) {
	// System.out.println("EntryDisplay onClick");
	// display.getManager().editOccurrence(owl.getOccurrence());
	// }


	// @Override
	protected void clickAction() {
		System.out.println("\n\nEntryDisplay click Action");
		showDetails(getLastClickEventCtrl());
	}

	/**
	 * only drag with the top row so that the resize still works
	 */
	// @Override
	public Widget getDragHandle() {
		return entryPreview.getDragHandle();
	}


	public DropController getDropController() {
		// TODO Auto-generated method stub
		return null;
	}

	public Widget getDropTarget() {
		return this;
	}



	public Entry getEntry() {
		return entry;
	}

	public double getFontFor(int size, double zoom) {

		double s = MIN_FONT + .4 * zoom;

		s = s > MAX_FONT ? MAX_FONT : s;

		return s;
	}


	public TopicIdentifier getIdentifier() {
		return owl.getOccurrence().getIdentifier();
	}

	// @Override
	protected Widget getOurWidget() {
		entryPreview = new EntryRichText(this, display);
		Entry e = (Entry) owl.getOccurrence();
		entryPreview.load(e);

		unscaledWidth = e.getWidth();
		unscaledHeight = e.getHeight();


		mainPanel = new AbsolutePanel();

		mainPanel.add(entryPreview);

		return mainPanel;
	}

	public String getTitle() {
		return owl.getOccurrence().getTitle();
	}

	public Topic getTopic() {
		return entry;
	}

	public void onDblClick(Widget sender) {
		getDisplay().getManager().editOccurrence(entry);
	}


	public void receivedDrop(TopicDisplayObj bubble) {
		// TODO Auto-generated method stub

	}

	public void resize(int width, int height) {
		unscaledHeight = (int) (height / lastScale);
		unscaledWidth = (int) (width / lastScale);

		zoomToScale(lastScale);
	}


	/**
	 * TODO PEND HIGH dupe of code in AbstractDraggableBubble
	 */
	protected void saveLocation() {
		getDisplay().getManager().getTopicCache().saveOccLocationA(
				getDisplay().getCurrentRoot().getId(), owl.getOccurrence().getId(), getTop(),
				getLeft(), new StdAsyncCallback("Save Occurrence LatLong") {
				});

		// if they just click and drag without an onclick, we won't select w/o this
		clickAction();
	}

	//
	public void setSelected(boolean b) {

		// TODO
	}

	private void showDetails(boolean ctrlKey) {
		getDisplay().doSelection(owl.getOccurrence(), ctrlKey);
	}


	public void update(Topic t) {
		if (t instanceof Entry) {
			entry = (Entry) t;

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


		double font_size = getFontFor(1, currentScale);

		entryPreview.setTextSize(font_size);


		lastScale = currentScale;



	}


}
