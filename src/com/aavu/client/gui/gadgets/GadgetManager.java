package com.aavu.client.gui.gadgets;

import java.util.ArrayList;
import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.GadgetDisplayer;
import com.aavu.client.service.Manager;

public class GadgetManager {
	private Manager manager;
	private GadgetDisplayer displayer;
	
	private List allGadgets;
	private UploadBoard uploadBoard;
	private LinkDisplayWidget linkDisplayW;
	private Connections connectionBoard;
	private EntryPreview entryPreview;
	private TagPropertyPanel tagProperties;
	
	
	public GadgetManager(Manager manager){
		this.manager = manager;
	}

	public List getFullGadgetList() {
		
		allGadgets = new ArrayList();		
		
		tagProperties = new TagPropertyPanel(manager);					
		entryPreview = new EntryPreview(manager);		
		connectionBoard = new Connections(manager);
		linkDisplayW = new LinkDisplayWidget(manager);	
		uploadBoard = new UploadBoard(manager);
		
		
		allGadgets.add(tagProperties);	
		allGadgets.add(uploadBoard);
		allGadgets.add(linkDisplayW);
		allGadgets.add(connectionBoard);		
		allGadgets.add(entryPreview);
		
		
		
		return allGadgets;		
	}

	public void show(Gadget gadget) {
		displayer.addGadget(gadget);
	}

	public void setGadgetDisplayer(GadgetDisplayer displayer) {
		this.displayer = displayer;
	}

	public void load(Topic topic) {
		List gadgetsToUse = new ArrayList();
		
		if(topic.hasConnections()){
			gadgetsToUse.add(connectionBoard);
		}
		if(topic.hasEntry()){
			gadgetsToUse.add(entryPreview);
		}
		if(topic.hasFiles()){
			gadgetsToUse.add(uploadBoard);
		}
		if(topic.hasTagProperties()){
			gadgetsToUse.add(tagProperties);
		}
		if(topic.hasWebLinks()){
			gadgetsToUse.add(linkDisplayW);
		}
		
		if(displayer != null){
			displayer.load(topic,gadgetsToUse);
		}
	}

}
