package com.aavu.client.gui.ext;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class UpdateableTabPanel extends TabPanel {


	public void updateTitle(Widget widget, String newTitle) {

		int idx = getWidgetIndex(widget);
		getTabBar().removeTab(idx);
		getTabBar().insertTab(newTitle, idx);
		
	}
	
}
