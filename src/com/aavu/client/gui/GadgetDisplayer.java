package com.aavu.client.gui;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.GadgetPicker;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GadgetDisplayer extends Composite {
		
	private Manager manager;

	private VerticalPanel gadgetPanel;
	private GadgetPicker gadgetPicker;

	private Topic topic;
	
	
	public GadgetDisplayer(final Manager manager){
			
		this.manager = manager;
		
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		gadgetPanel = new VerticalPanel();
		gadgetPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);		
		
		gadgetPicker = new GadgetPicker(manager.getGadgetManager());
		manager.getGadgetManager().setGadgetDisplayer(this);
		
		mainPanel.add(gadgetPicker);
		mainPanel.add(gadgetPanel);
		
		initWidget(mainPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-RightInfo");		
		
		setVisible(false);
	}


	public void load(Topic topic, List gadgets) {
		
		this.topic = topic;
		
		gadgetPanel.clear();		
				
		
		for (Iterator iter = gadgets.iterator(); iter.hasNext();) {
			Gadget gadget = (Gadget) iter.next();
			
			gadget.load(topic);
			
			gadgetPanel.add(gadget);			
		}
		
		setVisible(true);
				
	}

	public void unload() {
		setVisible(false);
	}


	public void addGadget(Gadget gadget) {
		if(gadgetPanel.getWidgetIndex(gadget) == -1){
			gadget.load(topic);
			gadgetPanel.add(gadget);
		}else{		
			if(gadget.isVisible()){
				Window.alert(Manager.myConstants.gadget_already_showing());
			}
			else{
				//This will force the ConnectionBoard to unhide itself 
				gadget.setVisible(true);
			}
		}
	}



//	public void onClick(Widget sender) {
//		if(sender == entryPreview){
//			manager.editEntry(topic);
//		}
//	}
}
