package com.aavu.client.gui.explorer;

import java.util.Set;

import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.google.gwt.user.client.ui.Widget;

public interface ExplorerPanel {

	Widget getWidget();

	//Set<TopicIdentifier>
	//all == null
	void loadAll();
	void load(Set tags);

}
