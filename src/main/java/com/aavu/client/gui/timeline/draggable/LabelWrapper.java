package com.aavu.client.gui.timeline.draggable;

import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.aavu.client.gui.ext.JSUtil;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class LabelWrapper extends Label implements RemembersPosition {

	private int left;
	private int top;

	public LabelWrapper(String s,int left, int top){
		super(s);
		this.left = left;
		this.top = top;
		JSUtil.disableSelect(getElement());
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
