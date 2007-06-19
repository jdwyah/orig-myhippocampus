package com.aavu.client.gui.ext;

import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;


public class DraggableLabel extends Label  implements ClickListener, SourcesMouseEvents, RemembersPosition {
	
	private int left;
	private int top;
	
	private double xPct;
	private double yPct;

	public DraggableLabel(String text, double xPct, double yPct){
		super(text);

		addClickListener(this);

		this.xPct = xPct;
		this.yPct = yPct;
		
		//sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS );	    

		//	sinkEvents(Event.MOUSEEVENTS );
	}


	/**
	 * cancel the event, or you can't have a draggable widget in a draggable widget. 
	 * (ie label on island)
	 */
	public void onBrowserEvent(Event event) {
		DOM.eventCancelBubble(event, true);
		super.onBrowserEvent(event);				
	}
//	boolean wasMouseUp = false;

//	switch (DOM.eventGetType(event)) {
////	case Event.ONCLICK: {
////	if (clickListeners != null)
////	clickListeners.fireClick(this);
////	break;
////	}
//	case Event.ONMOUSEUP:
//	wasMouseUp = true;
//	case Event.ONMOUSEDOWN:
//	case Event.ONMOUSEMOVE:
//	case Event.ONMOUSEOVER:
//	case Event.ONMOUSEOUT: {
//	if (mouseListeners != null)
//	mouseListeners.fireMouseEvent(this, event);
//	break;
//	}

//	}
//	/*
//	* detecting move requires subtracting out the possible shift of the ocean 
//	*/
//	if(wasMouseUp){
//	int absLeft = getAbsoluteLeft();
//	int absTop = getAbsoluteTop();
//	int oceanLeft = 0;//ocean.getBackX();
//	int oceanTop = 0;//ocean.getBackY();
//	if(absLeft != left + oceanLeft
//	||
//	absTop != top + oceanTop){
//	int newLeft = absLeft - oceanLeft;
//	int newTop = absTop - oceanTop;

//	//System.out.println("\n\n\n\nMove DETECTED!!!!!!!!!!!!");
//	//ocean.islandMoved(tagStat.getTagId(), newLeft, newTop);



//	left = newLeft;
//	top = newTop;
//	}else{
//	/*
//	* hmm...  
//	* detecting here is good bc then we know if it was a drag, 
//	* but the detection area is the whole island div.
//	* Alternative #2 is listener on acres & banner, but then we need a 
//	* new way to cancel if dragged. 
//	*/
//	onClick(this);
//	}
//	//System.out.println("Moved "+getAbsoluteLeft()+" "+left+" "+getAbsoluteTop()+" "+top);
//	//System.out.println("Ocean "+ocean.getAbsoluteLeft()+" top "+ocean.getAbsoluteTop()+" ");
//	}
//	}

	public void onClick(Widget sender) {

		System.out.println("clicked");

	}

	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public double getXPct() {
		return xPct;
	}
	public void setXPct(double pct) {
		xPct = pct;
	}
	public double getYPct() {
		return yPct;
	}

	public void setYPct(double pct) {
		yPct = pct;
	}

	public Widget getWidget() {
		return this;
	}

}

