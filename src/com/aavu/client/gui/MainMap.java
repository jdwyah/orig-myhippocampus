package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.FlashContainer;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.HippoCache;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;

public class MainMap extends Composite {

	private Sidebar sideBar;	
	private Manager manager;
	private TagSearch tagSearch;	
	private Ocean ocean;
	
	public MainMap(Manager manager){
		this.manager = manager;
		manager.setMap(this);
		
		MultiDivPanel mainP = new MultiDivPanel();	

		sideBar = new Sidebar(manager);
		tagSearch = new TagSearch(manager);
		
		ocean = new Ocean();
		
		mainP.add(new CompassRose());
		mainP.add(ocean);
		mainP.add(sideBar);
		mainP.add(new Dashboard(manager));
		mainP.add(tagSearch);
		
		initWidget(mainP);
	}
	
	public void doIslands(){
		ocean.initIslands();
	}
}
