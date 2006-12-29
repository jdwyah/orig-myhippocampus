package com.aavu.client.gui.dhtmlIslands;

import java.util.HashMap;
import java.util.Map;

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
	//private Map dragBuddy = new HashMap();

	public DragHandler(AbsolutePanel panel) {
		this.absolutePanel = panel;
		//RootPanel.get()
	}

	public void add(SourcesMouseEvents w) {
		w.addMouseListener(this);
	}
	
//	public void add(SourcesMouseEvents w,Widget draggable, Widget goWith) {
//		w.addMouseListener(this);
//		dragBuddy.put(draggable,goWith);
//	}
	
	public void remove(SourcesMouseEvents w) {
		w.removeMouseListener(this);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		System.out.println("got a down start drag "+sender);
		dragging=sender;
		DOM.setCapture(sender.getElement());
		
//		Widget buddy = (Widget) dragBuddy.get(sender);
//		if(null != buddy){
//			DOM.setCapture(buddy.getElement());
//		}
		
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
		System.out.println("got an up, cancel drag "+sender);
		if(dragging != null){
			DOM.releaseCapture(dragging.getElement());
			
//			Widget buddy = (Widget) dragBuddy.get(sender);
//			if(null != buddy){
//				DOM.releaseCapture(buddy.getElement());
//			}
		}
		dragging = null;
	} 
}
