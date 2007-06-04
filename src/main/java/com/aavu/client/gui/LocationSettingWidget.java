package com.aavu.client.gui;

import com.google.gwt.user.client.ui.Widget;

public interface LocationSettingWidget {

	int getAbsoluteLeft();

	int getAbsoluteTop();

	void add(Widget blockerWidget, int left, int top);

	void add(Widget internalFrame);

	boolean remove(Widget internalFrame);

	void setWidgetPosition(Widget widget, int left, int top);

	Widget getWidget();

}
