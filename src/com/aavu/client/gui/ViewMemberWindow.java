package com.aavu.client.gui;

import java.util.HashMap;
import java.util.Map;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.explorer.Explorer;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.tabbars.Orientation;
import com.aavu.client.gui.glossary.Glossary;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ViewMemberWindow extends PopupWindow {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;

	
	/**
	 * NOTE: you can't put a timeline in a TabPanel. Weird JS problem from the timeline.js
	 * 
	 * @param myTag
	 * @param topics
	 * @param manager
	 * @param frame
	 */
	public ViewMemberWindow(Manager manager, GInternalFrame frame) {
		this(ConstHolder.myConstants.glossary_tooltip(),null, null, manager, frame);
	}
	public ViewMemberWindow(Topic myTag, FullTopicIdentifier[] topics, Manager manager, GInternalFrame frame) {
		this(myTag.getTitle(),myTag, topics, manager, frame);
	}
	public ViewMemberWindow(String windowTitle,Topic myTag, FullTopicIdentifier[] topics, Manager manager, GInternalFrame frame) {
		super(frame,windowTitle,WIDTH,HEIGHT);		
		
		
		Explorer explorer;
		if(myTag != null){
			Map defMap = new HashMap();
			defMap.put(myTag, topics);
			explorer = new Explorer(defMap,manager,WIDTH,HEIGHT,this);
		}else{
			explorer = new Explorer(manager,WIDTH,HEIGHT,this);
		}
		
		setContent(explorer);
		
	
	}

}
