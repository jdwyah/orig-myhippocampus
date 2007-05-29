package com.aavu.client.gui.timeline.draggable;

import java.util.Date;

import com.aavu.client.gui.dhtmlIslands.RemembersPosition;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TimelineBG extends Composite implements RemembersPosition {

	private int left;
	private int top;

	public TimelineBG(int depth, int key, Date date, int zoneStart,
			int width,int height, MouseListener listener) {
		
		left = zoneStart;
		top = 0;
		
//		Element myDiv = DOM.createDiv();
//		DOM.setElementProperty(myDiv, "width", width+"px");
//		DOM.setElementProperty(myDiv, "height", height+"px");
//		
//		DOM.setStyleAttribute(myDiv, "background-color", "red");
//		setElement(myDiv);
		
		
		FocusPanel p = new FocusPanel();
		
		p.setPixelSize(1, height);
		p.setStyleName("H-TimelineBG");
		initWidget(p);
				
		p.addMouseListener(listener);
		
		
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
