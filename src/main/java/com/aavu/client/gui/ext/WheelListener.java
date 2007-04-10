package com.aavu.client.gui.ext;

import com.google.gwt.user.client.ui.Widget;

public interface WheelListener {

	/**
	 * return true if you consume the event.
	 * 
	 * @param widget
	 * @param delta
	 * @return
	 */
	boolean onWheel(Widget widget, int delta);
	
}
