package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TagBoard;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RightTopicDisplayer extends Composite implements SaveNeededListener {
	
	private TagBoard tagBoard;
	private TopicDetailsTabBar topicDetails;
	private EditableLabelExtension titleBox;
	
	private Topic topic;
	private Manager manager;
	
	public RightTopicDisplayer(Manager manager){
			
		this.manager = manager;
		
		tagBoard = new TagBoard(manager,this);		
		
		topicDetails = new TopicDetailsTabBar(manager,this);		
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		
		titleBox = new EditableLabelExtension("",this);
		mainPanel.add(titleBox);
		mainPanel.add(tagBoard);
		mainPanel.add(topicDetails);
		
		
		
		initWidget(mainPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-RightInfo");		
		
		setVisible(false);
	}


	public void load(Topic topic) {
		setVisible(true);
		
		tagBoard.load(topic);
		topicDetails.load(topic);
		titleBox.setText(topic.getTitle());
		
	}

	public void unload() {
		setVisible(false);
	}


	/**
	 * saveNeeded
	 */
	public void onChange(Widget sender) {
		
	}

	
	
}
