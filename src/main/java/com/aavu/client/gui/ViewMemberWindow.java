package com.aavu.client.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.gui.explorer.Explorer;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;

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
		this(ConstHolder.myConstants.explorer_tooltip(),null, null, manager, frame);
	}
	public ViewMemberWindow(Topic myTag, List topics, Manager manager, GInternalFrame frame) {
		this(myTag.getTitle(),myTag, topics, manager, frame);
	}
	public ViewMemberWindow(String windowTitle,Topic myTag, List topics, Manager manager, GInternalFrame frame) {
		super(frame,windowTitle,WIDTH,HEIGHT);		
		
		
		Explorer explorer;
		if(myTag != null){
			Map defMap = new HashMap();						
			defMap.put(myTag.getIdentifier(), topics);
			explorer = new Explorer(defMap,manager,WIDTH,HEIGHT,this);
		}else{
			explorer = new Explorer(manager,WIDTH,HEIGHT,this);
		}
		
		setContent(explorer);
		
	
	}

}
