package com.aavu.client.gui.timeline.draggable;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.FocusPanelExt;
import com.aavu.client.gui.ocean.dhtmlIslands.TimelineRemembersPosition;
import com.aavu.client.gui.timeline.zoomer.ZoomableTimeline;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.Widget;

public class TLORangeWidget extends FocusPanelExt implements TimelineRemembersPosition,
		SourcesMouseWheelEvents {


	private static final int MIN_WIDTH = 2;
	private static final int X_MARGIN = 10;
	private static final int LABEL_HOLDER_HEIGHT = 13;
	private static final int EDGE_WIDTH = 16;

	private int top;
	private int left;
	private TimeLineObj tlo;
	private ZoomableTimeline timeline;

	private int sizeThisZoom;


	private Label label;
	private HippoDate hippoDate;
	private AbsolutePanel labelHolder;
	private Label labelBG;
	private TLORangeEdge rightEdge;
	private double currentScale;

	public TLORangeWidget(ZoomableTimeline timeline, final TimeLineObj tlo) {
		super();
		this.tlo = tlo;
		this.left = tlo.getLeft();
		this.top = 0;
		this.timeline = timeline;


		// TODO cast
		hippoDate = (HippoDate) tlo.getHasDate();

		label = new Label(tlo.getTopicIdentifier().getTopicTitle() + " " + hippoDate.getTitle(),
				false);
		labelBG = new Label(" ");


		// HorizontalPanel hp = new HorizontalPanel();

		TLORangeEdge leftEdge = new TLORangeEdge(this, true);
		rightEdge = new TLORangeEdge(this, false);


		labelHolder = new AbsolutePanel();

		labelHolder.add(labelBG, 0, 0);
		labelHolder.add(label, EDGE_WIDTH, 0);

		labelHolder.add(leftEdge, 0, 0);
		labelHolder.add(rightEdge, 0, 0);


		label.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				System.out.println("Clicked start " + hippoDate.getStartDate() + " to "
						+ hippoDate.getEndDate());
			}
		});

		setWidget(labelHolder);

		label.setStyleName("H-TimeBar-Label");
		labelBG.setStyleName("H-TimeBar-BG");

		zoomToScale(1.0);

	}


	public int getLeft() {
		return left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public Widget getWidget() {
		return this;
	}

	public void zoomToScale(double currentScale) {
		this.currentScale = currentScale;

		int start = timeline.getPositionX(tlo.getLeftForDate(hippoDate.getStartDate()));
		int end = timeline.getPositionX(tlo.getLeftForDate(hippoDate.getEndDate()));

		sizeThisZoom = end - start;

		sizeThisZoom = sizeThisZoom < MIN_WIDTH ? MIN_WIDTH : sizeThisZoom;

		System.out.println("TLORange  st:" + start + " en:" + end + " size " + sizeThisZoom
				+ " sd " + hippoDate.getStartDate() + " ed " + hippoDate.getEndDate());

		DOM.setStyleAttribute(labelBG.getElement(), "width", sizeThisZoom + "px");

		setCurWidth();
	}


	private void setCurWidth() {
		int width = getWidth();

		labelHolder.setPixelSize(width, LABEL_HOLDER_HEIGHT);
		labelHolder.setWidgetPosition(rightEdge, width - EDGE_WIDTH, 0);
	}


	public int getWidth() {

		int minWidth = label.getOffsetWidth() + (2 * EDGE_WIDTH);

		System.out.println("get TLO RANGE WIDTH  " + sizeThisZoom + " "
				+ tlo.getTopicIdentifier().getTopicTitle());

		int dateWidth = (sizeThisZoom + X_MARGIN);

		int rtnW = dateWidth > minWidth ? dateWidth : minWidth;

		return rtnW;
	}

	public void expand(int x, boolean leftSide) {
		System.out.println("-----------------\nexpand " + x + " "
				+ timeline.getPositionXFromGUIX(x) + " left " + getLeft());


		if (leftSide) {
			System.out.println("LEFT left " + left + " size " + sizeThisZoom);
			// left += dx;

			System.out.println("tlo start " + tlo.getHasDate().getStartDate());

			tlo.setStartDateToX(timeline.getPositionXFromGUIX(x));

			System.out.println("tlo2 start " + tlo.getHasDate().getStartDate());


		} else {
			// System.out.println("RIGHT left " + left + " size " + sizeThisZoom);

			System.out.println("tlo end " + tlo.getHasDate().getEndDate());

			tlo.setEndDateToX(timeline.getPositionXFromGUIX(x));

			System.out.println("tlo2 end " + tlo.getHasDate().getEndDate());
		}


		left = tlo.getLeft();


		timeline.redraw(this);

		// zoomToScale(currentScale);
	}
}
