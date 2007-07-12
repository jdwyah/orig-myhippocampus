package com.aavu.client.gui.ext;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * FocusPanel gives us no good way to see if the ctrl key was down, or do double clicks.
 * 
 * 
 * Stores the ctrl key before the event goes by. Technically you're suppsosed to override or redo,
 * FocusPanel, but let's just be lazy.
 * 
 * 
 * @author Jeff Dwyer
 * 
 */
public class FocusPanelExt extends FocusPanel {

	private Event lastEvent;

	private DblClickListenerCollection doubleClickLisenerCollection;

	public FocusPanelExt() {
		super();
		sinkEvents(Event.ONDBLCLICK);
	}

	// @Override
	public void onBrowserEvent(Event event) {
		lastEvent = event;
		super.onBrowserEvent(event);


		switch (DOM.eventGetType(event)) {
		case Event.ONDBLCLICK:
			if (doubleClickLisenerCollection != null) {
				doubleClickLisenerCollection.fireDoubleClick(this);
			}
		}

		lastEvent = null;
	}

	public Event getLastEvent() {
		return lastEvent;
	}

	public void addDblClickListener(DblClickListener listener) {
		if (doubleClickLisenerCollection == null) {
			doubleClickLisenerCollection = new DblClickListenerCollection();
		}
		doubleClickLisenerCollection.add(listener);
	}

}
