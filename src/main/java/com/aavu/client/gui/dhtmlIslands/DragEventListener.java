package com.aavu.client.gui.dhtmlIslands;

import com.google.gwt.user.client.ui.Widget;

public interface DragEventListener {

	void dragFinished(Widget dragging);

	void dragged(Widget dragging, int newX, int newY);
	
}
