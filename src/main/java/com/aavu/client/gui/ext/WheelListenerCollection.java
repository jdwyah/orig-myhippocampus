package com.aavu.client.gui.ext;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.Widget;

public class WheelListenerCollection extends Vector {

	/**
	 * Fires a click event to all listeners. NOT the cancellable collection,
	 * but will return true if ANY of its listeners return true.
	 * 
	 * @param sender the widget sending the event.
	 */
	public boolean fireWheel(Widget sender, int delta) {
		boolean result = false;
		for (Iterator it = iterator(); it.hasNext();) {
			WheelListener listener = (WheelListener) it.next();
			if(listener.onWheel(sender, delta)){
				result = true;
			}
		}
		return result;
	}
}
