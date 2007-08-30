package com.aavu.client.gui.gadgets;

import org.gwm.client.impl.DefaultGInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.GadgetDisplayer;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class GadgetPopup extends DefaultGInternalFrame implements GadgetHolder {

	private Gadget gadget;
	private GadgetDisplayer displayer;
	public static final int POPUPWIDTH = 240;

	public GadgetPopup(Gadget gadget, GadgetDisplayer displayer) {
		super(gadget.getDisplayName());
		this.displayer = displayer;
		this.gadget = gadget;
		width = POPUPWIDTH;
		height = 20;

		setTheme("alphacube");


		setMinimizable(false);
		setMaximizable(false);

		setDraggable(true);
		setEffects(false);

		setClosable(false);

		gadget.setGadgetHolder(this);

		setContent(gadget);


		centerRow.getFlexCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
	}

	public int getLastLoadSize() {
		return gadget.getLastLoadSize();
	}

	/**
	 * PEND TODO
	 */
	public boolean isVisible() {
		return true;
	}

	public void resetSize() {
		setWidth(POPUPWIDTH);
		setHeight(0);
	}

	public void nowShowTopic(Topic topic) {
		resetSize();
		setCaption(gadget.getDisplayName(topic));
	}

	public boolean showForIsCurrent(boolean isCurrent) {
		return gadget.showForIsCurrent(isCurrent);
	}

	public void normalize(int top, int left) {
		setLocation(top, left);
		gadget.normalize(this);
	}

	public Gadget getGadget() {
		return gadget;
	}

	public boolean isOnForTopic(Topic topic) {
		return gadget.isOnForTopic(topic);
	}

	public void fireSizeChanged() {
		displayer.fireSizeChanged();
	}


}
