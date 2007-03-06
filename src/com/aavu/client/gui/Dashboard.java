package com.aavu.client.gui;

import java.util.Map;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.HippoTest;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.gui.ext.ExternalPopup;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.gui.mapper.MapperWidget;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class Dashboard extends SimplePanel {

	public static final int NEW_BUTTON_W = 60;
	public static final int NEW_BUTTON_H = 46;


	private Manager manager;

	private HorizontalPanel mainPanel;

	// 'A' -> a's
	// 'B-C' -> b's & c's
	//<String,List<Topic>>
	Map sidebarEntries = new GWTSortedMap();	


	public Dashboard(Manager _manager){

		this.manager = _manager;

		mainPanel = new HorizontalPanel();

//		Button tagButton = new Button(ConstHolder.myConstants.yourTags());
//		tagButton.addClickListener(new ClickListener(){
//			public void onClick(Widget sender) {
//				manager.showTagBoard();
//			}});

		ImageButton addNewButton = new ImageButton(ConstHolder.myConstants.topic_new_image(),NEW_BUTTON_W,NEW_BUTTON_H);
		addNewButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.newTopic();
			}});
		addNewButton.addMouseListener(new TooltipListener(0,-20,ConstHolder.myConstants.topic_new()));
		
		//ImageButton addNewIslandButton = new ImageButton(ConstHolder.myConstants.island_new_image(),60,41);
		ImageButton addNewIslandButton = new ImageButton(ConstHolder.myConstants.island_new_image(),45,45);
		addNewIslandButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.newIsland();
			}});
		addNewIslandButton.addMouseListener(new TooltipListener(0,-20,ConstHolder.myConstants.island_new()));

		Button addDeliciousTags = new Button("Add Delicious Tags");
		addDeliciousTags.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				DeliciousWidget widg = new DeliciousWidget(manager); 				
			}});

		ImageButton timeLine = new ImageButton(ConstHolder.myConstants.timeline_image(),40,60);
		timeLine.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				manager.showTimeline();
			}});
		timeLine.addMouseListener(new TooltipListener(0,-20,ConstHolder.myConstants.timeline()));
		
		Button facebookB = new Button("FaceBook");
		facebookB.addClickListener(new ClickListener(){
//http://api.facebook.com/login.php?api_key=d1144ae411b79109d46c6d752cd4d222&popup=true
//http://www.myhippocampus.com/site/facebook.html?auth_token=5db53300a9cebdc59ead9ea882cb57df
			public void onClick(Widget sender) {				
				String login = "http://api.facebook.com/login.php?api_key=d1144ae411b79109d46c6d752cd4d222&popup=true";
				ExternalPopup ex = new ExternalPopup("Facebook",login,626,436);
			}});
				
		Button logoutB = new Button("Logout");
		logoutB.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				Window.open(HippoTest.getRelativeURL("/site/j_acegi_logout"),"logout","");						
			}});
		
		ImageButton glossaryButton = new ImageButton(ConstHolder.myConstants.glossary_image(),64,45);
		glossaryButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.showGlossary();
			}});
		glossaryButton.addMouseListener(new TooltipListener(0,-20,ConstHolder.myConstants.glossary_tooltip()));
		
		
		
		mainPanel.add(addNewButton);
		mainPanel.add(addNewIslandButton);
		
//		mainPanel.add(tagButton);		
//		mainPanel.add(timeLine);
//		mainPanel.add(addDeliciousTags);		
		
		mainPanel.add(glossaryButton);
		
		mainPanel.add(logoutB);
		
		
		//mainPanel.add(facebookB);
				

		add(mainPanel);
		//sets
		addStyleName("H-AbsolutePanel");
		addStyleName("H-Dashboard");		
	}


}
