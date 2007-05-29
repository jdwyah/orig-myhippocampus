package com.aavu.client.gui.timeline;

import java.util.List;

import org.gwm.client.event.GFrameEvent;

import com.google.gwt.user.client.ui.Widget;

public interface HippoTimeline {

	Widget getWidget();

	void resize(GFrameEvent evt);

	void load(List result);

}
