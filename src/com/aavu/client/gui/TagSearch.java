package com.aavu.client.gui;

import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.CompleteListener;
import com.aavu.client.widget.edit.TagAutoCompleteBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TagSearch extends SimplePanel implements CompleteListener {

	private Manager manager;

	public TagSearch(Manager manager){
		this.manager = manager;
		VerticalPanel mainPanel = new VerticalPanel();
				
		TagAutoCompleteBox tagAuto = new TagAutoCompleteBox(this,manager.getTagCache());
		
		mainPanel.add(new Label(manager.myConstants.yourTags()));
		mainPanel.add(tagAuto);
		
		add(mainPanel);		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-TagSearch");
				
	}

	public void completed(String completeText) {
		manager.showTopicsForTag(completeText);
	}
}
