package com.aavu.client.gui.gadgets;

import org.gwm.client.impl.DefaultGInternalFrame;

import com.aavu.client.domain.Topic;

public class GadgetPopup extends DefaultGInternalFrame implements GadgetHolder {

	private Gadget gadget;

	public GadgetPopup(Gadget gadget) {
		super(gadget.getDisplayName());
		this.gadget = gadget;
		width = 200;
		height = 20;

		setTheme("alphacube");


		setMinimizable(false);
		setMaximizable(false);

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

	public void resetSize() {
		setWidth(200);
		setHeight(0);
	}

	public void nowShowTopic(Topic topic) {
		resetSize();
		setCaption(topic.getTitle() + " " + gadget.getDisplayName());
	}


}
