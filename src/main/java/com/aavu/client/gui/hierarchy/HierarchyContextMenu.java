package com.aavu.client.gui.hierarchy;

import com.aavu.client.gui.ContextMenu;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.ocean.SpatialDisplay;
import com.aavu.client.service.Manager;

public class HierarchyContextMenu extends ContextMenu {


	private SpatialDisplay display;


	/**
	 * No x,y params mean we'll just create a new topic and have it placed automatically
	 * 
	 * @param m
	 * @param display
	 */
	public HierarchyContextMenu(final Manager m, final SpatialDisplay display) {
		this(m, display, -1, -1);
	}

	/**
	 * x,y, should be used for new topic location
	 * 
	 * @param m
	 * @param display
	 * @param x
	 * @param y
	 */
	public HierarchyContextMenu(final Manager m, final SpatialDisplay display, final int x,
			final int y) {
		super(m, x, y);
		this.display = display;
	}

	// @Override
	protected boolean useGadget(Gadget gadget) {
		return gadget.isOnContextMenu();
	}


	// @Override
	protected void fireGadget(Gadget gadget) {
		if (x != -1 || y != -1) {
			m.getGadgetManager().fireGadgetClick(gadget, display.getLongLatForXY(x, y), null);
		} else {
			m.getGadgetManager().fireGadgetClick(gadget, null, null);
		}
		hide();
	}

}
