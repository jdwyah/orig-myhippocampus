package com.aavu.client.gui;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;


public class SaveStopLight extends Composite {
	private ImageButton redLight;
	private StackPanel sp;
	private boolean saveNeeded;

	public SaveStopLight(final ClickListener saveNow){
		sp = new StackPanel();
		sp.removeStyleName("gwt-StackPanel");
		sp.add(new ImageButton(ConstHolder.myConstants.save_greenLight(),30,30));

		redLight = new ImageButton(ConstHolder.myConstants.save_redLight(),30,30);
		redLight.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				saveNow.onClick(SaveStopLight.this);
			}});
		redLight.addMouseListener(new TooltipListener(ConstHolder.myConstants.save()));
		sp.add(redLight);

		initWidget(sp);
	}
	public void setSaveNeeded(){
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

