package com.aavu.client.gui;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TagBoard;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RightTopicDisplayer extends Composite implements SaveNeededListener, ClickListener {
	
	private TagBoard tagBoard;
	private TopicDetailsTabBar topicDetails;
	private EditableLabelExtension titleBox;
	
	private EntryPreview entryPreview;
	
	private Topic topic;
	private Manager manager;
	
	public RightTopicDisplayer(Manager manager){
			
		this.manager = manager;
		
		tagBoard = new TagBoard(manager,this);		
		
		topicDetails = new TopicDetailsTabBar(manager,this);		
		
		entryPreview = new EntryPreview();
		
		VerticalPanel mainPanel = new VerticalPanel();
		
		
		titleBox = new EditableLabelExtension("",this);
		
		mainPanel.add(titleBox);
		mainPanel.add(tagBoard);
		mainPanel.add(topicDetails);
		mainPanel.add(entryPreview);
		
		
		entryPreview.addClickListener(this);
		
		initWidget(mainPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-RightInfo");		
		
		setVisible(false);
	}


	public void load(Topic topic) {
		
		this.topic = topic;
		
		setVisible(true);
		
		titleBox.setText(topic.getTitle());
		tagBoard.load(topic);
		topicDetails.load(topic);
		
		entryPreview.load(topic);
		
	}

	public void unload() {
		setVisible(false);
	}


	/**
	 * saveNeeded
	 */
	public void onChange(Widget sender) {
		
	}


	public void onClick(Widget sender) {
		if(sender == entryPreview){
			manager.bringUpChart(topic);
		}
	}

	
	
}
