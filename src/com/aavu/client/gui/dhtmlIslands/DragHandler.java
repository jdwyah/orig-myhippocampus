package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.util.Logger;
import com.google.gwt.core.client.GWT;
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
	
	private int panelOffsetX;
	private int panelOffsetY;


	/**
	 * 
	 * 
	 * @param panel
	 */
	public DragHandler(AbsolutePanel panel) {
		this.absolutePanel = panel;
		
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
		
		
		Logger.debug("got a down start drag "+GWT.getTypeName(sender));
		
		dragging=sender;
		DOM.setCapture(sender.getElement());
		
//		Widget buddy = (Widget) dragBuddy.get(sender);
//		if(null != buddy){
//			DOM.setCapture(buddy.getElement());
//		}		
		
		dragStartX = x;
		dragStartY = y;		
		
		/*
		 * The original drag handler assumed that we were draggin ontop of something
		 * located at (0,0)  With dragging labels ontop of islands, that's not the case.
		 */
		panelOffsetX = absolutePanel.getAbsoluteLeft();
		panelOffsetY = absolutePanel.getAbsoluteTop();
		
		System.out.println("dragStartX "+dragStartX+" dragStartY "+dragStartY);
		
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
		
		Logger.debug("mouseMove "+GWT.getTypeName(sender)+" x "+x+" y "+y);
//		if(GWT.getTypeName(sender).equals("com.aavu.client.gui.dhtmlIslands.DraggableLabel")){
//			System.out.println("dragStartX "+dragStartX+" dragStartY "+dragStartY+" absX "+absX+" absY "+absY);
//		}		
//		if(GWT.getTypeName(sender).equals("com.aavu.client.gui.dhtmlIslands.DraggableLabel")){
//			System.out.println("Label dragging = "+(dragging != null));
//		}
		
		if (dragging!=null) {
			
			int absX = x + dragging.getAbsoluteLeft();
			int absY = y + dragging.getAbsoluteTop();
			
			
//			System.out.println("drag.absolLeft() "+dragging.getAbsoluteLeft()+" dag.abslTop() "+dragging.getAbsoluteTop());
			
//			System.out.println("mouse x "+mouseX+" mouse y "+mouseY);
			
			//Logger.debug("mouseMove "+GWT.getTypeName(sender));
//			if(GWT.getTypeName(sender).equals("com.aavu.client.gui.dhtmlIslands.DraggableLabel")){
//				System.out.println("dragStartX "+dragStartX+" dragStartY "+dragStartY+" absX "+absX+" absY "+absY+" ");
//			}
			
			
			absolutePanel.setWidgetPosition(dragging,
					absX - dragStartX - panelOffsetX,
					absY - dragStartY - panelOffsetY);
						
			
			dragging.addStyleName(DRAGGING_STYLE);
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		Logger.debug("got an up, cancel drag "+GWT.getTypeName(sender));
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
