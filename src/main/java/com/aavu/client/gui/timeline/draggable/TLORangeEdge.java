package com.aavu.client.gui.timeline.draggable;

import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class TLORangeEdge extends Composite implements MouseListener {

	private boolean resizing;
	private Image myLabel;
	private int resizeStartX;

	private TLORangeWidget tloRangeW;
	private boolean leftSide;

	public TLORangeEdge(TLORangeWidget tloRangeW, boolean leftSide) {
		this.tloRangeW = tloRangeW;
		this.leftSide = leftSide;

		// myImage = ConstHolder.images.bullet_blue().createImage();

		if (leftSide) {
			myLabel = ConstHolder.images.resultset_previous().createImage();
		} else {
			myLabel = ConstHolder.images.resultset_next().createImage();
		}

		myLabel.addMouseListener(this);
		myLabel.setStyleName("H-TimeBar-Edge");

		initWidget(myLabel);

	}

	public void onMouseDown(Widget sender, int x, int y) {
		resizing = true;
		DOM.setCapture(myLabel.getElement());
		resizeStartX = x;

	}


	public void onMouseUp(Widget sender, int x, int y) {
		resizing = false;
		DOM.releaseCapture(myLabel.getElement());
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (resizing) {

			int clientX = DOM.eventGetClientX(DOM.eventGetCurrentEvent());
			System.out.println("resiszeStart " + resizeStartX + " " + x + " clientX " + clientX);

			tloRangeW.expand(clientX, leftSide);
		}
	}
}
