package com.aavu.client.gui;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.RootPanel;

public class Preloader {
	
	public static void preload(String url) {
		Frame iframe = new Frame(url);		
		RootPanel.get("preload").add(iframe);
		RootPanel.get("preload").setVisible(false);		
	}

}
