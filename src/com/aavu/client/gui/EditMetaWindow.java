package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameAdapter;
import org.gwm.client.event.GFrameEvent;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class EditMetaWindow extends PopupWindow {

	public static final int WIDTH = 750;
	private static final int HEIGHT = 500;
	private Manager manager;
	
	private HorizontalPanel mainP;
	
	

	public EditMetaWindow(TopicCache topicCache, GInternalFrame frame, final CloseListener listener) {	
		super(frame,ConstHolder.myConstants.meta_title(),WIDTH,HEIGHT);
		this.manager = manager;

	
		mainP = new HorizontalPanel();
		
		mainP.setVerticalAlignment(HorizontalPanel.ALIGN_TOP);
		
		
		
		setContent(mainP);
		
		
		frame.addFrameListener(new GFrameAdapter(){
			public void frameClosing(GFrameEvent evt) {
				listener.close();				
			}
		});
	}
	
	
}
