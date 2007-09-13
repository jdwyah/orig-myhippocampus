package com.aavu.client.gui;

import java.util.Iterator;

import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class ContextMenu extends PopupPanel {

	protected int x;
	protected int y;
	protected Manager m;

	public ContextMenu(final Manager m, final int x, final int y) {
		super(true);
		this.x = x;
		this.y = y;
		this.m = m;

		System.out.println("ContextMenu " + x + " " + y);

		VerticalPanel mainPanel = new VerticalPanel();

		for (Iterator iter = m.getGadgetManager().getFullGadgetList().iterator(); iter.hasNext();) {
			final Gadget gadget = (Gadget) iter.next();


			if (!useGadget(gadget)) {
				continue;
			}

			Image imgButton = gadget.getPickerButton();
			imgButton.addClickListener(new ClickListener() {

				public void onClick(Widget sender) {
					fireGadget(gadget);
				}
			});

			HorizontalPanel hp = new HorizontalPanel();
			hp.add(imgButton);
			hp.add(new Label(gadget.getDisplayName()));
			mainPanel.add(hp);

		}

		// mainPanel.add(new HoverLabel("New Topic"));
		// mainPanel.add(new HoverLabel("New Connection"));

		add(mainPanel);

		setStyleName("H-ContextMenu");
		addStyleName("H-BlueFade");
	}

	protected abstract boolean useGadget(Gadget gadget);

	protected abstract void fireGadget(Gadget gadget);

	public void show(int x, int y) {
		setPopupPosition(x, y);
		super.show();
	}


}
