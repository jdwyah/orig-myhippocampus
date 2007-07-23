package com.aavu.client.gui.hierarchy;

import java.util.Iterator;

import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.ocean.SpatialDisplay;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContextMenu extends PopupPanel {
	private int x;
	private int y;

	public ContextMenu(final Manager m, final SpatialDisplay display) {
		super(true);

		VerticalPanel mainPanel = new VerticalPanel();


		for (Iterator iter = m.getGadgetManager().getFullGadgetList().iterator(); iter.hasNext();) {
			final Gadget gadget = (Gadget) iter.next();

			Image imgButton = gadget.getPickerButton();
			imgButton.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					m.getGadgetManager().fireGadgetClick(gadget, display.getLongLatForXY(x, y));
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

	public void show(int x, int y) {
		this.x = x;
		this.y = y;
		setPopupPosition(x, y);
		super.show();
	}


}
