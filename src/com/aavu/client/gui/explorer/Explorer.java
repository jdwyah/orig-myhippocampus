package com.aavu.client.gui.explorer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.blog.BlogView;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.LostNFound;
import com.aavu.client.gui.maps.BigMap;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Explorer extends Composite {

	
	private SimplePanel currentView;
	private TimeLineWrapper timeline;
	private ExplorerGlossary glossary;
	private BlogView blogView;
	private BigMap bigMap;
	private LostNFound lostNFound;
	
	private ExplorerB selectedB;
	
	private Manager manager;
	
	//<Tag,FullTopicIdentifier>
	
	private TagChooser tagChooser;
	
	//<TopicIdentifier>
	private Set tags = new HashSet();
	private boolean allmode;
	


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
		//this.tagToIdentifierMap = defaultMap;
		
		CellPanel mainP = new HorizontalPanel();				
		
		CellPanel optionsPanel = new VerticalPanel();
		
		currentView = new SimplePanel();		
		
		glossary = new ExplorerGlossary(manager,defaultMap,Orientation.HORIZONTAL);		
						
		timeline = new TimeLineWrapper(manager,defaultMap,width,height,close);
				
		blogView = new BlogView(manager,defaultMap);
		
		bigMap = new BigMap(manager);
		
		lostNFound = new LostNFound(manager);
		
		
		ExplorerB timeB = new ExplorerB(ConstHolder.myConstants.explorer_timeline(),timeline);		
		ExplorerB azB = new ExplorerB(ConstHolder.myConstants.explorer_atoz(),glossary);		
		ExplorerB recentB = new ExplorerB(ConstHolder.myConstants.explorer_recent(),blogView);		
		ExplorerB mapB = new ExplorerB(ConstHolder.myConstants.explorer_map(),bigMap);						
		ExplorerB lostNFoundB = new ExplorerB(ConstHolder.myConstants.explorer_lostFound(),lostNFound);		
		
		selectedB = recentB;
		
		optionsPanel.add(timeB);
		optionsPanel.add(azB);
		optionsPanel.add(recentB);
		optionsPanel.add(mapB);
		//optionsPanel.add(lostNFoundB);
		
		tagChooser = new TagChooser(defaultMap,manager,this);
		
		//carfeull, tags = defaultMap.keySet() makes tags a HashMap.KeySet obj and .add() is unsopported
		tags.addAll(defaultMap.keySet());
		
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

	//Set<TagStat>
	public void load(Set tagstats) {
		ExplorerPanel selectedPanel = selectedB.getExplorerP();
		
		allmode = false;
		
		System.out.println("explorer load "+tagstats.size());
		tags.clear();
		for (Iterator iter = tagstats.iterator(); iter.hasNext();) {
			TopicIdentifier tag = (TopicIdentifier) iter.next();
			System.out.println(" tag "+tag);
			System.out.println("tags "+tags);
		
			System.out.println(" type "+GWT.getTypeName(tags));
			tags.add(tag);
			
		}				
		
	}
	
	public void setAllMode(boolean allmode){
		this.allmode = allmode;
	}


	private class ExplorerB extends Button implements ClickListener{

		private ExplorerPanel explorerP;
		
		public ExplorerB(String label,ExplorerPanel w){
			super(label);
			addClickListener(this);
			addStyleName("H-ExplorerButton");
			this.explorerP = w;
		}


		public ExplorerPanel getExplorerP() {
			return explorerP;
		}


		public void onClick(Widget sender) {

			selectedB = (ExplorerB) sender;
			
			System.out.println("on click size "+tags.size());
			
			currentView.clear();
			if(allmode){
				explorerP.loadAll();
			}else{
				explorerP.load(tags);
			}
			currentView.add(explorerP.getWidget());		
		}
	}

}
