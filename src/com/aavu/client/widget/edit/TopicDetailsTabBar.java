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

	private TopicViewAndEditWidget tvw;

	public TopicDetailsTabBar(Topic topic,Manager manager,SaveNeededListener saveNeeded){
		
		boolean selected = false;
		
		tvw = new TopicViewAndEditWidget(manager,saveNeeded);
		tvw.load(topic);
		add(tvw,Manager.myConstants.entry());
		if(!selected && !tvw.getEntry().isEmpty()){
			System.out.println("SELEC ENTRY");
			selectTab(getWidgetCount()-1);
			selected = true;
		}
		
		LinkDisplayWidget ldw = new LinkDisplayWidget(topic);
		add(ldw,Manager.myConstants.occurrencesN(ldw.getSize()));
		if(!selected && ldw.getSize() > 0){
			System.out.println("SELEC LINKS");
			selectTab(getWidgetCount()-1);
			selected = true;
		}
				
		SeeAlsoBoard sab = new SeeAlsoBoard(manager,saveNeeded);
		int size = sab.load(topic);		
		add(sab,Manager.myConstants.seeAlsosN(size));				
		if(!selected && size > 0){
			System.out.println("SELEC SEE ALSO");
			selectTab(getWidgetCount()-1);
			selected = true;
		}
		
		UploadBoard ub = new UploadBoard(manager,topic,saveNeeded);		
		add(ub,Manager.myConstants.filesN(ub.getSize()));		
		
		MindMapBoard mindMapBoard = new MindMapBoard(manager,topic,saveNeeded);
		add(mindMapBoard,Manager.myConstants.mapperTitle());
		
		ReferencePanel referencesPanel = new ReferencePanel(manager,topic);
		add(referencesPanel,Manager.myConstants.references());
		referencesPanel.load();
		
		
	}

	public String getEntryText() {
		return tvw.getEntryText();
	}
	
}
