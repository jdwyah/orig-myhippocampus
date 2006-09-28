package com.aavu.client.gui.timeline;

import java.util.List;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.timeline.SimileTimeline;
import com.aavu.client.service.Manager;

public class HippoTimeLine extends PopupWindow {

	private SimileTimeline timeline;
	
	/**
	 * 
	 * @param timeLinesObjs
	 */
	public HippoTimeLine(Manager manager,List timeLinesObjs){
		super(manager.myConstants.helloWorld());
		
		timeline = new SimileTimeline("foo");
		
		setContentPanel(timeline);
				
	}
	
}
