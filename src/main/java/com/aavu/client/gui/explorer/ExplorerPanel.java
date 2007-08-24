package com.aavu.client.gui.explorer;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.ui.Widget;

public interface ExplorerPanel {

	Widget getWidget();

	// Set<TopicIdentifier>
	// all == null
	void loadAll();

	void load(List tags);

	void load(Topic topic);

}
