package com.aavu.client.gui;

import java.util.Map;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Dashboard extends SimplePanel {


	private Manager manager;

	private HorizontalPanel mainPanel;

	// 'A' -> a's
	// 'B-C' -> b's & c's
	//<String,List<Topic>>
	Map sidebarEntries = new GWTSortedMap();	


	public Dashboard(Manager _manager){

		this.manager = _manager;

		mainPanel = new HorizontalPanel();

		Button tagButton = new Button("Tags");
		tagButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.showTagBoard();
			}});

		Button addNewButton = new Button("New Topic");
		addNewButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.newTopic();
			}});

//		Button initIslands = new Button("Init Islands");
//		initIslands.addClickListener(new ClickListener(){
//
//			public void onClick(Widget sender) {
//				manager.doIslands();
//			}});

		Button timeLine = new Button("TimeLine");
		timeLine.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				manager.showTimeline();
			}});

		
		mainPanel.add(addNewButton);
		mainPanel.add(tagButton);
		//mainPanel.add(initIslands);
		mainPanel.add(timeLine);
		
		

		add(mainPanel);
		//sets
		addStyleName("H-AbsolutePanel");
		addStyleName("H-Dashboard");		
	}


}
