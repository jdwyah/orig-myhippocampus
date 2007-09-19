package com.aavu.client.gui;

import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.explorer.TimeLineWrapper;

public interface GUIManager {

	void showHover(TopicIdentifier ti);

	void hideCurrentHover();

	void showTimeline();

	void showGlossary();

	void showGoogleMap();

	void showConnections();

	void navigateTo(TopicIdentifier ti);

	TimeLineWrapper getTimeline();

	void zoomIn();

	void zoomOut();

	void zoomTo(double convertToScale);



}
