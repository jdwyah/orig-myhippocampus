package com.aavu.client.gui.hierarchy;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ocean.dhtmlIslands.RemembersPosition;
import com.allen_sauer.gwt.dragdrop.client.drop.DropController;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public interface TopicDisplayObj extends RemembersPosition {

	DropController getDropController();

	void processDrag(double currentScale);

	void receivedDrop(TopicDisplayObj bubble);

	void zoomToScale(double currentScale);

	Widget getDropTarget();

	FocusPanel getFocusPanel();

	void setTop(int latitude);

	String getTitle();

	TopicIdentifier getIdentifier();

	void update(Topic t);

	void setLeft(int longitude);

}
