package com.aavu.client.gui.hierarchy;

import com.aavu.client.gui.HoverLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContextMenu extends PopupPanel {
	public ContextMenu() {
		super(true);

		VerticalPanel mainPanel = new VerticalPanel();

		mainPanel.add(new HoverLabel("New Topic"));
		mainPanel.add(new HoverLabel("New Connection"));

		add(mainPanel);

		setStyleName("H-ContextMenu");
	}
}
