package com.aavu.client.gui.dhtmlIslands;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.util.Logger;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

public class DragHandler implements MouseListener {
	private static final String DRAGGING_STYLE = "H-Dragging";
	private Widget dragging;
	private int dragStartX;
	private int dragStartY;
	private AbsolutePanel absolutePanel;
	//private Map dragBuddy = new HashMap();
	private DragFinishedListener dragFinishedListener;

	public DragHandler(AbsolutePanel panel) {
		this.absolutePanel = panel;
		//RootPanel.get()
	}

	public void add(SourcesMouseEvents w,DragFinishedListener listener) {
		w.addMouseListener(this);
		this.dragFinishedListener = listener;
	}
	
//	public void add(SourcesMouseEvents w,Widget draggable, Widget goWith) {
//		w.addMouseListener(this);
//		dragBuddy.put(draggable,goWith);
//	}
	
	public void remove(SourcesMouseEvents w) {
		w.removeMouseListener(this);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		Logger.debug("got a down start drag "+sender);
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
			dragging.addStyleName(DRAGGING_STYLE);
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		Logger.debug("got an up, cancel drag "+sender);
		if(dragging != null){
			DOM.releaseCapture(dragging.getElement());
			
			dragging.removeStyleName(DRAGGING_STYLE);
			
			if(dragFinishedListener != null)
			dragFinishedListener.dragFinished(dragging);
			
//			Widget buddy = (Widget) dragBuddy.get(sender);
//			if(null != buddy){
//				DOM.releaseCapture(buddy.getElement());
//			}
		}
		dragging = null;
	} 
}
