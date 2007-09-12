package com.aavu.client.gui.ocean.dhtmlIslands;

import com.aavu.client.domain.dto.TimeLineObj;

public interface TimelineRemembersPosition extends RemembersPosition {
	int getWidth();

	TimeLineObj getTLO();

	void updateTitle();

	void addStyleName(String string);

	void removeStyleName(String string);
}
