package com.aavu.client.gui;

import java.util.Set;

import org.gwm.client.GInternalFrame;
import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.util.SimpleDateFormatGWT;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.SubjectBoard;
import com.aavu.client.widget.edit.TagBoard;
import com.aavu.client.widget.edit.TopicDetailsTabBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.StackPanel;
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
	private SubjectBoard subjectBoard;

	public TopicTagSuperWindow(GInternalFrame frame, String title, int width, int height) {
		super(frame,title,width,height);		
	}
	protected void init(Topic topic, Manager manager) {
		this.topic = topic;
		this.manager = manager;
		tagBoard = new TagBoard(manager,this);
		
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
		topicDetails = new TopicDetailsTabBar(manager,this);
		
		mainPanel.add(topicDetails,DockPanel.SOUTH);
				
		setContent(mainPanel);
		
		load(topic);
	}
	
	private void load(Topic loadme) {
		topic = loadme;
		tagBoard.load(topic);
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
			Button islandButton = new Button(manager.myConstants.tag_upgrade());
			islandButton.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					makeThisAnIsland();
				}});				
			rightPanel.add(islandButton);
		}
		
		return rightPanel;
	}	

	private void makeThisAnIsland(){
		manager.getTagCache().makeMeATag(topic,new StdAsyncCallback(Manager.myConstants.tag_upgradeAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Tag tag = (Tag) result;
				manager.growIsland(tag);
				load(tag);
			}
		});
	}
	
	protected VerticalPanel getLeftPanel(Topic topic) {
		
		
		saveButton = new SaveStopLight();
		
		HorizontalPanel leftTopPanel = new HorizontalPanel();
		leftTopPanel.setSpacing(5);
		
		titleBox = new EditableLabelExtension(topic.getTitle(),this);
		
		leftTopPanel.clear();
		leftTopPanel.add(saveButton);
		leftTopPanel.add(new Label(Manager.myConstants.title()));
		leftTopPanel.add(titleBox);
		leftTopPanel.add(new Label(Manager.myConstants.topic_updated()+formatDate(topic.getLastUpdated())));

		ImageButton delete = new ImageButton(Manager.myConstants.delete_image(),20,20);
		delete.addMouseListener(new TooltipListener(Manager.myConstants.delete()));
		delete.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				delete();
			}});
		leftTopPanel.add(delete);
				
		
		//Extension point
		addLeftTopExtras(leftTopPanel);		
		
		VerticalPanel leftPanel = new VerticalPanel();
		
		leftPanel.add(leftTopPanel);
		
		subjectBoard = new SubjectBoard(manager,titleBox,tagBoard,this);
		subjectBoard.load(topic);
		
		leftPanel.add(subjectBoard);

		//Extension point
		addLeftExtras(leftPanel);
		
		return leftPanel;
	}

	
	private String formatDate(java.util.Date date){		
		if(df == null){
			df = new SimpleDateFormatGWT("MM/dd/yy");
		}
		if(date != null){
			return df.format(date);
		}
		return null;
	}
	
	private class SaveStopLight extends Composite {
		private ImageButton redLight;
		private StackPanel sp;
		
		public SaveStopLight(){
			sp = new StackPanel();
			sp.removeStyleName("gwt-StackPanel");
			sp.add(new ImageButton(Manager.myConstants.save_greenLight(),30,30));
			
			redLight = new ImageButton(Manager.myConstants.save_redLight(),30,30);
			redLight.addClickListener(new ClickListener(){
				public void onClick(Widget sender) {
					System.out.println("SAVE initiated");
					save();					
				}});
			redLight.addMouseListener(new TooltipListener(Manager.myConstants.save()));
			sp.add(redLight);
			
			initWidget(sp);
		}
		public void setSaveNeeded(){
			sp.showStack(1);
		}
		public void saveAccomplished() {
			sp.showStack(0);
		}
	}
	
	
	
	//implement ChangeListener
	/*
	 * Changes are assumed to be SaveNeeded events.
	 * 
	 */
	public void onChange(Widget w){
		saveButton.setSaveNeeded();
	}
	
	public void save() {
		
		System.out.println("save() ");
		String entryText = topicDetails.getEntryText();
		System.out.println("Entry: "+entryText);
		if(entryText != null){
			topic.getLatestEntry().setData(entryText);
		}
		
		topic.setTitle(titleBox.getText());
		
		topic.setSubject(subjectBoard.getSelectedSubject());
				
		tagBoard.saveThingsNowEvent(new StdAsyncCallback("save things now"){
			public void onSuccess(Object result) {
				super.onSuccess(result);		
				Set otherTopicsToSave = (Set) result;
				save(topic,otherTopicsToSave);					
			}});
		
	}
	
	private void save(Topic topic2, Set otherTopicsToSave) {

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
		//TODO is this good or bad? a bit early.. 
		//load(topic2);

		manager.getTopicCache().save(topic2, otherTopicsToSave, new StdAsyncCallback("topicDetail save") {				

			public void onSuccess(Object result) {		
				super.onSuccess(result);
				System.out.println("????????????????????");
				//this should prevent double saves
				Topic[] saved = (Topic[]) result;

				load(saved[0]);
				
				saveButton.saveAccomplished();
			}
		});	
	}
	
	private void delete() {
		if(Window.confirm(Manager.myConstants.delete_warningS(topic.getTitle()))){
			manager.getTopicCache().delete(topic,new StdAsyncCallback(Manager.myConstants.delete_async()){
				public void onSuccess(Object result) {
					super.onSuccess(result);
					close();
					manager.refreshAll();
				}				
			});
		}
	}
	
}
