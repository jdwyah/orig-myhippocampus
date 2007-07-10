package com.aavu.client.gui.ocean.dhtmlIslands;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;

/**
 * Stores the ctrl key before the event goes by. Technically you're suppsosed to override or redo,
 * FocusPanel, but let's just be lazy.
 * 
 * @author Jeff Dwyer
 * 
 */
public class EventBackdrop extends FocusPanel {

	private Event lastEvent;

	public EventBackdrop() {
		setWidth("100%");
		setHeight("100%");

		setStyleName("H-FocusBackDrop");
	}

	// @Override
	public void onBrowserEvent(Event event) {
		lastEvent = event;
		super.onBrowserEvent(event);
		lastEvent = null;
	}

	public Event getLastEvent() {
		return lastEvent;
	}

}
