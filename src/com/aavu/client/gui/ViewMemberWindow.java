package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ViewMemberWindow extends PopupWindow implements ClickListener {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
	private Manager manager;
	private SimplePanel currentView;
	private HippoTimeLine timeline;
	private Glossary glossary;
	private Button azB;
	private Button timeB;
	
	
	
	/**
	 * NOTE: you can't put a timeline in a TabPanel. Weird JS problem from the timeline.js
	 * 
	 * @param myTag
	 * @param topics
	 * @param manager
	 * @param frame
	 */
	public ViewMemberWindow(Topic myTag, FullTopicIdentifier[] topics, Manager manager, GInternalFrame frame) {
		super(frame,myTag.getTitle(),WIDTH,HEIGHT);
		this.manager = manager;

		
		CellPanel mainP = new HorizontalPanel();		
		
		
		CellPanel optionsPanel = new VerticalPanel();
		
		currentView = new SimplePanel();
		
		
		glossary = new Glossary(manager,Orientation.HORIZONTAL);
		glossary.load(topics);			
				
		
		TimeLineObj[] timelines = new TimeLineObj[topics.length];
		for (int i = 0; i < topics.length; i++) {
			TimeLineObj tobj = new TimeLineObj(topics[i],topics[i].getLastUpdated(),null);	
			timelines[i] = tobj;
		}		
		timeline = new HippoTimeLine(manager,timelines,HEIGHT,WIDTH - 30);
				
		timeB = new Button("Timeline");
		timeB.addClickListener(this);
		
		azB = new Button("A-Z");
		azB.addClickListener(this);		
		
		optionsPanel.add(timeB);
		optionsPanel.add(azB);
		
		mainP.add(optionsPanel);
		mainP.add(currentView);		
		
		setContent(mainP);
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
