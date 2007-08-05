package com.aavu.client.gui.hierarchy;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class BorderThemedPanelResizeImage extends FlowPanel implements MouseListener {

	private int resizeStartX;

	private int resizeStartY;

	private boolean resizing;

	private BorderThemedPanel parent;

	private String currentStyle;

	private Label label;


	public BorderThemedPanelResizeImage(BorderThemedPanel parent) {
		super();
		this.parent = parent;
		buildGui();
		sinkEvents(Event.MOUSEEVENTS);
	}

	private void buildGui() {
		this.currentStyle = parent.getTheme();
		label = new Label("");
		setTheme(currentStyle);
		label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		label.addMouseListener(this);
		add(label);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		resizing = true;
		DOM.setCapture(label.getElement());
		resizeStartX = x;
		resizeStartY = y;

		System.out.println("down");

		// parent.startResizing(); // call this and allow the frame to remove
		// iframes
	}

	public void onBrowserEvent(Event e) {
		super.onBrowserEvent(e);
	}

	public void onMouseUp(Widget sender, int x, int y) {
		resizing = false;
		DOM.releaseCapture(label.getElement());
		// parent.stopResizing(); // call this and allow the frame to remove
		// iframes
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {

		if (resizing) {

			int newHeight = parent.getHeight() + y - resizeStartY;
			int newWidth = parent.getWidth() + x - resizeStartX;
			if (newWidth > parent.getMinimumWidth()) {
				parent.setWidth(newWidth);
			}
			if (newHeight > parent.getMinimumHeight()) {
				parent.setHeight(newHeight);
			}
		}
	}

	public boolean onEventPreview(Event evt) {
		return true;
	}

	public void setTheme(String currentTheme) {
		label.setStyleName("gwm-" + currentTheme + "-Frame-ResizeButton");
	}

}
