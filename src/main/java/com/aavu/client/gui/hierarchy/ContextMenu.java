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

	/**
	 * No x,y params mean we'll just create a new topic and have it placed automatically
	 * 
	 * @param m
	 * @param display
	 */
	public ContextMenu(final Manager m, final SpatialDisplay display) {
		this(m, display, -1, -1);
	}

	/**
	 * x,y, should be used for new topic location
	 * 
	 * @param m
	 * @param display
	 * @param x
	 * @param y
	 */
	public ContextMenu(final Manager m, final SpatialDisplay display, final int x, final int y) {
		super(true);
		this.x = x;
		this.y = y;

		System.out.println("ContextMenu " + x + " " + y);

		VerticalPanel mainPanel = new VerticalPanel();


		for (Iterator iter = m.getGadgetManager().getFullGadgetList().iterator(); iter.hasNext();) {
			final Gadget gadget = (Gadget) iter.next();

			if (!gadget.isOnContextMenu()) {
				continue;
			}

			Image imgButton = gadget.getPickerButton();
			imgButton.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					if (x != -1 || y != -1) {
						m.getGadgetManager().fireGadgetClick(gadget, display.getLongLatForXY(x, y));
					} else {
						m.getGadgetManager().fireGadgetClick(gadget, null);
					}
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
		setPopupPosition(x, y);
		super.show();
	}


}
