package com.aavu.client.gui.hierarchy;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BorderThemedPanel extends Composite {
	private static final int DEFAULT_HEIGHT = 40;
	private static final int DEFAULT_WIDTH = 200;
	private FlexTable bottomRow;
	private Label centerLeftLabel;

	private Label centerRightLabel;
	protected FlexTable centerRow;
	private String currentTheme = "alphacube";

	private int height;
	private HTML imgBot;
	private Label imgTopLeft;
	private Label imgTopRight;
	private int minWidth, minHeight;
	private Widget myContent;

	private boolean resizable = true;

	private Widget captionWidget;

	private FlexTable topRow;

	private FlexTable ui;

	private int width;
	private BorderThemedPanelResizeImage resizeImage;

	private ArrayList<ResizeHandler> resizeListeners = null;
	private Widget dragHandle;

	public BorderThemedPanel() {

		ui = new FlexTable();
		topRow = new FlexTable();
		centerRow = new FlexTable();
		bottomRow = new FlexTable();

		// resizeImage = new ResizeImage(this);

		imgTopLeft = new Label();
		imgTopRight = new Label();
		imgBot = new HTML("&nbsp;");

		resizeImage = new BorderThemedPanelResizeImage(this);

		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;

		buildGui();

	}

	protected void applyTheme() {

		// topBar.setTheme(currentTheme);

		captionWidget.setStyleName(getItemTheme("FrameBorder-t"));

		// resizeImage.setTheme(currentTheme);

		imgTopLeft.setStyleName(getItemTheme("FrameBorder-tl"));
		imgTopRight.setStyleName(getItemTheme("FrameBorder-tr"));
		bottomRow.getCellFormatter().setStyleName(0, 0, getItemTheme("FrameBorder-bl"));
		imgBot.setStyleName(getItemTheme("FrameBorder-b"));
		bottomRow.getCellFormatter().setStyleName(0, 1, getItemTheme("FrameBorder-b"));
		topRow.getCellFormatter().setStyleName(0, 1, getItemTheme("FrameBorder-t"));
		centerRow.getCellFormatter().setStyleName(0, 1, getItemTheme("FrameContent"));


		// myContent.setStyleName(getItemTheme("FrameContent"));


		centerRow.getCellFormatter().setStyleName(0, 0, getItemTheme("FrameBorder-l"));
		centerRow.getCellFormatter().setStyleName(0, 2, getItemTheme("FrameBorder-r"));
		centerLeftLabel.setStyleName(getItemTheme("FrameBorder-l"));
		centerRightLabel.setStyleName(getItemTheme("FrameBorder-r"));
		topRow.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_BOTTOM);
		topRow.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_BOTTOM);

		bottomRow.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
		bottomRow.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_TOP);
		if (resizable) {
			resizeImage.setTheme(currentTheme);
			bottomRow.setWidget(0, 2, resizeImage);
			bottomRow.getCellFormatter().setStyleName(0, 2, getItemTheme("FrameBorder-br"));
		} else {
			bottomRow.getCellFormatter().setStyleName(0, 2, getItemTheme("FrameBorder-br"));
		}

		System.out.println("resi " + resizable + " " + resizeImage);

		// Logger.log("Applied theme");
		//
		// // DOM.setStyleAttribute(getStyleElement(), "z-index", "999");
		//
		//
		// DOM.setStyleAttribute(ui.getElement(), "z-index", "999");
		// Logger.log("special apply ");
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
		if (resizable) {
			bottomRow.setWidget(0, 2, resizeImage);
			bottomRow.getCellFormatter().setStyleName(0, 2, getItemTheme("FrameBorder-br"));
			resizeImage.setTheme(currentTheme);
		} else {
			bottomRow.setHTML(0, 2, "&nbsp;");
			bottomRow.getCellFormatter().setStyleName(0, 2, getItemTheme("FrameBorder-br"));
		}
	}

	protected void buildGui() {
		this.ui = new FlexTable();

		if (this.width < this.minWidth) {
			this.width = this.minWidth;
		}
		if (this.height < this.minHeight) {
			this.height = this.minHeight;
		}
		setSize(this.width, this.height);
		topRow.setWidget(0, 0, imgTopLeft);

		captionWidget = new Label();
		topRow.setWidget(0, 1, captionWidget);

		topRow.setWidget(0, 2, imgTopRight);
		bottomRow.setHTML(0, 0, "&nbsp;");
		bottomRow.setWidget(0, 1, imgBot);
		bottomRow.setHTML(0, 2, "&nbsp;");

		centerLeftLabel = new Label();

		centerRow.setWidget(0, 0, centerLeftLabel);
		// centerRow.setWidget(0, 1, myContent);

		centerRow.getFlexCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		centerRightLabel = new Label();
		centerRow.setWidget(0, 2, centerRightLabel);


		ui.getCellFormatter().setHeight(1, 0, "100%");
		ui.getCellFormatter().setWidth(1, 0, "100%");
		ui.getCellFormatter().setAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER,
				HasVerticalAlignment.ALIGN_MIDDLE);
		ui.setCellPadding(0);
		ui.setCellSpacing(0);
		ui.setWidget(0, 0, topRow);
		ui.setWidget(1, 0, centerRow);
		ui.setWidget(2, 0, bottomRow);

		initWidget(ui);

		setTheme(currentTheme);

		topRow.setCellPadding(0);
		topRow.setCellSpacing(0);
		topRow.setHeight("100%");
		topRow.getCellFormatter().setWidth(0, 1, "100%");
		centerRow.setCellPadding(0);
		centerRow.setCellSpacing(0);
		centerRow.setWidth("100%");
		centerRow.setHeight("100%");
		centerRow.setBorderWidth(0);

		bottomRow.setCellPadding(0);
		bottomRow.setCellSpacing(0);
		bottomRow.setWidth("100%");
		// bottomRow.getCellFormatter().setWidth(0, 1, "100%");


		// if (visible) {
		// setSize(getOffsetWidth(), getOffsetHeight());
		// }
	}

	private String getItemTheme(String item) {
		return "gwm-" + currentTheme + "-" + item;
	}

	public String getTheme() {
		return this.currentTheme;
	}


	public void setCaption(Widget w) {
		w.addStyleName(getItemTheme("FrameCaption"));
		setCaption(w, w);
	}

	/**
	 * NOTE dragHandle must implement SourcesMouseEvents if you want this to be draggable.
	 * 
	 * These two widgets will be separate if you have a Composite caption that doesn't do
	 * SourcesMouseEvents itself. The dragHandle needs to be the widget that fires the event, or the
	 * Dragger will not find it in its lookup dragHandlers.
	 * 
	 * @param dragHandle
	 * @param w
	 */
	public void setCaption(Widget dragHandle, Widget w) {
		this.dragHandle = dragHandle;
		captionWidget = w;
		captionWidget.setStyleName(getItemTheme("FrameBorder-t"));
		topRow.setWidget(0, 1, captionWidget);
	}


	// @Override
	public void setContent(Widget w) {
		myContent = w;
		centerRow.setWidget(0, 1, w);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;

		ui.setSize(width + "px", height + "px");

		fireResizeEvent();
	}

	private void fireResizeEvent() {
		if (resizeListeners != null) {
			for (ResizeHandler handler : resizeListeners) {
				handler.resize(width, height);
			}
		}
	}

	public void setTheme(String theme) {
		this.currentTheme = theme;
		applyTheme();
	}

	public void setWidth(int width) {
		setSize(width, height);
	}

	public int getWidth() {
		int widthResult = 0;
		if (getOffsetWidth() > 0) {
			widthResult = getOffsetWidth();
			return widthResult;
		}
		try {
			String widthStr = DOM.getStyleAttribute(ui.getElement(), "width");
			widthStr = widthStr.replaceAll("px", "");
			int width = Integer.parseInt(widthStr);
			return width;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void setHeight(int height) {
		setSize(width, height);
	}

	public int getHeight() {
		if (getOffsetHeight() > 0)
			return getOffsetHeight();
		try {
			String heightStr = DOM.getStyleAttribute(ui.getElement(), "height");
			heightStr = heightStr.replaceAll("px", "");
			int height = Integer.parseInt(heightStr);
			return height;

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}


	public int getMinimumWidth() {
		return minWidth;
	}

	public int getMinimumHeight() {
		return minHeight;
	}

	public Widget getDragHandle() {
		return dragHandle;
	}

	public final void addResizeHandler(ResizeHandler handler) {
		if (resizeListeners == null) {
			resizeListeners = new ArrayList<ResizeHandler>();
		}
		resizeListeners.add(handler);
	}

}
