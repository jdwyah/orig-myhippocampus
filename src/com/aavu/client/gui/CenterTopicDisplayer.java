package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.TagBoard;
import com.aavu.client.gui.gadgets.TagPropertyPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.OnThisIslandBoard;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CenterTopicDisplayer extends Composite {
	
	private TagBoard tagBoard;
	private OnThisIslandBoard onThisIslandBoard;	
	private TagPropertyPanel tagProperties;
	
		
	private Topic topic;
	private Manager manager;
	private TitleGadget titleG;
	
	public CenterTopicDisplayer(final Manager manager){
			
		this.manager = manager;
		
		tagBoard = new TagBoard(manager);		
		onThisIslandBoard  = new OnThisIslandBoard(manager);
		tagProperties = new TagPropertyPanel(manager);
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		
		
	
		titleG = new TitleGadget(manager);
		
		mainPanel.add(titleG);
		
				
		mainPanel.add(tagBoard);
		mainPanel.add(onThisIslandBoard);
		mainPanel.add(tagProperties);
		
		initWidget(mainPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-CenterInfo");		
		
		setVisible(false);
	}


	public void load(Topic topic) {
		
		this.topic = topic;
		
		setVisible(true);
		
		titleG.load(topic);
				
		tagBoard.load(topic);
		onThisIslandBoard.load(topic);
		tagProperties.load(topic);
		
	}

	public void unload() {
		setVisible(false);
	}
	
	
}
