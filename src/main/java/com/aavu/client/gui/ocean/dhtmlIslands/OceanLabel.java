package com.aavu.client.gui.ocean.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OceanLabel extends Label implements RemembersPosition {

	private static final double SIZE_AT_ZOOM_1 = 80;
	private int top;
	private int left;

	public OceanLabel(String html, int x, int y) {
		super(html, false);
		setStyleName("H-OceanLabel");
		DOM.setStyleAttribute(getElement(), "fontSize", SIZE_AT_ZOOM_1 + "px");

		this.left = x;
		this.top = y;

		sinkEvents(Event.ONDBLCLICK);

	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public Widget getWidget() {
		return this;
	}

	public void zoomToScale(double currentScale) {
		int size = (int) (SIZE_AT_ZOOM_1 * currentScale);
		DOM.setStyleAttribute(getElement(), "fontSize", size + "px");
	}

	// @Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if (DOM.eventGetType(event) == Event.ONDBLCLICK) {
			System.out.println("edit me");
		}
	}

}
