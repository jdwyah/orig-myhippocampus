package com.aavu.client.gui.timeline.zoomer;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.gui.ContextMenu;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.maps.HippoMapWidget;
import com.aavu.client.service.Manager;

public class BigMapContextMenu extends ContextMenu {


	private HippoMapWidget mapWidget;



	/**
	 * x,y, should be used for new topic location
	 * 
	 * @param m
	 * @param timeline
	 * @param x
	 * @param y
	 */
	public BigMapContextMenu(final Manager m, final HippoMapWidget mapWidget,
			final HippoLocation location) {
		super(m, location.getLongitude(), location.getLatitude());
		this.mapWidget = mapWidget;
	}

	// @Override
	protected boolean useGadget(Gadget gadget) {
		return gadget.isOnMapContextMenu();
	}


	// @Override
	protected void fireGadget(Gadget gadget) {
		int[] xy = new int[] { x, y };
		m.getGadgetManager().fireGadgetClick(gadget, xy, null);
		hide();
	}
}
