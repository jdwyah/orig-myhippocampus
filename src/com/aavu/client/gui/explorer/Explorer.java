package com.aavu.client.gui.explorer;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.HippoTimeLine;
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
	private Button azB;
	private Button timeB;
	private Manager manager;
	
	
	
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
	public Explorer(Topic myTag, FullTopicIdentifier[] topics, Manager manager, int width, int height, CloseListener close) {
		
		this.manager = manager;
		
		CellPanel mainP = new HorizontalPanel();				
		
		CellPanel optionsPanel = new VerticalPanel();
		
		currentView = new SimplePanel();
		
		
		glossary = new Glossary(manager,Orientation.HORIZONTAL);
		glossary.load(topics);			
				
		
		timeline = new TimeLineWrapper(manager,width,height,topics,close);
				
		timeB = new Button("Timeline");
		timeB.addClickListener(this);
		
		azB = new Button("A-Z");
		azB.addClickListener(this);		
		
		optionsPanel.add(timeB);
		optionsPanel.add(azB);
		
		mainP.add(optionsPanel);
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
		}
	}
	
	
}
