package com.aavu.client.gui.ocean.dhtmlIslands;

import com.aavu.client.util.Logger;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.Widget;


public class IslandBanner extends AbsolutePanel {

	private static final double HALF_MIN_EM = .4;
	private static final double SCALE_DIVISOR = 6;

	private static final double MAX_FONT = 3;


	public static final String BANNER_SELECTED = "Selected";
	public static final String BANNER_SHDW_SELECTED = "ShdwSelected";

	private Label reg;
	private Label shdw;
	private int size;

	private static final String HOVER_STYLE = "H-IslandBanner-Hover";


	// private MouseListenerCollection mouseListeners;



	/**
	 * 
	 * NOTE css value must be "fontSize" not "font-size" !! see
	 * http://groups.google.com/group/Google-Web-Toolkit/msg/5d2850a39637e56f
	 * 
	 * @param text
	 * @param size
	 */
	public IslandBanner(String text, int size) {

		super();
		this.size = size;

		double font_size = getFontFor(size);
		Logger.debug("IslandBanner.FONT " + getFontFor(size) + " " + size);
		shdw = new Label(text, false);
		DOM.setStyleAttribute(shdw.getElement(), "fontSize", font_size + "em");
		shdw.addStyleName("Shadow");
		add(shdw, 1, 1);

		reg = new Label(text, false);
		DOM.setStyleAttribute(reg.getElement(), "fontSize", font_size + "em");
		reg.addStyleName("Text");
		add(reg, 0, 0);


		setStyleName("H-IslandBanner");

		sinkEvents(Event.MOUSEEVENTS);

		reg.addMouseListener(new MouseListenerAdapter() {
			public void onMouseEnter(Widget sender) {
				reg.addStyleName(HOVER_STYLE);
			}

			public void onMouseLeave(Widget sender) {
				reg.removeStyleName(HOVER_STYLE);
			}
		});

		Logger.debug("IslandBanner.reg " + text + " " + reg.getOffsetWidth() + " "
				+ reg.getOffsetHeight());
		DOM.setStyleAttribute(getElement(), "position", "absolute");

	}


	// @Override
	protected void onLoad() {
		super.onLoad();
		setDimensions(1);
	}

	private Widget setDimensions(double currentScale) {

		double width = reg.getOffsetWidth();
		double height = reg.getOffsetHeight();

		// Without this, long names like "Ways to Get to Europe" never
		// resize as we zoom in. This solution is a bit hackish, but seems to work.
		// we'd really like to pass in the island width, but that calc is waiting on
		// us, so it's a bit odd.
		//
		// WARN!! the height *= was making us unable to roll over the topic labels
		// in firefox! not sure why it only affects FF
		if (currentScale > 1) {
			width *= currentScale;
			// height *= currentScale;
		}
		if (shdw.getText().equals("Person")) {
			System.out.println("on load reg " + width + " ");
		}
		DOM.setStyleAttribute(getElement(), "width", (int) width + "px");
		DOM.setStyleAttribute(getElement(), "height", (int) height + "px");

		Logger.debug("IslandBanner.SETDIM " + reg.getText() + " " + reg.getOffsetWidth() + " "
				+ reg.getOffsetHeight() + " " + width + " " + height);
		return reg;
	}


	public double getFontFor(int size) {
		return getFontFor(size, 1);
	}

	public double getFontFor(int size, double zoom) {

		if (size <= 0) {
			size = 1;
		}
		double s = (Math.log(size) / SCALE_DIVISOR + HALF_MIN_EM + (zoom * HALF_MIN_EM));

		s = s > MAX_FONT ? MAX_FONT : s;

		return s;
	}

	/*
	 * TODO not working (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.UIObject#setWidth(java.lang.String)
	 */
	// public void setWidth(String str) {
	// DOM.setStyleAttribute(getElement(), "width", str);
	// reg.setWidth(str);
	// shdw.setWidth(str);
	// }
	public void addClickListener(ClickListener listener) {
		reg.addClickListener(listener);
	}

	public void setText(String text) {
		reg.setText(text);
		shdw.setText(text);
	}

	public Widget setToZoom(double currentScale) {
		double font_size = getFontFor(size, currentScale);
		DOM.setStyleAttribute(reg.getElement(), "fontSize", font_size + "em");
		DOM.setStyleAttribute(shdw.getElement(), "fontSize", font_size + "em");
		return setDimensions(currentScale);
	}


	public void setSelected(boolean b) {
		if (b) {
			reg.addStyleName(BANNER_SELECTED);
			shdw.addStyleName(BANNER_SHDW_SELECTED);
		} else {
			reg.removeStyleName(BANNER_SELECTED);
			shdw.removeStyleName(BANNER_SHDW_SELECTED);
		}
	}


	// public void addMouseListener(MouseListener listener) {
	// if (mouseListeners == null)
	// mouseListeners = new MouseListenerCollection();
	// mouseListeners.add(listener);
	// }
	//
	// public void removeMouseListener(MouseListener listener) {
	// if (mouseListeners != null)
	// mouseListeners.remove(listener);
	// }
	// public void onBrowserEvent(Event event) {
	//
	// switch (DOM.eventGetType(event)) {
	// case Event.ONCLICK:
	// case Event.ONMOUSEUP:
	// case Event.ONMOUSEDOWN:
	// case Event.ONMOUSEMOVE:
	// case Event.ONMOUSEOVER:
	// case Event.ONMOUSEOUT: {
	// if (mouseListeners != null)
	// mouseListeners.fireMouseEvent(this, event);
	// break;
	// }
	// }
	// }
}
