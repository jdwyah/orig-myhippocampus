package com.aavu.client.gui.ocean.dhtmlIslands;

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
	private DragEventListener dragFinishedListener;
	
	private int panelOffsetX;
	private int panelOffsetY;
	private boolean islandDrag = true;
	private boolean doYTranslate = true;
	private boolean actuallyDragged;


	/**
	 * 
	 * 
	 * @param panel
	 */
	public DragHandler(AbsolutePanel panel) {
		this.absolutePanel = panel;
		
	}

	public void add(SourcesMouseEvents w,DragEventListener listener) {
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
		
		if(!islandDrag){
			Logger.debug("!islandDrag, cancel down");
			return;
		}
		
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
		
		//System.out.println("dragStartX "+dragStartX+" dragStartY "+dragStartY);
		
		actuallyDragged = false;
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
		
		//Logger.debug("mouseMove "+GWT.getTypeName(sender)+" x "+x+" y "+y);
		
//		if(GWT.getTypeName(sender).equals("com.aavu.client.gui.dhtmlIslands.DraggableLabel")){
//			System.out.println("dragStartX "+dragStartX+" dragStartY "+dragStartY+" absX "+absX+" absY "+absY);
//		}		
//		if(GWT.getTypeName(sender).equals("com.aavu.client.gui.dhtmlIslands.DraggableLabel")){
//			System.out.println("Label dragging = "+(dragging != null));
//		}
		//System.out.println("DragHandle.onMouseMove "+(dragging!= null));
		if (dragging!=null) {
			
			int absX = x + dragging.getAbsoluteLeft();
			int absY = y + dragging.getAbsoluteTop();
			
			
			//System.out.println("drag.absolLeft() "+dragging.getAbsoluteLeft()+" dag.abslTop() "+dragging.getAbsoluteTop());
			
//			System.out.println("mouse x "+mouseX+" mouse y "+mouseY);
			
			//Logger.debug("mouseMove "+GWT.getTypeName(sender));
//			if(GWT.getTypeName(sender).equals("com.aavu.client.gui.dhtmlIslands.DraggableLabel")){
//				System.out.println("dragStartX "+dragStartX+" dragStartY "+dragStartY+" absX "+absX+" absY "+absY+" ");
//			}
			
			//System.out.println("draggin  "+GWT.getTypeName(dragging));			
			
			int newX = absX - dragStartX - panelOffsetX;
			int newY = absY - dragStartY - panelOffsetY;
			if(!doYTranslate){
				newY = dragStartY;
			}
			
			absolutePanel.setWidgetPosition(dragging,
					newX,
					newY);
						
			
			dragging.addStyleName(DRAGGING_STYLE);
			
			dragFinishedListener.dragged(dragging,newX,newY);
			

			actuallyDragged = true;
		}
	}

	public void onMouseUp(Widget sender, int x, int y) {
		Logger.debug("got an up, cancel drag "+GWT.getTypeName(sender));
		if(dragging != null){
			DOM.releaseCapture(dragging.getElement());
			
			dragging.removeStyleName(DRAGGING_STYLE);
			
			if(dragFinishedListener != null){
				dragFinishedListener.dragFinished(dragging);
			}
			
//			Widget buddy = (Widget) dragBuddy.get(sender);
//			if(null != buddy){
//				DOM.releaseCapture(buddy.getElement());
//			}
		}
		dragging = null;
	}

	public void setIslandDrag(boolean islandDrag) {
		this.islandDrag = islandDrag;
	}

	public void setDoYTranslate(boolean doYTranslate) {
		this.doYTranslate = doYTranslate;
	}
}
