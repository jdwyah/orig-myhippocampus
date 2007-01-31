package com.aavu.client.gui.glossary;

import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.TopicPreviewLink;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.TopicLink;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GlossaryPage extends Composite {
	
	private SimplePanel previewPanel;
	private VerticalPanel listPanel;
	private Manager manager; 
	
	public GlossaryPage(Manager manager){
		this.manager = manager;
		
		listPanel = new VerticalPanel();
		listPanel.setVerticalAlignment(VerticalPanel.ALIGN_TOP);
		listPanel.setWidth("200px");
		
		previewPanel = new SimplePanel();
		
		
		HorizontalPanel mainP = new HorizontalPanel();
		
		mainP.add(listPanel);
		mainP.add(previewPanel);
		mainP.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		initWidget(mainP);
		
	}

	public void add(TopicIdentifier topic, int max_link_chars) {
		TopicPreviewLink link = new TopicPreviewLink(topic,max_link_chars,null,previewPanel,manager);
		listPanel.add(link);
	}

	public void add(Label label) {
		listPanel.add(label);
	}

}
