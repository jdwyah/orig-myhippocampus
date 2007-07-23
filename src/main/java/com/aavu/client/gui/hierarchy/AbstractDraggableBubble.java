package com.aavu.client.gui.hierarchy;

import com.aavu.client.util.Logger;

public abstract class AbstractDraggableBubble extends AbstractBubble {

	private HierarchyDisplay display;
	private int top;
	private int left;

	public AbstractDraggableBubble(int longitude, int latitude, String title,
			HierarchyDisplay display) {
		super(title, display.getManager());

		this.left = longitude;
		this.top = latitude;
		this.display = display;
	}


	public HierarchyDisplay getDisplay() {
		return display;
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public void processDrag(double currentScale) {

		int[] lngLat = display.getLongLatForXY(getAbsoluteLeft(), getAbsoluteTop());

		if (lngLat[0] != left || lngLat[1] != top) {
			System.out.println("\nMove DETECTED!!!!!!!!!!!! " + " Scale " + currentScale
					+ " newLeft " + lngLat[0] + " newTop " + lngLat[1]);
			left = lngLat[0];
			top = lngLat[1];
			saveLocation();
		} else {
			Logger.debug("AbstractBubble no need to save drag");
		}

	}

}
