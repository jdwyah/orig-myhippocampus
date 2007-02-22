package com.aavu.client.gui;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetPicker;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RightTopicDisplayer extends Composite {
	
	private List gadgets;
	
	
//	private TagPropertyPanel tagProperties;
//	private TopicDetailsTabBar topicDetails;	
//	private EntryPreview entryPreview;
//	private OnThisIslandBoard onThisIslandBoard;
	
	
	private Topic topic;
	private Manager manager;


	private VerticalPanel gadgetPanel;


	private GadgetPicker gadgetPicker;
	
	
	public RightTopicDisplayer(final Manager manager){
			
		this.manager = manager;
		
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		gadgetPanel = new VerticalPanel();
		gadgetPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);		
		
		gadgetPicker = new GadgetPicker();
		
		mainPanel.add(gadgetPicker);
		mainPanel.add(gadgetPanel);
		
		initWidget(mainPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-RightInfo");		
		
		setVisible(false);
	}


	public void load(Topic topic) {
		
		this.topic = topic;
		
		gadgetPanel.clear();
		
				
		gadgets = topic.getGadgets(manager);
		
		for (Iterator iter = gadgets.iterator(); iter.hasNext();) {
			Gadget gadget = (Gadget) iter.next();
			
			gadget.load(topic);
			
			gadgetPanel.add(gadget);			
		}
		
		setVisible(true);
		
//		if(topic instanceof Tag){
//			
//			tagProperties.load((Tag) topic);			
//			onThisIslandBoard.load((Tag) topic);			
//			
//			tagProperties.setVisible(true);
//			onThisIslandBoard.setVisible(true);
//			
//		}else{
//			tagProperties.setVisible(false);
//			onThisIslandBoard.setVisible(false);
//		}
//		
//		topicDetails.load(topic);
//		
//		entryPreview.load(topic);
		
	}

	public void unload() {
		setVisible(false);
	}



//	public void onClick(Widget sender) {
//		if(sender == entryPreview){
//			manager.editEntry(topic);
//		}
//	}
}
