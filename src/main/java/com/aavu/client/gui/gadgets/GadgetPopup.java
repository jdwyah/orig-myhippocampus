package com.aavu.client.gui.gadgets;

import org.gwm.client.impl.DefaultGInternalFrame;

public class GadgetPopup extends DefaultGInternalFrame implements GadgetHolder {

	public GadgetPopup(Gadget gadget) {
		super(gadget.getDisplayName());

		width = 400;
		height = 40;

		setTheme("alphacube");


		setMinimizable(false);
		setMaximizable(true);

		setDraggable(true);
		setEffects(false);

		setClosable(false);

		gadget.setGadgetHolder(this);

		setContent(gadget);

	}

	/**
	 * PEND TODO
	 */
	public boolean isVisible() {
		return true;
	}


}
