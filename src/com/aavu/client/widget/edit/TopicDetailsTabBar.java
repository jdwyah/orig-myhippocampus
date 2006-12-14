package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.ReferencePanel;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicDetailsTabBar extends TabPanel {

	public TopicDetailsTabBar(Topic topic,Manager manager){
		
		TopicViewAndEditWidget tvw = new TopicViewAndEditWidget(manager);
		tvw.load(topic);
		add(tvw,Manager.myConstants.entry());
	
		LinkDisplayWidget ldw = new LinkDisplayWidget(topic);
		add(ldw,Manager.myConstants.occurrencesN(ldw.getSize()));
				
		SeeAlsoDisplayWidget sdw = new SeeAlsoDisplayWidget(topic);
		add(sdw,Manager.myConstants.seeAlsosN(sdw.getSize()));		
		
		UploadBoard ub = new UploadBoard(manager,topic);		
		add(ub,Manager.myConstants.filesN(ub.getSize()));		
		
		MindMapBoard mindMapBoard = new MindMapBoard(manager,topic);
		add(mindMapBoard,Manager.myConstants.mapperTitle());
		
		ReferencePanel referencesPanel = new ReferencePanel(manager,topic);
		add(referencesPanel,Manager.myConstants.references());
		referencesPanel.load();
	}
	
}
