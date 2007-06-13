package com.aavu.client;

import com.aavu.client.browser.PublicBrowser;
import com.aavu.client.service.BrowserManager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class HippocampusBrowser extends AbstractClientApp {
	
	public static final String MAIN_DIV = "slot1";
	
	
	private BrowserManager bmanager;
	
	public HippocampusBrowser(String user, String topic){

		initServices();

		bmanager = new BrowserManager(getHippoCache());
		
		
		PublicBrowser pb = new PublicBrowser(this,bmanager);
		
		
		loadGUI(pb.getWidget());
		
		pb.load(user,topic);
		
	}
	
	
	
	private void loadGUI(Widget widget) {
		RootPanel.get("loading").setVisible(false);
		RootPanel.get(MAIN_DIV).add(widget);					
	}
	
}
