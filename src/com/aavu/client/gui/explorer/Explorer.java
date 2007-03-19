package com.aavu.client.gui.explorer;

import java.util.HashMap;
import java.util.Map;

import com.aavu.client.gui.blog.BlogView;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.gui.maps.BigMap;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Explorer extends Composite implements ClickListener {

	
	private SimplePanel currentView;
	private TimeLineWrapper timeline;
	private Glossary glossary;
	private BlogView blogView;
	private BigMap bigMap;
	
	private Button azB;
	private Button timeB;
	private Button recentB;
	private Button mapB;
	
	private Manager manager;
	
	//<Tag,FullTopicIdentifier>
	private Map tagToIdentifierMap;
	private TagChooser tagChooser;
	


	public Explorer(Manager manager, int width, int height, CloseListener close) {
		this(new HashMap(),manager,width,height,close);
	}

	
	/**
	 * NOTE: you can't put a timeline in a TabPanel. Weird JS problem from the timeline.js
	 * 
	 * @param myTag
	 * @param topics
	 * @param manager
	 * @param height 
	 * @param width 
	 * @param frame
	 */
	public Explorer(Map defaultMap, Manager manager, int width, int height, CloseListener close) {
		
		this.manager = manager;
		this.tagToIdentifierMap = defaultMap;
		
		CellPanel mainP = new HorizontalPanel();				
		
		CellPanel optionsPanel = new VerticalPanel();
		
		currentView = new SimplePanel();
		
		
		glossary = new Glossary(manager,Orientation.HORIZONTAL);
		
		glossary.load(tagToIdentifierMap);
						
		timeline = new TimeLineWrapper(manager,tagToIdentifierMap,width,height,close);
				
		blogView = new BlogView(manager,tagToIdentifierMap);
		
		bigMap = new BigMap(manager,tagToIdentifierMap);
		
		
		timeB = new Button("Timeline");
		timeB.addClickListener(this);
		
		azB = new Button("A-Z");
		azB.addClickListener(this);		
		
		recentB = new Button("Recent");
		recentB.addClickListener(this);		
		
		mapB = new Button("Map");
		mapB.addClickListener(this);
				
		optionsPanel.add(timeB);
		optionsPanel.add(azB);
		optionsPanel.add(recentB);
		optionsPanel.add(mapB);
		
		tagChooser = new TagChooser(defaultMap,manager);
		
		
		CellPanel leftPanel = new VerticalPanel();
		leftPanel.add(optionsPanel);
		
		leftPanel.add(tagChooser);
		
		mainP.add(leftPanel);
		mainP.add(currentView);		
		
		initWidget(mainP);
		
		//default to glossary
		currentView.clear();
		currentView.add(glossary);
	}



	public void onClick(Widget sender) {
		if(sender == timeB){
			currentView.clear();
			currentView.add(timeline);
		}else if(sender == azB){
			currentView.clear();
			currentView.add(glossary);			
		}else if(sender == recentB){
			currentView.clear();
			currentView.add(blogView);			
		}else if(sender == mapB){
			currentView.clear();
			currentView.add(bigMap);			
		}
	}
	
	
}
