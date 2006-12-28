package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class DragHandler implements MouseListener {
	private Widget dragging;
	private int dragStartX;
	private int dragStartY;
	private AbsolutePanel absolutePanel;

	public DragHandler(AbsolutePanel panel) {
		this.absolutePanel = panel;
		//RootPanel.get()
	}

	public void add(SourcesMouseEvents w) {
		w.addMouseListener(this);
	}

	public void remove(SourcesMouseEvents w) {
		w.removeMouseListener(this);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		dragging=sender;
		DOM.setCapture(sender.getElement());
		dragStartX = x;
		dragStartY = y;
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (dragging!=null) {
			int absX = x + dragging.getAbsoluteLeft();
			int absY = y + dragging.getAbsoluteTop();
			absolutePanel.setWidgetPosition(dragging,
					absX - dragStartX,
					absY - dragStartY);
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		if(dragging != null){
			DOM.releaseCapture(dragging.getElement());
		}
		dragging = null;
	} 
}
