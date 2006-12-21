package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.UpdateableTabPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.AllReferencesPanel;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicDetailsTabBar extends UpdateableTabPanel {

	private TopicViewAndEditWidget topicViewAndEditW;
	
	private UploadBoard uploadBoard;
	private MindMapBoard mindMapBoard;
	private AllReferencesPanel referencesPanel;
	

	public TopicDetailsTabBar(Manager manager,SaveNeededListener saveNeeded){
		
		
		topicViewAndEditW = new TopicViewAndEditWidget(manager,saveNeeded);		
		add(topicViewAndEditW,Manager.myConstants.entry());
		
		referencesPanel = new AllReferencesPanel(manager,saveNeeded);
		add(referencesPanel,Manager.myConstants.references());		
		
		
		uploadBoard = new UploadBoard(manager,saveNeeded);		
		add(uploadBoard,Manager.myConstants.filesN(0));		
		
		mindMapBoard = new MindMapBoard(manager,saveNeeded);
		add(mindMapBoard,Manager.myConstants.mapperTitle());
		
		
	}

	public void load(Topic topic) {
		
		boolean selected = false;
				
		topicViewAndEditW.load(topic);
		if(!selected && !topicViewAndEditW.getEntry().isEmpty()){
			System.out.println("SELEC ENTRY");
			selectTab(getWidgetIndex(topicViewAndEditW));
			selected = true;
		}else{
			selectTab(getWidgetIndex(referencesPanel));
		}
			
		uploadBoard.load(topic);
		updateTitle(uploadBoard,Manager.myConstants.filesN(uploadBoard.getSize()));		
		
		mindMapBoard.load(topic);
		
		referencesPanel.load(topic,this);
		
	}

	public String getEntryText() {
		return topicViewAndEditW.getEntryText();
	}


	
}
