package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.FocusPanelExt;
import com.aavu.client.gui.ocean.dhtmlIslands.IslandBanner;
import com.aavu.client.service.Manager;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractBubble extends FocusPanelExt implements TopicDisplayObj,
		MouseListener, ClickListener {

	private IslandBanner banner;
	private int currentHeight;
	private int currentWidth;
	private Manager manager;

	private DropController dropController;
	private Image image;


	private AbsolutePanel mainPanel;

	private String title;

	private int unscaledHeight;
	private int unscaledWidth;

	public AbstractBubble(String title, Image image, Manager manager) {

		this.manager = manager;
		this.title = title;

		unscaledWidth = 50;
		unscaledHeight = 50;

		mainPanel = new AbsolutePanel();
		mainPanel.setPixelSize(unscaledWidth, unscaledHeight);

		this.image = image;
		this.image.setPixelSize(unscaledWidth, unscaledHeight);

		// System.out.println("AbstractBubble left "+left+" top "+top);

		banner = new IslandBanner(title, 5);

		mainPanel.add(image, 0, 0);
		mainPanel.add(banner, 0, 0);

		setWidget(mainPanel);

		addMouseListener(this);

		addClickListener(this);
	}

	public int getCurrentHeight() {
		return currentHeight;
	}

	public int getCurrentWidth() {
		return currentWidth;
	}


	public DropController getDropController() {
		return dropController;
	}

	public Widget getDropTarget() {
		return image;
	}

	public FocusPanel getFocusPanel() {
		return this;
	}

	public TopicIdentifier getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}



	public String getTitle() {
		return title;
	}

	public Widget getWidget() {
		return this;
	}

	protected abstract void hover();

	public void onMouseDown(Widget sender, int x, int y) {
	}

	public void onMouseEnter(Widget sender) {
		// hover();
	}

	public void onMouseLeave(Widget sender) {
		unhover();
	}

	public void onMouseMove(Widget sender, int x, int y) {
	}

	public void onMouseUp(Widget sender, int x, int y) {
	}



	public void receivedDrop(TopicDisplayObj bubble) {
		// TODO Auto-generated method stub

	}

	protected abstract void saveLocation();

	public void setDropController(DropController dropController) {
		this.dropController = dropController;
	}


	public Manager getManager() {
		return manager;
	}

	protected abstract void unhover();


	public void update(Topic t) {
		banner.setText(t.getTitle());
	}

	public IslandBanner getBanner() {
		return banner;
	}

	public void zoomToScale(double currentScale) {

		image.setPixelSize((int) (unscaledWidth * currentScale),
				(int) (unscaledHeight * currentScale));

		Widget minimumWidget = banner.setToZoom(currentScale);

		currentWidth = (minimumWidget.getOffsetWidth() > (int) (unscaledWidth * currentScale)) ? minimumWidget
				.getOffsetWidth()
				: (int) (unscaledWidth * currentScale);

		currentHeight = ((int) (unscaledHeight * currentScale) < minimumWidget.getOffsetHeight()) ? minimumWidget
				.getOffsetHeight()
				: (int) (unscaledHeight * currentScale);

		// System.out.println("w "+correctedWidth+" h "+correctedHeight);



		mainPanel.setPixelSize(currentWidth, currentHeight);

	}


	public void onClick(Widget sender) {
		hover();
	}

}
