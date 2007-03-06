package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;
import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.gui.gadgets.TagBoard;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.SimpleDateFormatGWT;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class TopicTagSuperWindow extends PopupWindow implements SaveNeededListener {

	protected static SimpleDateFormatGWT df;
	protected Topic topic;
	protected Manager manager;
	private SaveStopLight saveButton;

	private TagBoard tagBoard;
	private TopicDetailsTabBar topicDetails;
	private EditableLabelExtension titleBox;
	//private SubjectBoard subjectBoard;

	private TopicViewAndEditWidget topicViewAndEditW;
	
	
	public TopicTagSuperWindow(GInternalFrame frame, String title, int width, int height) {
		super(frame,title,width,height);		
	}
	protected void init(Topic topic, Manager manager) {
		this.topic = topic;
		this.manager = manager;
		tagBoard = new TagBoard(manager);
		
		VerticalPanel leftSide = getLeftPanel(topic);		
		VerticalPanel rightSide = getRightPanel(topic,manager);		
		
		DockPanel mainPanel = new DockPanel();
		mainPanel.addStyleName("H.IslandDetailDock");
		mainPanel.setSpacing(10);
		
		mainPanel.add(leftSide,DockPanel.CENTER);
		mainPanel.add(rightSide,DockPanel.EAST);
		
				
		setContent(mainPanel);
		
		load(topic);
	}
	
	private void load(Topic loadme) {
		topic = loadme;
		tagBoard.load(topic);
		
		topicViewAndEditW.load(topic);
		
		topicDetails.load(topic);
		
		titleBox.setText(topic.getTitle());
		setTitle(topic.getTitle());
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
	 * 
	 * Show Islands that the topic is on & metadata. 
	 * 
	 * Extras has Island Properties panel for tags;
	 * 
	 * @param manager 
	 * @param topic 
	 */
	protected VerticalPanel getRightPanel(Topic topic, Manager manager) {

		VerticalPanel rightPanel = new VerticalPanel();	
		rightPanel.setStyleName("H-TopicTagRight");
				
		int size = tagBoard.load(topic);		
		rightPanel.add(tagBoard);
				
		addRightExtras(rightPanel);		
		
		/*
		 * Island creation
		 */
		
		if(!(topic instanceof Tag)){
			Button islandButton = new Button(ConstHolder.myConstants.tag_upgradeS(topic.getTitle()));
			islandButton.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					makeThisAnIsland();
				}});				
			rightPanel.add(islandButton);
		}
		
	
		topicDetails = new TopicDetailsTabBar(manager);
		
		rightPanel.add(topicDetails);
		
		return rightPanel;
	}	

	private void makeThisAnIsland(){
		manager.getTagCache().makeMeATag(topic,new StdAsyncCallback(ConstHolder.myConstants.tag_upgradeAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Tag tag = (Tag) result;
				manager.growIsland(tag);
				load(tag);
			}
		});
	}
	
	protected VerticalPanel getLeftPanel(final Topic topic) {
				
		saveButton = new SaveStopLight(new ClickListener(){
			public void onClick(Widget sender) {
				save();	
			}			
		});
		
		HorizontalPanel leftTopPanel = new HorizontalPanel();
		leftTopPanel.setSpacing(5);
		
		titleBox = new EditableLabelExtension(topic.getTitle(),new ChangeListener(){
			public void onChange(Widget sender) {								
				manager.getTopicCache().save(topic,new SaveTitleCommand(topic, titleBox.getText()),
						new StdAsyncCallback(ConstHolder.myConstants.save()){});
			}			
		});
		
		leftTopPanel.clear();
		leftTopPanel.add(saveButton);
		leftTopPanel.add(new Label(ConstHolder.myConstants.title()));
		leftTopPanel.add(titleBox);
		leftTopPanel.add(new Label(ConstHolder.myConstants.topic_updated()+formatDate(topic.getLastUpdated())));

		ImageButton delete = new ImageButton(ConstHolder.myConstants.delete_image(),20,20);
		delete.addMouseListener(new TooltipListener(ConstHolder.myConstants.delete()));
		delete.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				delete();
			}});
		leftTopPanel.add(delete);
				
		
		//Extension point
		addLeftTopExtras(leftTopPanel);		
		
		VerticalPanel leftPanel = new VerticalPanel();
		
		leftPanel.add(leftTopPanel);
		
		//Extension point
		addLeftExtras(leftPanel);
		
		
		/*
		 * Editor
		 * 
		 */
		topicViewAndEditW = new TopicViewAndEditWidget(manager,this);		
		leftPanel.add(topicViewAndEditW);
		
		
		return leftPanel;
	}

	
	protected String formatDate(java.util.Date date){		
		if(df == null){
			df = new SimpleDateFormatGWT("MM/dd/yy");
		}
		if(date != null){
			return df.format(date);
		}
		return null;
	}
	
	
	
	//implement ChangeListener
	/*
	 * Changes are assumed to be SaveNeeded events.
	 * 
	 */
	public void onChange(Widget w){
		if(w == titleBox){
			
			manager.getTopicCache().save(topic,new SaveTitleCommand(topic, titleBox.getText()),
					new StdAsyncCallback(ConstHolder.myConstants.save()){});
		}
		saveButton.setSaveNeeded();
	}
	
	public void save() {		
		manager.getTopicCache().save(topic,topicViewAndEditW.getSaveCommand(),
				new StdAsyncCallback(""){
					public void onSuccess(Object result) {					
						super.onSuccess(result);
						saveButton.saveAccomplished();
					}});		
	}	
	
	private void delete() {
		if(Window.confirm(ConstHolder.myConstants.delete_warningS(topic.getTitle()))){
			manager.delete(topic,new StdAsyncCallback(""){				
				public void onSuccess(Object result) {
					close();					
				}});
		}
	}
	
}
