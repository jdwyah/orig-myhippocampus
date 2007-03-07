package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.gwm.client.GDesktopPane;
import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.impl.DefaultGFrame;
import org.gwm.client.impl.DefaultGInternalFrame;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractSaveCommand;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.gui.dhtmlIslands.OceanDHTMLImpl;
import com.aavu.client.gui.ext.DefaultGInternalFrameHippoExt;
import com.aavu.client.gui.ext.LocationSetter;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;

public class MainMap extends Composite implements GDesktopPane, LocationSetter {

	//private Sidebar sideBar;	
	private Manager manager;
	//private TagSearch tagSearch;
		
	private Ocean ocean;
	private StatusPanel statusPanel;
	private CompassRose compassRose;
	private GadgetDisplayer gadgetDisplayer;
	private CenterTopicDisplayer centerDisplayer;
	
	private Zoomer zoomer;
	private List frames;

	private MultiDivPanel mainP;
	
	public MainMap(Manager manager){
		this.manager = manager;
		
		this.frames = new ArrayList();
		
		Window.enableScrolling(false);
		
		manager.setMap(this);
		
		mainP = new MultiDivPanel();	
		//mainP.setStyleName("H-AbsolutePanel");

		//sideBar = new Sidebar(manager);
		
		//TODO cleanout remove TagSearch class, css deadwood
		//tagSearch = new TagSearch(manager);
		
		
		//ocean = new OceanFlashImpl(manager);
		ocean = new OceanDHTMLImpl(manager);
		

		
		statusPanel = new StatusPanel();
		
		compassRose = new CompassRose(manager);
		mainP.add(compassRose);
		mainP.add(ocean.getWidget());//,0,0);
		
		//mainP.add(sideBar);
		mainP.add(new Dashboard(manager));
		
		mainP.add(statusPanel);
		
		zoomer = new Zoomer(manager);
		mainP.add(zoomer);		
		
		centerDisplayer = new CenterTopicDisplayer(manager);
		mainP.add(centerDisplayer);
		
		gadgetDisplayer = new GadgetDisplayer(manager);
		
		mainP.add(gadgetDisplayer);
		
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


	public void update(Topic t, AbstractSaveCommand command) {

		System.out.println("MainMap update "+t+" "+command);
		
		if(t instanceof Tag){
			System.out.println("is tag ");
			ocean.update((Tag) t,command);
		}
		/*
		 * affectedTag() is true if 
		 */
		//for(command.getAffectedTags())
		
		for (Iterator iter = command.getAffectedTopics().iterator(); iter.hasNext();) {
						
			Topic top = (Topic) iter.next();
			if(top instanceof Tag){
				ocean.update((Tag) top, command);
			}
		}
		
//		if(command.affectedTag()){
//			ocean.update(command.getAffectedTag(), command);			
//		}
		
	}


	
	
	//TODO shouldn't need null checks, but we do.
	public void updateStatusWindow(int id, String string, StatusCode statusCode) {
		if(statusPanel != null){
			statusPanel.update(id,string,statusCode);
		}
	}

	
	public void displayTopic(Topic topic) {
		
		centerDisplayer.load(topic);
		manager.getGadgetManager().load(topic);		
				
	}
	
	public boolean centerOn(Topic topic){
		return ocean.centerOn(topic);		
	}

	public void unselect() {
		centerDisplayer.unload();
		gadgetDisplayer.unload();
	}

	public void zoomTo(double scale) {		
		ocean.zoomTo(scale);
		zoomer.setToScale(scale);
	}

	public void ensureZoomOfAtLeast(double scale) {
		zoomer.setToScale(ocean.ensureZoomOfAtLeast(scale));
	}
	
	
	
	public void addFrame(GInternalFrame internalFrame) {
		internalFrame.setDesktopPane(this);
		
		int spos = (frames.size() + 1) * 30;
		
		mainP.add((DefaultGFrame) internalFrame);		
		
		internalFrame.setLocation(mainP.getAbsoluteLeft() + spos,
				mainP.getAbsoluteTop() + spos);
		
		
		DOM.setStyleAttribute(((DefaultGInternalFrameHippoExt)internalFrame).getElement(), "position","absolute");
				
		frames.add(internalFrame);
	}

	public void closeAllFrames() {
		// TODO Auto-generated method stub		
	}
	public void iconify(GFrame internalFrame) {
		// TODO Auto-generated method stub		
	}
	public void deIconify(GFrame minimizedWindow) {
		// TODO Auto-generated method stub		
	}

	public List getAllFrames() {
		return frames;
	}

	public void setLocation(DefaultGInternalFrameHippoExt ext, int left, int top) {
		
		DOM.setStyleAttribute(ext.getElement(), "left", left+"px");
		DOM.setStyleAttribute(ext.getElement(), "top", top+"px");
		
	}


}
