package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.util.SimpleDateFormatGWT;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class TopicTagSuperWindow extends PopupWindow {

	private static SimpleDateFormatGWT df;


	public TopicTagSuperWindow(GInternalFrame frame, String title) {
		super(frame, title);		
	}
	public TopicTagSuperWindow(GInternalFrame frame, String title, int width, int height) {
		super(frame,title,width,height);		
	}
	protected void init(Topic topic, Manager manager) {
		
		VerticalPanel leftSide = getLeftPanel(topic);		
		VerticalPanel rightSide = getRightPanel(topic,manager);		
		
		DockPanel mainPanel = new DockPanel();
		mainPanel.addStyleName("H.IslandDetailDock");
		mainPanel.setSpacing(10);
		
		mainPanel.add(leftSide,DockPanel.CENTER);
		mainPanel.add(rightSide,DockPanel.EAST);
		
		
		/*
		 * TopicDetails on the bottom
		 * 
		 */
		TopicDetailsTabBar bottom = new TopicDetailsTabBar(topic,manager);
		
		mainPanel.add(bottom,DockPanel.SOUTH);
		
		setContent(mainPanel);
	}
	
	protected void addLeftExtras(CellPanel panel) {
		return;
	}
	protected void addLeftTopExtras(CellPanel panel) {
		return;
	}
	
	protected void addRightExtras(CellPanel panel) {
		return;
	}
	

	
	/**
	 * RightSide
	 * @param manager 
	 * @param topic 
	 */
	protected VerticalPanel getRightPanel(Topic topic, Manager manager) {

		VerticalPanel rightPanel = new VerticalPanel();	
		
		rightPanel.add(new Label("REGULAR RIGHT"));
		
		addRightExtras(rightPanel);		
		
		return rightPanel;
	}

	protected VerticalPanel getLeftPanel(Topic topic) {
		
		HorizontalPanel leftTopPanel = new HorizontalPanel();

		leftTopPanel.clear();
		leftTopPanel.add(new Label(Manager.myConstants.title()));
		leftTopPanel.add(new Label(topic.getTitle()));
		leftTopPanel.add(new Label(Manager.myConstants.topic_updated()+formatDate(topic.getLastUpdated())));

		//Extension point
		addLeftTopExtras(leftTopPanel);		
		
		VerticalPanel leftPanel = new VerticalPanel();
		
		leftPanel.add(leftTopPanel);

		//Extension point
		addLeftExtras(leftPanel);
		
		return leftPanel;
	}


	
	private String formatDate(java.util.Date date){		
		if(df == null){
			df = new SimpleDateFormatGWT("yyyy-MM-dd HH:mm:ss");
		}
		if(date != null){
			return df.format(date);
		}
		return null;
	}
	
}
