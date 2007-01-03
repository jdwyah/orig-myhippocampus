package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

public class OceanLabel extends HTML implements RemembersPosition {

	private int top;
	private int left;

	public OceanLabel(String html,int x, int y) {
		super(html);
		setStyleName("H-OceanLabel");
		this.left = x;
		this.top = y;
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
