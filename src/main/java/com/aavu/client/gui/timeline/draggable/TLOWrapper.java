package com.aavu.client.gui.timeline.draggable;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.FocusPanelExt;
import com.aavu.client.gui.ext.JSUtil;
import com.aavu.client.gui.ocean.dhtmlIslands.TimelineRemembersPosition;
import com.aavu.client.gui.timeline.zoomer.ZoomableTimeline;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.Widget;

public class TLOWrapper extends FocusPanelExt implements TimelineRemembersPosition,
		SourcesMouseWheelEvents {


	private TimeLineObj tlo;
	private int top;
	private Label label;

	private Image image;

	public TLOWrapper(final Manager manager, ZoomableTimeline timeline, final TimeLineObj tlo,
			Image image) {
		this.tlo = tlo;
		this.top = 0;
		this.image = image;

		HorizontalPanel panel = new HorizontalPanel();

		label = new Label(tlo.getTopicIdentifier().getTopicTitle(), false);

		TLORangeEdge edge = new TLORangeEdge(timeline, tlo, this, true, image);

		panel.add(edge);
		panel.add(label);

		addClickListener(timeline);
		addDblClickListener(timeline);

		setWidget(panel);

		label.setStyleName("H-TLOWrapper");

		JSUtil.disableSelect(getElement());
	}

	public int getLeft() {
		return tlo.getLeft();
	}

	public int getTop() {
		return top;
	}

	public Widget getWidget() {

		return this;
	}

	public void addMouseWheelListener(MouseWheelListener listener) {
		label.addMouseWheelListener(listener);
		image.addMouseWheelListener(listener);
	}

	public void removeMouseWheelListener(MouseWheelListener listener) {
		image.removeMouseWheelListener(listener);
		label.removeMouseWheelListener(listener);
	}

	public void setTop(int top) {
		this.top = top;
	}



	public void zoomToScale(double currentScale) {



	}

	public TimeLineObj getTLO() {
		return tlo;
	}

	/**
	 * PEND MED weak 11 * #letters = width assumption
	 */
	public int getWidth() {
		return 11 * tlo.getTopicIdentifier().getTopicTitle().length();
	}

	public String toString() {
		return "TLOWrapper " + tlo.getTopicIdentifier().getTopicTitle();
	}

	public void updateTitle() {
		label.setText(tlo.getHasDate().getTitle());
	}

}
