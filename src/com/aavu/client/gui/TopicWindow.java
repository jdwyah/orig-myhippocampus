package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;

public class TopicWindow extends TopicTagSuperWindow {

	public static final int WIDTH = 700;
	public static final int HEIGHT = 500;
		
	public TopicWindow(Manager manager,Topic t) {
		super(manager.newFrame(),t.getTitle(),WIDTH,HEIGHT);
		
		init(t,manager);
				
	}	
	
	
}
