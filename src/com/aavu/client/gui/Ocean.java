package com.aavu.client.gui;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.google.gwt.user.client.ui.Widget;

public interface Ocean {

	/**
	 * Will init after async "getTagStats()" call
	 *
	 */
	public void load();

	public void growIsland(Tag tag);
	
	public Widget getWidget();


	public void removeIsland(long id);

	public void unFocus();

	public void showCloseup(long id, FullTopicIdentifier[] topics);

}