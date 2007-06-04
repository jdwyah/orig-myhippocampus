package com.aavu.client.gui.timeline;

import java.util.List;

import org.gwm.client.event.GFrameEvent;

import com.google.gwt.user.client.ui.Widget;

public interface HippoTimeline {

	Widget getWidget();

	void resize(int newWidth, int newHeight);

	void load(List result);

}
