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
		int absLeft = getAbsoluteLeft();
		int absTop = getAbsoluteTop();
		int oceanLeft = getDisplay().getBackX();
		int oceanTop = getDisplay().getBackY();
		if (absLeft != left + oceanLeft || absTop != top + oceanTop) {
			int newLeft = absLeft - oceanLeft;
			int newTop = absTop - oceanTop;
			System.out.println("\nMove DETECTED!!!!!!!!!!!! " + " Scale " + currentScale
					+ " newLeft " + newLeft + " newTop " + newTop + " " + oceanLeft + " "
					+ oceanTop);
			// ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);
			left = (int) (newLeft / currentScale);
			top = (int) (newTop / currentScale);

			// tagStat.setLongitude(left);
			// tagStat.setLatitude(top);

			saveLocation();

		} else {
			Logger.debug("AbstractBubble no need to save drag");
		}
	}

}
