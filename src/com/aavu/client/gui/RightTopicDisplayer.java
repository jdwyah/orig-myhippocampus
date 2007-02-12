package com.aavu.client.gui;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.OnThisIslandBoard;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TagBoard;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RightTopicDisplayer extends Composite implements SaveNeededListener, ClickListener {
	
	private TagPropertyPanel tagProperties;
	private TopicDetailsTabBar topicDetails;
	
	private EntryPreview entryPreview;
	
	private Topic topic;
	private Manager manager;
	private OnThisIslandBoard onThisIslandBoard;
	
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


	public void onChange(Widget sender) {
		// TODO Auto-generated method stub
		
	}

	
	
}
