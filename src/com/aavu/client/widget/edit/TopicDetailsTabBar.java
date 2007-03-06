package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.UpdateableTabPanel;
import com.aavu.client.gui.gadgets.UploadBoard;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.AllReferencesPanel;

public class TopicDetailsTabBar extends UpdateableTabPanel {

	
	
	private UploadBoard uploadBoard;
	//private MindMapBoard mindMapBoard;
	private AllReferencesPanel referencesPanel;
	
	public TopicDetailsTabBar(Manager manager){		
	
		referencesPanel = new AllReferencesPanel(manager);
		add(referencesPanel,ConstHolder.myConstants.references());		
		
		
		uploadBoard = new UploadBoard(manager);		
		add(uploadBoard,ConstHolder.myConstants.filesN(0));		
		
//		mindMapBoard = new MindMapBoard(manager,saveNeeded);
//		add(mindMapBoard,ConstHolder.myConstants.mapperTitle());
//		
		
		addStyleName("H-Gadget");
		addStyleName("H-TopicDetails");
	}

	public void load(Topic topic) {
				
		selectTab(getWidgetIndex(referencesPanel));

			
		uploadBoard.load(topic);
		updateTitle(uploadBoard,ConstHolder.myConstants.filesN(uploadBoard.getSize()));		
		
		//mindMapBoard.load(topic);
		
		referencesPanel.load(topic,this);
		
	}


	
}
