package com.aavu.client.gui.dhtmlIslands;

import com.aavu.client.gui.ext.SourcesWheelEvents;
import com.aavu.client.gui.ext.WheelListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

public class EventBackdrop extends FocusPanel implements SourcesWheelEvents {

	private WheelListener wheelListener;
	
	public EventBackdrop(){
		setWidth("100%");
		setHeight("100%");
		
		setStyleName("H-FocusBackDrop");		
		
		addWheelSupport();
	}
	
	public void scrollDelta(int delta){
		if(wheelListener != null){
			wheelListener.onWheel(this, delta);
		}
	}

	/**
	 * WARN can only add one listener.
	 */
	public void addWheelistener(WheelListener listener) {
		wheelListener = listener;
	}
	
	
	private native boolean addWheelSupport() /*-{
	
	var callBackTarget = this;
	
	function handle(delta){
		//alert("delta "+delta);		
		callBackTarget.@com.aavu.client.gui.dhtmlIslands.EventBackdrop::scrollDelta(I)(delta);
	}
	
	function wheel(event){
	        var delta = 0;
	        if (!event) // For IE. 
	                event = $wnd.event;
	        if (event.wheelDelta) { // IE/Opera. 
	                delta = event.wheelDelta/120;
	                // In Opera 9, delta differs in sign as compared to IE.
	                //
	                if ($wnd.opera)
	                        delta = -delta;
	        } else if (event.detail) { // Mozilla case. 
	                 // In Mozilla, sign of delta is different than in IE.
	                 // Also, delta is multiple of 3.
	                 //
	                delta = -event.detail/3;
	        }
	        // If delta is nonzero, handle it.
	        // Basically, delta is now positive if wheel was scrolled up,
	        // and negative, if wheel was scrolled down.
	        //
	        if (delta)
	                handle(delta);
	        // Prevent default actions caused by mouse wheel.
	        // That might be ugly, but we handle scrolls somehow
	        // anyway, so don't bother here..
	        //
	        if (event.preventDefault)
	                event.preventDefault();
		event.returnValue = false;
	}
	
	try{		
    
    	if ($wnd.addEventListener){
        	// DOMMouseScroll is for mozilla. 
        	$wnd.addEventListener('DOMMouseScroll', wheel, false);
    	}
		// IE/Opera. 
		$wnd.onmousewheel = $doc.onmousewheel = wheel;	    	
    	
    	return true;
    	
    }catch(e){
    	alert("error occured while adjust whether frame is loaded.\n" + e.message);
    	return false;
    }
    
}-*/;

	
}
