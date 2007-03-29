package com.aavu.client.gui.ext;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.ui.Widget;

public class WheelListenerCollectionCancellable extends Vector {

	/**
	 * Fires a click event to all listeners. Will cancel the action if onWheel returns true
	 * 
	 * @param sender the widget sending the event.
	 */
	public boolean fireWheel(Widget sender, int delta) {
		for (Iterator it = iterator(); it.hasNext();) {
			WheelListener listener = (WheelListener) it.next();
			if(listener.onWheel(sender, delta)){
				return true;
			}
		}
		return false;
	}
}
