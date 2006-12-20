package com.aavu.client.widget.edit;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.UpdateableTabPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.ReferencePanel;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicDetailsTabBar extends UpdateableTabPanel {

	private TopicViewAndEditWidget topicViewAndEditW;
	private SeeAlsoBoard seeAlsoBoard;
	private UploadBoard uploadBoard;
	private MindMapBoard mindMapBoard;
	private ReferencePanel referencesPanel;
	private LinkDisplayWidget linkDisplayW;

	public TopicDetailsTabBar(Manager manager,SaveNeededListener saveNeeded){
		
		
		topicViewAndEditW = new TopicViewAndEditWidget(manager,saveNeeded);		
		add(topicViewAndEditW,Manager.myConstants.entry());
				
		linkDisplayW = new LinkDisplayWidget(saveNeeded);
		add(linkDisplayW,Manager.myConstants.occurrencesN(linkDisplayW.getSize()));		
				
		seeAlsoBoard = new SeeAlsoBoard(manager,saveNeeded);		
		add(seeAlsoBoard,Manager.myConstants.seeAlsosN(0));				
		
		
		uploadBoard = new UploadBoard(manager,saveNeeded);		
		add(uploadBoard,Manager.myConstants.filesN(0));		
		
		mindMapBoard = new MindMapBoard(manager,saveNeeded);
		add(mindMapBoard,Manager.myConstants.mapperTitle());
		
		referencesPanel = new ReferencePanel(manager);
		add(referencesPanel,Manager.myConstants.references());		
		
	}

	public void load(Topic topic) {
		
		boolean selected = false;
				
		topicViewAndEditW.load(topic);
		if(!selected && !topicViewAndEditW.getEntry().isEmpty()){
			System.out.println("SELEC ENTRY");
			selectTab(getWidgetCount()-1);
			selected = true;
		}
		
		linkDisplayW.load(topic);
		updateTitle(linkDisplayW,Manager.myConstants.occurrencesN(linkDisplayW.getSize()));
		if(!selected && linkDisplayW.getSize() > 0){
			System.out.println("SELEC LINKS");
			selectTab(getWidgetCount()-1);
			selected = true;
		}
		
		int size = seeAlsoBoard.load(topic);		
		updateTitle(seeAlsoBoard, Manager.myConstants.seeAlsosN(size));
		if(!selected && size > 0){
			System.out.println("SELEC SEE ALSO");
			selectTab(getWidgetCount()-1);
			selected = true;
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
