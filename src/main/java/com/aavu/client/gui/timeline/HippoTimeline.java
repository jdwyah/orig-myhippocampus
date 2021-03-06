package com.aavu.client.gui.timeline;

import java.util.List;

import com.aavu.client.domain.dto.TimeLineObj;
import com.google.gwt.user.client.ui.Widget;

public interface HippoTimeline {

	Widget getWidget();

	void resize(int newWidth, int newHeight);

	void add(List<TimeLineObj> result);

	void clear();


}
