package com.aavu.client.gui;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;


public class SaveStopLight extends Composite {
	private ImageButton redLight;
	private StackPanel sp;

	public SaveStopLight(final ClickListener saveNow){
		sp = new StackPanel();
		sp.removeStyleName("gwt-StackPanel");
		sp.add(new ImageButton(Manager.myConstants.save_greenLight(),30,30));

		redLight = new ImageButton(Manager.myConstants.save_redLight(),30,30);
		redLight.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				saveNow.onClick(SaveStopLight.this);
			}});
		redLight.addMouseListener(new TooltipListener(Manager.myConstants.save()));
		sp.add(redLight);

		initWidget(sp);
	}
	public void setSaveNeeded(){
		sp.showStack(1);
	}
	public void saveAccomplished() {
		sp.showStack(0);
	}
}

