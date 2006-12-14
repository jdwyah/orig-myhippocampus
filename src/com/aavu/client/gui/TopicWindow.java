package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;

public class TopicWindow extends TopicTagSuperWindow {

	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;
	
	private TopicViewAndEditWidget widg;
	
	public TopicWindow(Manager manager,Topic t) {
		super(manager.newFrame(),t.getTitle(),WIDTH,HEIGHT);
		
		init(t,manager);
		
//		widg = new TopicViewAndEditWidget(this,manager);
//		widg.load(t);
//				
//		setContent(widg);
		
		
	}
	public void setToEdit(){
		widg.activateEditView();
	}
	
	
}
