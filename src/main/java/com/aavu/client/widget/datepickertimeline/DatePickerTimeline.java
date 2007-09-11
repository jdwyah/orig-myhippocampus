package com.aavu.client.widget.datepickertimeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ocean.dhtmlIslands.DragEventListener;
import com.aavu.client.gui.ocean.dhtmlIslands.DragHandler;
import com.aavu.client.gui.ocean.dhtmlIslands.TimelineRemembersPosition;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.zoomer.ZoomableTimeline;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.datepicker.DateFormat;
import com.aavu.client.widget.datepicker.DatePickerInterface;
import com.google.gwt.user.client.ui.Widget;

public class DatePickerTimeline extends ZoomableTimeline implements DragEventListener,
		DatePickerInterface {

	private DragHandler dragHandler;

	public DatePickerTimeline(Manager manager, int width, int height, CloseListener window) {
		super(manager, width, height, window);

		dragHandler = new DragHandler(this);
		dragHandler.setDoYTranslate(false);

		addStyleName("H-DatePickerTimeline");
	}

	// @Override
	protected TimelineRemembersPosition getTLORepr(Manager manager, TimeLineObj tlo, int left,
			int top) {
		DraggableTimeLineObj tlow = new DraggableTimeLineObj(manager, tlo, left, top);
		tlow.addMouseWheelListener(this);



		dragHandler.add(tlow, this);


		return tlow;
	}

	public void dragFinished(Widget dragging) {
		System.out.println("drag finished " + dragging);
		DraggableTimeLineObj dtlo = (DraggableTimeLineObj) dragging;

		// dragging.get

	}

	public void setSelectedDate(Date created) {
		List timelines = new ArrayList();

		throw new UnsupportedOperationException();
		// timelines.add(new TimeLineObj(t);

		// add(timelines);
	}

	public void dragged(Widget dragging, int newX, int newY) {
		DraggableTimeLineObj dtlo = (DraggableTimeLineObj) dragging;

		System.out.println("dragged " + newX + " " + newY);
		// dtlo.setleft(newX);

	}

	public Date getCurrentDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public Date getSelectedDate() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDateFormat(DateFormat date_format_mmddyyyy) {
		// TODO Auto-generated method stub

	}

	public void setWeekendSelectable(boolean b) {
		// TODO Auto-generated method stub

	}


}
