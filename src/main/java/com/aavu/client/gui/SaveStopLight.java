package com.aavu.client.gui;

import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;


public class SaveStopLight extends Composite {
	private Image redLight;
	private StackPanel sp;
	private boolean saveNeeded;

	public SaveStopLight(final ClickListener saveNow) {
		sp = new StackPanel();
		sp.setStyleName("H-SaveLight");


		sp.add(ConstHolder.images.greenLight().createImage());

		redLight = ConstHolder.images.redLight().createImage();
		redLight.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				saveNow.onClick(SaveStopLight.this);
			}
		});
		redLight.addMouseListener(new TooltipListener(ConstHolder.myConstants.save()));
		sp.add(redLight);

		initWidget(sp);
	}

	public void setSaveNeeded() {
		saveNeeded = true;
		sp.showStack(1);
	}

	public void saveAccomplished() {
		saveNeeded = false;
		sp.showStack(0);
	}

	public boolean isSaveNeeded() {
		return saveNeeded;
	}
}
