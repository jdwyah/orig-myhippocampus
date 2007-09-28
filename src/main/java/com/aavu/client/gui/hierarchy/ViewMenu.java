package com.aavu.client.gui.hierarchy;

import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ViewMenu extends PopupPanel {


	private Manager manager;

	/**
	 * x,y, should be used for new topic location
	 * 
	 * @param m
	 * @param display
	 * @param x
	 * @param y
	 */
	public ViewMenu(final Manager m) {
		super(true);
		this.manager = m;


		VerticalPanel mainPanel = new VerticalPanel();


		mainPanel.add(addView(ConstHolder.images.bookAZ().createImage(), "A-Z",
				new ClickListener() {
					public void onClick(Widget sender) {
						manager.getGui().showGlossary();
						hide();
					}
				}));
		mainPanel.add(addView(ConstHolder.images.hourglassbase().createImage(), "Timeline",
				new ClickListener() {
					public void onClick(Widget sender) {
						manager.getGui().showTimeline();
						hide();
					}
				}));
		// mainPanel.add(addView(ConstHolder.images.gadgetConnections().createImage(),
		// "Connections",
		// new ClickListener() {
		// public void onClick(Widget sender) {
		// manager.getGui().showConnections();
		// hide();
		// }
		// }));


		mainPanel.add(addView(ConstHolder.images.gadgetMap().createImage(), "Maps",
				new ClickListener() {
					public void onClick(Widget sender) {
						manager.getGui().showGoogleMap();
						hide();
					}
				}));


		add(mainPanel);

		setStyleName("H-ContextMenu");
		addStyleName("H-BlueFade");
	}

	private Widget addView(Image image, String string, ClickListener listener) {
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(image);
		hp.add(new Label(string));
		image.addClickListener(listener);
		return hp;
	}

	public void show(int x, int y) {
		setPopupPosition(x, y);
		super.show();
	}


}
