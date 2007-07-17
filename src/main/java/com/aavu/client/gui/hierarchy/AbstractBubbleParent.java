package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractBubbleParent extends AbstractDraggableBubble {

	private IslandBanner banner;
	private int currentHeight;
	private int currentWidth;

	private Image image;


	private AbsolutePanel mainPanel;

	public AbstractBubbleParent(int longitudeOnIsland, int latitudeOnIsland, String topicTitle,
			Image image, HierarchyDisplay display) {
		super(longitudeOnIsland, latitudeOnIsland, topicTitle, display);
		this.image = image;

		init();
	}

	protected Widget getOurWidget() {


		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(getUnscaledWidth(), getUnscaledHeight());


		this.image.setPixelSize(getUnscaledWidth(), getUnscaledHeight());

		// System.out.println("AbstractBubble left "+left+" top "+top);

		banner = new IslandBanner(getTitle(), 5);

		mainPanel.add(image, 0, 0);
		mainPanel.add(banner, 0, 0);

		return mainPanel;
	}

	protected abstract int getUnscaledWidth();

	protected abstract int getUnscaledHeight();

	public int getCurrentHeight() {
		return currentHeight;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}

	public Widget getDropTarget() {
		return image;
	}

	public void update(Topic t) {
		banner.setText(t.getTitle());
	}

	public IslandBanner getBanner() {
		return banner;
	}

	public void zoomToScale(double currentScale) {

		image.setPixelSize((int) (getUnscaledWidth() * currentScale),
				(int) (getUnscaledHeight() * currentScale));

		Widget minimumWidget = banner.setToZoom(currentScale);

		currentWidth = (minimumWidget.getOffsetWidth() > (int) (getUnscaledWidth() * currentScale)) ? minimumWidget
				.getOffsetWidth()
				: (int) (getUnscaledWidth() * currentScale);

		currentHeight = ((int) (getUnscaledHeight() * currentScale) < minimumWidget
				.getOffsetHeight()) ? minimumWidget.getOffsetHeight()
				: (int) (getUnscaledHeight() * currentScale);

		// System.out.println("w "+correctedWidth+" h "+correctedHeight);



		mainPanel.setPixelSize(currentWidth, currentHeight);

	}
}
