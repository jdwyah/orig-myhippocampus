package com.aavu.client.gui;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.util.LorumIpsum;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

public class Chart extends PopupWindow {

	public Chart(){
		super("caption");
		
		HorizontalPanel p = new HorizontalPanel();
		
		p.add(new Label(LorumIpsum.getLorum(40)));
		p.add(new Button("Close"));
		
		setWidget(p);
	}
	
}
