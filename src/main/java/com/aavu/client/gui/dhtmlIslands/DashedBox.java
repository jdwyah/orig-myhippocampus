package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class DashedBox extends Widget implements RemembersPosition {

	private int top;
	private int left;

	public DashedBox(int left, int top,int width, int heightPCT) {
		this.left = left;
		this.top = top;
		
		setElement(DOM.createDiv());		
		DOM.setStyleAttribute(getElement(), "width", width+"%");
		DOM.setStyleAttribute(getElement(), "height", heightPCT+"%");
		
		setStyleName("H-Tropics");
	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public Widget getWidget() {
		return this;
	}

}
