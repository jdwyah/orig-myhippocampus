package com.aavu.client.gui.hierarchy;

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

	private boolean resizable;
	private Label topLabel;
	private FlexTable topRow;

	private FlexTable ui;

	private int width;

	public BorderThemedPanel() {

		ui = new FlexTable();
		topRow = new FlexTable();
		centerRow = new FlexTable();
		bottomRow = new FlexTable();

		// resizeImage = new ResizeImage(this);

		imgTopLeft = new Label();
		imgTopRight = new Label();
		imgBot = new HTML("&nbsp;");
		this.width = DEFAULT_WIDTH;
		this.height = DEFAULT_HEIGHT;

		buildGui();

	}

	protected void applyTheme() {

		// topBar.setTheme(currentTheme);

		topLabel.setStyleName(getItemTheme("FrameBorder-t"));

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
			// resizeImage.setTheme(currentTheme);
			// bottomRow.setWidget(0, 2, resizeImage);
			bottomRow.getCellFormatter().setStyleName(0, 2, getItemTheme("FrameBorder-br"));
		} else {
			bottomRow.getCellFormatter().setStyleName(0, 2, getItemTheme("FrameBorder-br"));
		}


		// Logger.log("Applied theme");
		//
		// // DOM.setStyleAttribute(getStyleElement(), "z-index", "999");
		//
		//
		// DOM.setStyleAttribute(ui.getElement(), "z-index", "999");
		// Logger.log("special apply ");
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

		topLabel = new Label();
		topRow.setWidget(0, 1, topLabel);

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

	public void setCaption(String title) {
		topLabel.setText(title);
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
	}

	public void setTheme(String theme) {
		this.currentTheme = theme;
		applyTheme();
	}
}
