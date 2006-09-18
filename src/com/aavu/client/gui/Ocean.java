package com.aavu.client.gui;

import com.aavu.client.gui.ext.FlashContainer;

public class Ocean extends FlashContainer {

	public Ocean(){
		super();
	
		setStyleName("GuiTest-Ocean");		
	}
	
	public void initIslands(){
		doIslands(getCommand());
	}
}
