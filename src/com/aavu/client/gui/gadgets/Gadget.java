package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Topic;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class Gadget extends Composite {
	
	private VerticalPanel mainP;

	public Gadget(String title){
		mainP = new VerticalPanel();
		mainP.add(new HeaderLabel(title));		
		
		super.initWidget(mainP);
		
		addStyleName("H-Gadget");
	}
	
	//@Override
	protected void initWidget(Widget widget) {
		mainP.add(widget);				
	}
	
	public abstract int load(Topic topic);

	public abstract ImageButton getPickerButton();
	
}
