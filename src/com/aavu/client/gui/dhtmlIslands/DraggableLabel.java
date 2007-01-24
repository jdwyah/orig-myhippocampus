package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;


public class DraggableLabel extends Label  implements ClickListener, SourcesMouseEvents, RemembersPosition {
	private MouseListenerCollection mouseListeners;
	private int left;
	private int top;

	public DraggableLabel(String text){
		super(text);

		addClickListener(this);

		//sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS );	    

		//	sinkEvents(Event.MOUSEEVENTS );
	}

//	public void onBrowserEvent(Event event) {
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

	public Widget getWidget() {
		return this;
	}

}

