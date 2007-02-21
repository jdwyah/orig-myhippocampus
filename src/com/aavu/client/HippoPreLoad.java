package com.aavu.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class HippoPreLoad implements EntryPoint {


	/**
	 * This is the entry point method.
	 *  
	 * 
	 */
	public void onModuleLoad() {

		RootPanel.get("loading").setVisible(false);
		RootPanel.get("slot1").add(new Label("Preload complete"));		
		
	}


	

}
