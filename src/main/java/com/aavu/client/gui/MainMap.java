package com.aavu.client.gui;

import java.util.Iterator;

import org.gwm.client.GFrame;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.gui.dhtmlIslands.OceanDHTMLImpl;
import com.aavu.client.gui.ext.MultiDivPanel;
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
		
	private Ocean ocean;
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
		
		
		//ocean = new OceanFlashImpl(manager);
		ocean = new OceanDHTMLImpl(manager);
		

		
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
		mainP.add(ocean.getWidget());//,0,0);
		
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
	public void load(LoadFinishedListener loadFinished){
		//sideBar.load();
		dashboard.load();
		ocean.load(loadFinished);		
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


	public void update(Topic t, AbstractCommand command) {

		System.out.println("MainMap update "+t+" "+command);
		
		if(t instanceof Tag){
			System.out.println("is tag "+command);
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
	public void clearForLoading() {
		centerDisplayer.clearForLoading();
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
	
	

	public void zoomIn() {
		ocean.zoomIn();
	}

	public void zoomOut() {
		ocean.zoomOut();
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
	 * This manifested itself as a problem when TopBar does some logic  in setMovingGuard() 
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
