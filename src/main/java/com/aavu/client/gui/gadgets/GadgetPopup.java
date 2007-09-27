package com.aavu.client.gui.gadgets;

import org.gwm.client.Caption;
import org.gwm.client.impl.DefaultGInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.GadgetDisplayer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;


/**
 * note GadgetCaption extends HorizontalPanel -> all kinds of problems with the Window DnD system.
 * 
 * @author Jeff Dwyer
 * 
 */
class GadgetCaptionHTML extends HTML implements Caption {

	private String postfix = "";

	public GadgetCaptionHTML(String title) {
		super(title);
	}

	/**
	 * PEND MED don't like setHTML()
	 */
	public void setText(String text) {
		super.setHTML(text + postfix);
	}

	public Widget getWidget() {
		return this;
	}

	public void setPostFix(String display) {
		this.postfix = "<span class=\"H-GadgetPopupType\">" + display + "</span>";

		// PEND MED ugly
		if (display.equals("Title")) {
			this.postfix = "";
		}
	}
}


public class GadgetPopup extends DefaultGInternalFrame implements GadgetHolder {

	private Gadget gadget;
	private GadgetDisplayer displayer;
	public static final int POPUPWIDTH = 240;

	private GadgetCaptionHTML caption;

	public GadgetPopup(Gadget gadget, GadgetDisplayer displayer) {
		super(new GadgetCaptionHTML(gadget.getDisplayName()));
		caption = (GadgetCaptionHTML) getCaption();

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



	// @Override
	public void setTheme(String theme, String display) {
		super.setTheme(theme);
		caption.setPostFix(display);

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
