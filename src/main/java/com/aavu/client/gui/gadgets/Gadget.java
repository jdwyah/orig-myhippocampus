package com.aavu.client.gui.gadgets;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
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
	private GadgetHolder gadgetHolder;
	protected Manager manager;

	// implements SourcesMouseEvents {


	// private BorderThemedPanel borderPanel;
	// private FocusPanel focusPanel;


	/**
	 * Child Gadget responsible for its own title bar w/ this constructor.
	 * 
	 */
	public Gadget(Manager manager) {
		this.manager = manager;
		// borderPanel = new BorderThemedPanel();
		// borderPanel.setCaption("unset");
		//
		//
		// focusPanel = new FocusPanel();
		// focusPanel.add(borderPanel);
		//
		// setWidget(focusPanel);


	}



	public GadgetHolder getGadgetHolder() {
		return gadgetHolder;
	}


	public void setGadgetHolder(GadgetHolder gadgetHolder) {
		this.gadgetHolder = gadgetHolder;
	}


	private void init() {
		// setStyleName("H-DisplayPanel");

		addStyleName("H-Gadget");
	}


	protected void setHeader(Widget widget) {
		setTitle("special set");

	}

	// @Override
	protected void initWidget(Widget widget) {
		super.initWidget(widget);
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

	public void setVisible(boolean b) {
		gadgetHolder.setVisible(b);
	}

	public boolean isVisible() {
		return gadgetHolder.isVisible();
	}



	public Manager getManager() {
		return manager;
	}

	// @Override
	public void createInstance(Manager manager, int[] lngLat) {
		throw new UnsupportedOperationException();
	}

	public boolean isDisplayer() {
		return false;
	}

	public boolean isOnContextMenu() {
		return false;
	}

	// public void addMouseListener(MouseListener listener) {
	// focusPanel.addMouseListener(listener);
	// }
	//
	// public void removeMouseListener(MouseListener listener) {
	// focusPanel.removeMouseListener(listener);
	// }
}
