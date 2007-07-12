package com.aavu.client.gui.gadgets;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Gadgets are the GUI building blocks for topics. An topic can have gadgets added to it they're a
 * bit like super powers.
 * 
 * 
 * @author Jeff Dwyer
 * 
 */
public abstract class Gadget extends Composite {

	protected DisclosurePanel mainP;

	/**
	 * Child Gadget responsible for its own title bar w/ this constructor.
	 * 
	 */
	public Gadget() {

		mainP = new DisclosurePanel();
		super.initWidget(mainP);
		mainP.setOpen(true);
		addStyleName("H-Gadget");
	}

	public Gadget(String title) {
		mainP = new DisclosurePanel();
		mainP.setHeader(new HeaderLabel(title));
		mainP.setOpen(true);

		super.initWidget(mainP);

		addStyleName("H-Gadget");
	}

	protected void setHeader(Widget widget) {
		mainP.setHeader(widget);
	}

	// @Override
	protected void initWidget(Widget widget) {
		mainP.add(widget);
	}

	public abstract int load(Topic topic);

	public abstract Image getPickerButton();

	public void showForFirstTime() {
	}

	public abstract boolean isOnForTopic(Topic topic);

	/**
	 * Overridden by Gadgets that need to now when the GadgetDisplayer is visible
	 * 
	 */
	public void nowVisible() {
	}

	public boolean enabled(User user) {
		return true;
	}

	public abstract String getDisplayName();

	public abstract void onClick(Manager manager);

}
