package com.aavu.client.gui;

import com.aavu.client.domain.Tag;
import com.google.gwt.user.client.ui.Widget;

public interface Ocean {

	/**
	 * Will init after async "getTagStats()" call
	 *
	 */
	public void load();

	public void growIsland(Tag tag);
	
	public Widget getWidget();

	
	public int getLatitude();
	public int getLongitude();

	public void removeIsland(long id);

}