package com.aavu.client.gui.timeline;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;

public interface HippoTimeline {

	Widget getWidget();

	void resize(int newWidth, int newHeight);

	void add(List result);

	void clear();


}
