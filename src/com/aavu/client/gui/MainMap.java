package com.aavu.client.gui;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.dhtmlIslands.OceanDHTMLImpl;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.ext.FlashContainer;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.HippoCache;
import com.aavu.client.widget.edit.TagBoard;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainMap extends Composite {

	//private Sidebar sideBar;	
	private Manager manager;
	//private TagSearch tagSearch;	
	private Ocean ocean;
	private StatusPanel statusPanel;
	private CompassRose compassRose;
	private RightTopicDisplayer topicDetailsDisplayer;
	
	private Zoomer zoomer;
	
	public MainMap(Manager manager){
		this.manager = manager;
		manager.setMap(this);
		
		MultiDivPanel mainP = new MultiDivPanel();	

		//sideBar = new Sidebar(manager);
		
		//TODO cleanout remove TagSearch class, css deadwood
		//tagSearch = new TagSearch(manager);
		
		
		
		//ocean = new OceanFlashImpl(manager);
		ocean = new OceanDHTMLImpl(manager);
		
		statusPanel = new StatusPanel();
		
		compassRose = new CompassRose(manager);
		mainP.add(compassRose);
		mainP.add(ocean.getWidget());
				
		//mainP.add(sideBar);
		mainP.add(new Dashboard(manager));
		
		mainP.add(statusPanel);
		
		zoomer = new Zoomer(manager);
		mainP.add(zoomer);
		
		topicDetailsDisplayer = new RightTopicDisplayer(manager);
		
		mainP.add(topicDetailsDisplayer);
		
		//mainP.add(tagSearch);
		
		//mainP.addStyleName("");
		initWidget(mainP);
	}
	
//	public Widget getOcean() {
//		return ocean.getWidget();
//	}
	
	/**
	 * Do things that require a login / data
	 *
	 */
	public void load(){
		//sideBar.load();
		ocean.load();
	}

	public void growIsland(Tag tag) {
		ocean.growIsland(tag);
	}

	public void removeIsland(long id) {
		ocean.removeIsland(id);	
	}
	public void refreshIslands(){
		//TODO
	}

	public void updateSidebar() {
		//sideBar.load();
	}
	public void updateSidebar(Topic t) {
		//sideBar.load(t);
	}
	
	//TODO shouldn't need null checks, but we do.
	public void updateStatusWindow(int id, String string, StatusCode statusCode) {
		if(statusPanel != null){
			statusPanel.update(id,string,statusCode);
		}
	}

	public void showBackToOcean(boolean focussed) {
		compassRose.showBackToOcean(focussed);
	}

	public void unFocus() {
		ocean.unFocus();
	}

	public void showCloseup(long id, FullTopicIdentifier[] topics) {
		ocean.showCloseup(id,topics);
	}

	
	
	
	public void displayTopic(Topic topic) {
		
		topicDetailsDisplayer.load(topic);		
		
	}

	public void unselect() {
		topicDetailsDisplayer.unload();
	}

	public void zoomTo(double scale) {		
		ocean.zoomTo(scale);
		zoomer.setToScale(scale);
	}



}
