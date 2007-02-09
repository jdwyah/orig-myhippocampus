package com.aavu.client.gui.timeline;

import org.gwm.client.GInternalFrame;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.TabPanel;

public class TimeLineWindow extends PopupWindow {

	
	private static final int WIDTH = 680;
	private static final int HEIGHT = 400;
	
	public TimeLineWindow(HippoTimeLine h, GInternalFrame frame) {
		
		super(frame,Manager.myConstants.timeline(),WIDTH,HEIGHT);
			
		setContent(h);
		
	}

}