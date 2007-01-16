package com.aavu.client.gui;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.TopicLink;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class IslandDetailsWindow extends TopicTagSuperWindow {

	public static final int WIDTH = 900;
	public static final int HEIGHT = 500;	

	private TopicIdentifier[] topics;


	public IslandDetailsWindow(final Tag tag, TopicIdentifier[] topics,final Manager manager) {
		
		super(manager.newFrame(),manager.myConstants.tagContentsTitle(tag.getTitle()),WIDTH,HEIGHT);
						
		this.topics = topics;
		
		init(tag,manager);

	}

	

	//@Override
	protected void addLeftTopExtras(CellPanel panel) {
		//TODO where should this image x/y really be?
		ImageButton addNewButton = new ImageButton(manager.myConstants.topic_new_image(),Dashboard.NEW_BUTTON_W/2,Dashboard.NEW_BUTTON_H/2);		
		addNewButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				Topic t = new Topic(topic.getUser(),manager.myConstants.topic_new_title());
				t.tagTopic(topic);
				manager.bringUpChart(t);
			}});
		addNewButton.addMouseListener(new TooltipListener(manager.myConstants.topic_new_on_island()));		
		
		panel.add(addNewButton);
	}
	
	/**
	 * LeftSide 
	 */
	//@Override
	protected void addLeftExtras(CellPanel panel) {
			
		VerticalPanel leftSideExtra = new VerticalPanel();		
		leftSideExtra.setStyleName("H-IslandTopicList");
		
		leftSideExtra.add(new HeaderLabel(manager.myConstants.island_topics_on()));
				
		if(topics != null){
			for (int i = 0; i < topics.length; i++) {
				TopicIdentifier topic = topics[i];
				
				leftSideExtra.add(new TopicLink(topic,this));
				//leftSideExtra.add(new TopicPreviewLink(manager,topic,previewPop));
			}
		}
		panel.add(leftSideExtra);
	}
	
	//@Override
	protected void addRightExtras(CellPanel panel) {
		VerticalPanel rightSideExtras = new VerticalPanel();		
		rightSideExtras.add(new HeaderLabel(Manager.myConstants.island_property(),Manager.myConstants.island_property_help()));


		rightSideExtras.add(new TagPropertyPanel(manager, (Tag) topic));
		panel.add(rightSideExtras);
	}


	
}
