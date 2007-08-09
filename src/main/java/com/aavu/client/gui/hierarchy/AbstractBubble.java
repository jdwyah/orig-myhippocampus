package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ext.FocusPanelExt;
import com.aavu.client.service.Manager;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractBubble extends FocusPanelExt implements TopicDisplayObj,
		MouseListener, ClickListener {


	private Manager manager;

	private DropController dropController;


	private String title;


	public AbstractBubble(String title, Manager manager) {

		this.manager = manager;
		this.title = title;


		addMouseListener(this);

		addClickListener(this);
	}

	/**
	 * Call this in your ctor once you're getOurWidget() method is ready to go.
	 */
	protected void init() {
		setWidget(getOurWidget());
	}

	protected abstract Widget getOurWidget();



	public DropController getDropController() {
		return dropController;
	}



	public TopicIdentifier getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}



	public String getTitle() {
		return title;
	}

	public Widget getDragHandle() {
		return getWidget();
	}

	public Widget getWidget() {
		return this;
	}

	protected abstract void clickAction();

	public void onMouseDown(Widget sender, int x, int y) {
	}

	public void onMouseEnter(Widget sender) {
		// hover();
	}

	public void onMouseLeave(Widget sender) {
		// unhover();
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



	public void onClick(Widget sender) {
		clickAction();
	}

}
