package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;

public class TopicWindow extends PopupWindow {

	public static final int WIDTH = 800;
	public static final int HEIGHT = 500;
	
	private TopicViewAndEditWidget widg;
	
	public TopicWindow(Manager manager,Topic t, GInternalFrame frame) {
		super(frame,t.getTitle(),WIDTH,HEIGHT);
		
		widg = new TopicViewAndEditWidget(this,manager);
		widg.load(t);
				
		frame.setContent(widg);
		
		
	}
	public void setToEdit(){
		widg.activateEditView();
	}
	
	
}
