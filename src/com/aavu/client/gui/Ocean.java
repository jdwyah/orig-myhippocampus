package com.aavu.client.gui;

import com.aavu.client.gui.ext.MultiDivPanel;

public class Ocean extends MultiDivPanel {

	public Ocean(){
		
	
		Island i1 = new Island(40,55,180);
		Island i2 = new Island(60,400,400);
		
		add(i1);
		add(i2);
		
		setStyleName("GuiTest-Ocean");
	}
	
	
}
