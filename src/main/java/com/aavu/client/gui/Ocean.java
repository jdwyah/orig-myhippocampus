package com.aavu.client.gui;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.google.gwt.user.client.ui.Widget;

public interface Ocean {

	/**
	 * Will init after async "getTagStats()" call
	 *
	 */
	public void load(LoadFinishedListener loadFinished);

	public void growIsland(Tag tag);
	
	public Widget getWidget();


	public void removeIsland(long id);


	public void zoomTo(double scale);
	public void zoomIn();
	public void zoomOut();

	public void update(Tag t, AbstractCommand command);

	/**
	 * 
	 * @param topic
	 * @return - should return true if it finds a good thing to center on
	 */
	public boolean centerOn(Topic topic);

	public void moveBy(int i, int j);

	public double ensureZoomOfAtLeast(double scale);

}