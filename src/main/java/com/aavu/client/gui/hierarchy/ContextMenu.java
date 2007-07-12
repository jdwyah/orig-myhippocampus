package com.aavu.client.gui.hierarchy;

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

public class ContextMenu extends PopupPanel {
	public ContextMenu(final Manager m) {
		super(true);

		VerticalPanel mainPanel = new VerticalPanel();


		for (Iterator iter = m.getGadgetManager().getFullGadgetList().iterator(); iter.hasNext();) {
			final Gadget gadget = (Gadget) iter.next();

			Image imgButton = gadget.getPickerButton();
			imgButton.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					m.getGadgetManager().fireGadgetClick(gadget);
					hide();
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
}
