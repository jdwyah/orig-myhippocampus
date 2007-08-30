package com.aavu.client.gui;

import java.util.Iterator;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.Ribbon;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GadgetDisplayerBarImpl extends Composite implements GadgetDisplayer {

	private Manager manager;

	private VerticalPanel gadgetPanel;
	private Ribbon gadgetPicker;

	private Topic topic;


	public GadgetDisplayerBarImpl(final Manager manager) {

		this.manager = manager;


		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

		gadgetPanel = new VerticalPanel();
		gadgetPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);

		gadgetPicker = new Ribbon(manager.getGadgetManager());
		manager.getGadgetManager().addGadgetClickListener(this);

		mainPanel.add(gadgetPicker);
		mainPanel.add(gadgetPanel);

		initWidget(mainPanel);

		addStyleName("H-AbsolutePanel");
		addStyleName("H-RightInfo");

		setVisible(true);
	}


	public void load(Topic topic, boolean isCurrent) {

		this.topic = topic;

		manager.getGadgetManager().load(topic);

		gadgetPanel.clear();


		for (Iterator iter = manager.getGadgetManager().getFullGadgetList().iterator(); iter
				.hasNext();) {
			Gadget gadget = (Gadget) iter.next();

			if (!gadget.isDisplayer()) {
				continue;
			}

			gadget.setVisible(true);
			gadgetPanel.add(gadget);
		}

		setVisible(true);

		for (Iterator iter = manager.getGadgetManager().getFullGadgetList().iterator(); iter
				.hasNext();) {
			Gadget gadget = (Gadget) iter.next();
			gadget.nowVisible();
		}

	}

	public void unload() {
		gadgetPicker.close();
		setVisible(false);
	}


	public void gadgetClicked(Gadget gadget, int[] lngLat) {
		if (!gadget.enabled(manager.getUser())) {
			manager.displayInfo(ConstHolder.myConstants.gadget_not_available());
			return;
		}
		/*
		 * if this is a first time add
		 */
		if (!gadgetPanel.isVisible()) {
			gadget.load(topic);

			gadget.setVisible(true);
			// gadgetPanel.add(gadget);


			gadget.showForFirstTime();
		} else {
			if (gadget.isVisible()) {
				manager.displayInfo(ConstHolder.myConstants.gadget_already_showing());
			} else {
				// This will force the ConnectionBoard to unhide itself
				gadget.setVisible(true);
			}
		}
	}



	public void addTo(Panel mainP) {
		mainP.add(this);
	}


	public void fireSizeChanged() {
		// TODO Auto-generated method stub

	}

	// public void onClick(Widget sender) {
	// if(sender == entryPreview){
	// manager.editEntry(topic);
	// }
	// }
}
