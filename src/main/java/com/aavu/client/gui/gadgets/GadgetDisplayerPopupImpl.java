package com.aavu.client.gui.gadgets;

import java.util.HashSet;
import java.util.Iterator;

import org.gwm.client.impl.DefaultGInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class GadgetDisplayerPopupImpl extends DefaultGInternalFrame implements GadgetDisplayer {

	private GadgetManager gadgetManager;
	private HashSet popups;

	public GadgetDisplayerPopupImpl(Manager manager) {
		this.gadgetManager = manager.getGadgetManager();

		popups = new HashSet();

		for (Iterator iterator = gadgetManager.getFullGadgetList().iterator(); iterator.hasNext();) {
			Gadget gadget = (Gadget) iterator.next();

			if (!gadget.isDisplayer()) {
				continue;
			}

			GadgetPopup popup = manager.newFrameGadget(gadget);
			popups.add(popup);


		}
		normalize();
	}

	private void normalize() {

		int left = Window.getClientWidth() - 200;

		int cTop = 0;

		for (Iterator iterator = popups.iterator(); iterator.hasNext();) {
			GadgetPopup popup = (GadgetPopup) iterator.next();
			popup.setLocation(cTop, left);
			System.out.println("normalize " + popup.getHeight());

			cTop += popup.getOffsetHeight();
		}
	}

	public Widget getWidget() {
		return null;
	}

	public void load(Topic topic) {
		System.out.println("gadgetPopuDisplayer load");
		gadgetManager.load(topic);

		for (Iterator iterator = popups.iterator(); iterator.hasNext();) {
			GadgetPopup popup = (GadgetPopup) iterator.next();
			popup.nowShowTopic(topic);

			// System.out.println("GadgetDisplayPopupImpl popup set vis " + popup.getLeft() + " " +
			// popup.getTop());


			// popup.setWidth(100);
			// popup.setHeight(100);

			System.out.println("w h " + popup.getOffsetWidth() + " " + popup.getOffsetHeight());

			popup.setVisible(true);

		}
		normalize();
	}

	/**
	 * do nothing, since our popups don't need any help
	 */
	public void addTo(Panel mainP) {
	}

	public void unload() {
		// System.out.println("-----------GadgetDisplayPopup Unload");

		for (Iterator iterator = popups.iterator(); iterator.hasNext();) {
			GadgetPopup popup = (GadgetPopup) iterator.next();

			popup.setVisible(false);

		}

	}

	public void gadgetClicked(Gadget gadget, int[] lngLat) {
		// TODO Auto-generated method stub

	}

}
