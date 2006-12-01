package com.aavu.client.gui;

import java.util.Map;

import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.gui.ext.ExternalPopup;
import com.aavu.client.gui.mapper.MapperWidget;
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

		Button tagButton = new Button(_manager.myConstants.yourTags());
		tagButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.showTagBoard();
			}});

		Button addNewButton = new Button(_manager.myConstants.topic_new());
		addNewButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.newTopic();
			}});

		Button addDeliciousTags = new Button("Add Delicious Tags");
		addDeliciousTags.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				DeliciousWidget widg = new DeliciousWidget(manager); 
				widg.setPopupPosition(200, 200);
				widg.show();
			}});

		Button timeLine = new Button("TimeLine");
		timeLine.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				manager.showTimeline();
			}});

		Button facebookB = new Button("FaceBook");
		facebookB.addClickListener(new ClickListener(){
//http://api.facebook.com/login.php?api_key=d1144ae411b79109d46c6d752cd4d222&popup=true
//http://www.myhippocampus.com/site/facebook.html?auth_token=5db53300a9cebdc59ead9ea882cb57df
			public void onClick(Widget sender) {				
				String login = "http://api.facebook.com/login.php?api_key=d1144ae411b79109d46c6d752cd4d222&popup=true";
				ExternalPopup ex = new ExternalPopup("Facebook",login,626,436);
			}});
		
		
		mainPanel.add(addNewButton);
		mainPanel.add(tagButton);		
		mainPanel.add(timeLine);
		mainPanel.add(addDeliciousTags);		
		
		//mainPanel.add(facebookB);
				

		add(mainPanel);
		//sets
		addStyleName("H-AbsolutePanel");
		addStyleName("H-Dashboard");		
	}


}
