package com.aavu.client.gui.hierarchy;

import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class DisplayPanel extends PopupPanel implements SourcesMouseEvents {

	private VerticalPanel mainPanel;
	private BorderThemedPanel borderPanel;
	private FocusPanel focusPanel;

	public DisplayPanel(String string) {
		super();
		mainPanel = new VerticalPanel();
		borderPanel = new BorderThemedPanel();
		borderPanel.setCaption(string);

		borderPanel.setContent(mainPanel);

		focusPanel = new FocusPanel();
		focusPanel.add(borderPanel);

		setWidget(focusPanel);

		setStyleName("H-DisplayPanel");
	}

	public void add(Widget widget) {
		mainPanel.add(widget);
	}

	public boolean isEmpty() {
		return mainPanel.getWidgetCount() < 1;
	}

	public void addMouseListener(MouseListener listener) {
		focusPanel.addMouseListener(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		focusPanel.removeMouseListener(listener);
	}
}
