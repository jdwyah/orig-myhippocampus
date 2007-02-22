package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.UpdateableTabPanel;
import com.aavu.client.gui.gadgets.UploadBoard;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.AllReferencesPanel;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicDetailsTabBar extends UpdateableTabPanel {

	
	
	private UploadBoard uploadBoard;
	//private MindMapBoard mindMapBoard;
	private AllReferencesPanel referencesPanel;
	
	public TopicDetailsTabBar(Manager manager){		
	
		referencesPanel = new AllReferencesPanel(manager);
		add(referencesPanel,Manager.myConstants.references());		
		
		
		uploadBoard = new UploadBoard(manager);		
		add(uploadBoard,Manager.myConstants.filesN(0));		
		
//		mindMapBoard = new MindMapBoard(manager,saveNeeded);
//		add(mindMapBoard,Manager.myConstants.mapperTitle());
//		
		
		addStyleName("H-Gadget");
		addStyleName("H-TopicDetails");
	}

	public void load(Topic topic) {
				
		selectTab(getWidgetIndex(referencesPanel));

			
		uploadBoard.load(topic);
		updateTitle(uploadBoard,Manager.myConstants.filesN(uploadBoard.getSize()));		
		
		//mindMapBoard.load(topic);
		
		referencesPanel.load(topic,this);
		
	}


	
}
