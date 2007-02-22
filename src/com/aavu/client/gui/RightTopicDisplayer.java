package com.aavu.client.gui;

import java.util.List;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.EntryPreview;
import com.aavu.client.gui.gadgets.TagPropertyPanel;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.OnThisIslandBoard;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RightTopicDisplayer extends Composite implements ClickListener {
	
	private List gadgets;
	
	
	private TagPropertyPanel tagProperties;
	private TopicDetailsTabBar topicDetails;	
	private EntryPreview entryPreview;
	private OnThisIslandBoard onThisIslandBoard;
	
	
	private Topic topic;
	private Manager manager;
	
	
	public RightTopicDisplayer(final Manager manager){
			
		this.manager = manager;
		
		tagProperties = new TagPropertyPanel(manager);
		
		onThisIslandBoard = new OnThisIslandBoard(manager);
		
		topicDetails = new TopicDetailsTabBar(manager);		
		
		entryPreview = new EntryPreview();
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		
		mainPanel.add(tagProperties);
		
		mainPanel.add(onThisIslandBoard);
		
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
			
		if(topic instanceof Tag){
			
			tagProperties.load((Tag) topic);			
			onThisIslandBoard.load((Tag) topic);			
			
			tagProperties.setVisible(true);
			onThisIslandBoard.setVisible(true);
			
		}else{
			tagProperties.setVisible(false);
			onThisIslandBoard.setVisible(false);
		}
		
		topicDetails.load(topic);
		
		entryPreview.load(topic);
		
	}

	public void unload() {
		setVisible(false);
	}



	public void onClick(Widget sender) {
		if(sender == entryPreview){
			manager.editEntry(topic);
		}
	}
}
