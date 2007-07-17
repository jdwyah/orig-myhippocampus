package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
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

	public StationaryBubble(TopicIdentifier ti, Manager manager) {
		super(ti.getTopicTitle(), manager);
		this.ti = ti;
		this.image = new Image(ImageHolder.getImgLoc("hierarchy/") + "ball_red.png");

		unscaledWidth = 50;
		unscaledHeight = 50;

		init();

		// System.out.println("Banner " + getBanner());

	}

	// @Override
	protected void onAttach() {
		super.onAttach();
		// need this to setup dimensions
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				banner.setToZoom(1.0);
				System.out.println("^^^^^^^^^^");
			}
		});

	}

	// @Override
	protected void hover() {
		HoverManager.showHover(getManager(), this, ti);
	}

	// @Override
	protected void unhover() {
		HoverManager.hideHover(ti);
	}

	// @Override
	protected void saveLocation() {
		// TODO Auto-generated method stub

	}


	public void processDrag(double currentScale) {
		// TODO Auto-generated method stub

	}

	public void setLeft(int longitude) {
		// TODO Auto-generated method stub

	}

	public void setTop(int latitude) {
		// TODO Auto-generated method stub

	}

	public int getLeft() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTop() {
		// TODO Auto-generated method stub
		return 0;
	}

	// @Override
	protected Widget getOurWidget() {

		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(unscaledWidth, unscaledHeight);


		this.image.setPixelSize(unscaledWidth, unscaledHeight);

		// System.out.println("AbstractBubble left "+left+" top "+top);

		banner = new IslandBanner(getTitle(), 5);

		mainPanel.add(image, 0, 0);
		mainPanel.add(banner, 0, 0);

		return mainPanel;
	}

	public Widget getDropTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(Topic t) {
		// TODO Auto-generated method stub

	}

	public void zoomToScale(double currentScale) {
		// TODO Auto-generated method stub

	}

}
