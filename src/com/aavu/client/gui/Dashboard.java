package com.aavu.client.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.collections.GWTSortedMap;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.VertableTabPanel;
import com.aavu.client.service.cache.HippoCache;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Dashboard extends SimplePanel {

	private MainMap map;
	private HippoCache hippoCache;

	private HorizontalPanel mainPanel;

	// 'A' -> a's
	// 'B-C' -> b's & c's
	//<String,List<Topic>>
	Map sidebarEntries = new GWTSortedMap();	


	public Dashboard(MainMap _map, HippoCache hippoCache){
		this.map = _map;
		this.hippoCache = hippoCache;
		
		mainPanel = new HorizontalPanel();

		Button tagButton = new Button("Tags");
		tagButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				map.showTagBoard();
			}});
		
		Button addNewButton = new Button("New Topic");
		addNewButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				map.newTopic();
			}});
		
		
		mainPanel.add(addNewButton);
		mainPanel.add(tagButton);
						
		add(mainPanel);
		//sets
		setStyleName("GuiTest-Dashboard");
	}


}
