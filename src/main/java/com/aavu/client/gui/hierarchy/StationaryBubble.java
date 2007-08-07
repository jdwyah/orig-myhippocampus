package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.GUIManager;
import com.aavu.client.gui.ocean.dhtmlIslands.ImageHolder;
import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class StationaryBubble extends AbstractBubble {

	private TopicIdentifier ti;
	private IslandBanner banner;


	private Image image;

	private AbsolutePanel mainPanel;
	private int unscaledHeight;
	private int unscaledWidth;
	private GUIManager guiManager;

	public StationaryBubble(TopicIdentifier ti, Manager manager, GUIManager guiManager) {
		super(ti.getTopicTitle(), manager);
		this.ti = ti;
		this.image = new Image(ImageHolder.getImgLoc("hierarchy/") + "ball_red.png");
		this.guiManager = guiManager;
		unscaledWidth = 50;
		unscaledHeight = 50;

		init();

	}

	// @Override
	protected void onAttach() {
		super.onAttach();
		// need this to setup dimensions
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				zoomToScale(1.0);
			}
		});

	}

	// @Override
	protected void clickAction() {
		guiManager.hideCurrentHover();
		getManager().bringUpChart(ti);
	}

	// @Override
	protected void unClickAction() {
		guiManager.hideHoverIn1(ti);
		// HoverManager.hideHoverIn1(ti);
	}

	// @Override
	protected void saveLocation() {
	}


	public void processDrag(double currentScale) {
	}

	public void setLeft(int longitude) {
	}

	public void setTop(int latitude) {
	}

	public int getLeft() {
		return 0;
	}

	public int getTop() {
		return 0;
	}

	// @Override
	protected Widget getOurWidget() {

		mainPanel = new AbsolutePanel();


		this.image.setPixelSize(unscaledWidth, unscaledHeight);

		// System.out.println("AbstractBubble left "+left+" top "+top);

		banner = new IslandBanner(getTitle(), 5);

		double currentScale = 1;
		Widget minimumWidget = banner.setToZoom(currentScale);



		mainPanel.add(image, 0, 0);
		mainPanel.add(banner, 0, 0);

		int width = (minimumWidget.getOffsetWidth() > (int) (unscaledWidth * currentScale)) ? minimumWidget
				.getOffsetWidth()
				: (int) (unscaledWidth * currentScale);

		int height = ((int) (unscaledHeight * currentScale) < minimumWidget.getOffsetHeight()) ? minimumWidget
				.getOffsetHeight()
				: (int) (unscaledHeight * currentScale);

		System.out.println("getOurWidget " + width + " " + height);
		mainPanel.setPixelSize(width, height);

		return mainPanel;
	}

	public Widget getDropTarget() {
		return null;
	}

	public void update(Topic t) {
		banner.setText(t.getTitle());
	}

	public void zoomToScale(double currentScale) {


		Widget minimumWidget = banner.setToZoom(currentScale);
		int width = (minimumWidget.getOffsetWidth() > (int) (unscaledWidth * currentScale)) ? minimumWidget
				.getOffsetWidth()
				: (int) (unscaledWidth * currentScale);

		int height = ((int) (unscaledHeight * currentScale) < minimumWidget.getOffsetHeight()) ? minimumWidget
				.getOffsetHeight()
				: (int) (unscaledHeight * currentScale);

		System.out.println("zoom to scale " + width + " " + height);
		mainPanel.setPixelSize(width, height);

	}



}
