package com.aavu.client.gui.ocean;

import java.util.Iterator;

import org.gwm.client.GFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.gui.CenterTopicDisplayer;
import com.aavu.client.gui.Dashboard;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.gui.GadgetDisplayerBarImpl;
import com.aavu.client.gui.HippoDesktopPane;
import com.aavu.client.gui.LoadFinishedListener;
import com.aavu.client.gui.LocationSettingWidget;
import com.aavu.client.gui.SearchBox;
import com.aavu.client.gui.StatusCode;
import com.aavu.client.gui.StatusPanel;
import com.aavu.client.gui.Zoomer;
import com.aavu.client.gui.ext.MultiDivPanel;
import com.aavu.client.gui.hierarchy.HierarchyDisplay;
import com.aavu.client.gui.ocean.dhtmlIslands.OceanDHTMLImpl;
import com.aavu.client.service.MindscapeManager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class MainMap extends HippoDesktopPane {

	//private Sidebar sideBar;	
	private MindscapeManager manager;
	//private TagSearch tagSearch;
		
	private SpatialDisplay spatialDisplay;
	private StatusPanel statusPanel;
	private SearchBox searchBox;
	private GadgetDisplayer gadgetDisplayer;
	private CenterTopicDisplayer centerDisplayer;
	
	private Zoomer zoomer;
	
	private MultiDivPanel mainP;

	private Dashboard dashboard;

//	private List frames;
//	private GInternalFrame activeFrame;
//	private String theme;
	
	public MainMap(final MindscapeManager manager){
		super();
		this.manager = manager;
		
		//this.frames = new ArrayList();
		
		Window.enableScrolling(false);
		
		manager.setMap(this);
		
		mainP = new MultiDivPanel();	
		//mainP.setStyleName("H-AbsolutePanel");

		//sideBar = new Sidebar(manager);
		
		//TODO cleanout remove TagSearch class, css deadwood
		//tagSearch = new TagSearch(manager);
		
		
		//spatialDisplay = new OceanFlashImpl(manager);
		//spatialDisplay = new OceanDHTMLImpl(manager);
		spatialDisplay = new HierarchyDisplay(manager);
		

		
		statusPanel = new StatusPanel();
		
		Image questionHorse = ConstHolder.images.questionHorse().createImage();
		questionHorse.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.showHelp();
			}});
		questionHorse.addStyleName("H-AbsolutePanel");
		questionHorse.addStyleName("H-QuestionHorse");
		mainP.add(questionHorse);
		
		searchBox = new SearchBox(manager);
		mainP.add(searchBox);
		mainP.add(spatialDisplay.getWidget());//,0,0);
		
		//mainP.add(sideBar);
		dashboard = new Dashboard(manager);
		mainP.add(dashboard);
		
		mainP.add(statusPanel);
		
		zoomer = new Zoomer(manager);
		mainP.add(zoomer);		
		
		centerDisplayer = new CenterTopicDisplayer(manager);
		mainP.add(centerDisplayer);
		
		gadgetDisplayer = new GadgetDisplayerBarImpl(manager);
		
		mainP.add(gadgetDisplayer.getWidget());
		
		//mainP.add(tagSearch);
		
		
		//mainP.addStyleName("");
		initWidget(mainP);
		
	}
	
//	public Widget getOcean() {
//		return ocean.getWidget();
//	}
	
	/**
	 * Do things that require a login / data
	 * @param loadFinished 
	 *
	 */
	public void load(final LoadFinishedListener loadFinished){
		//sideBar.load();
			
		dashboard.load();
			
				
	}

	public void growIsland(Topic tag) {
		spatialDisplay.growIsland(tag);
	}

	public void removeIsland(long id) {
		spatialDisplay.removeIsland(id);	
	}
	public void refreshIslands(){
		//TODO
	}


	public void update(Topic t, AbstractCommand command) {

		System.out.println("MainMap update "+t+" "+command);
		
		
		System.out.println("MainMap.ocean.update "+command);
		spatialDisplay.update( t,command);
		
		/*
		 * affectedTag() is true if 
		 */
		//for(command.getAffectedTags())
		
		for (Iterator iter = command.getAffectedTopics().iterator(); iter.hasNext();) {
						
			Topic top = (Topic) iter.next();
			
			spatialDisplay.update(top, command);
			
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
		
		spatialDisplay.load(topic, null);
		centerDisplayer.load(topic);
		manager.getGadgetManager().load(topic);		
				
	}
	public void clearForLoading() {
		centerDisplayer.clearForLoading();
	}
	
	public boolean centerOn(Topic topic){
		return spatialDisplay.centerOn(topic);		
	}

	public void unselect() {
		centerDisplayer.unload();
		gadgetDisplayer.unload();
	}

	public void zoomTo(double scale) {		
		spatialDisplay.zoomTo(scale);
		zoomer.setToScale(scale);
	}

	public void ensureZoomOfAtLeast(double scale) {
		zoomer.setToScale(spatialDisplay.ensureZoomOfAtLeast(scale));
	}
	
	

	public void zoomIn() {
		spatialDisplay.zoomIn();
	}

	public void zoomOut() {
		spatialDisplay.zoomOut();
	}

	//@Override
	public void addButton(GFrame frame) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public LocationSettingWidget getFrame() {
		
		return mainP;
	}

	//@Override
	public void onWindowResized(int width, int height) {
		// TODO Auto-generated method stub		
	}

	//@Override
	public void removeButton(GFrame frame) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	/**
	 * Not sure what the problem is here (something about MultiDivPanel?) but super.getOffsetHeight()
	 * is totally off. It returns something like 0-180px when it should be 500px.
	 * 
	 * This manifested itself as a GWM problem when TopBar does some logic  in setMovingGuard() 
	 * to ensure that it hasn't 
	 * been dragged off the screen. It would freak out a bit and we'd be limitted to where 
	 * we could move the window.
	 * 
	 * Happily we want this to be the client height, so we're all set.
	 * 
	 */
	//@Override
	public int getOffsetHeight() {
		//int superH = super.getOffsetHeight();		
		return Window.getClientHeight();	
	}



}
