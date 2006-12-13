package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;

public class TopicDetailsTabBar extends TabPanel {

	public TopicDetailsTabBar(Topic topic,Manager manager){
		
	
		
		
		LinkDisplayWidget ldw = new LinkDisplayWidget(topic);
		add(ldw,manager.myConstants.occurrencesN(ldw.getSize()));
		
		SeeAlsoDisplayWidget sdw = new SeeAlsoDisplayWidget(topic);
		add(sdw,manager.myConstants.seeAlsosN(sdw.getSize()));
		
		
		add(new Label("entries!!!"),"Entries(1)");
		add(new Label("References!!!!!!"),"References(0)");
		
		
	}
	
}
