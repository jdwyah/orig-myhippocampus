package com.aavu.client.gui.timeline.zoomer;

import java.util.Date;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ContextMenu;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.service.Manager;

public class TimelineContextMenu extends ContextMenu {


	private ZoomableTimeline timeline;



	/**
	 * x,y, should be used for new topic location
	 * 
	 * @param m
	 * @param timeline
	 * @param x
	 * @param y
	 */
	public TimelineContextMenu(final Manager m, final ZoomableTimeline timeline, final int x) {
		super(m, x, -1);
		this.timeline = timeline;
	}

	// @Override
	protected boolean useGadget(Gadget gadget) {
		return gadget.isOnTimelineContextMenu();
	}


	// @Override
	protected void fireGadget(Gadget gadget) {
		Date date = TimeLineObj.getDateFromViewPanelX(timeline.getPositionXFromGUIX(x));

		m.getGadgetManager().fireGadgetClick(gadget, null, date);

		hide();
	}

}
