package com.aavu.client.widget.edit;

import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicEditWidget extends Composite {
	
	private TextBox titleBox = new TextBox();
	private SpecialTextbox textArea = null;

	private TagBoard tagBoard;
	private SubjectBoard subjectBoard;
	
	private Topic topic;
	private TopicViewAndEditWidget topicViewAndEditWidget;
	
	private SeeAlsoBoard seeAlsoBoard;
	
	private UploadBoard uploadBoard;
	private MindMapBoard mindMapBoard;
	private Manager manager;
	private HorizontalPanel topPanel;
	
	public TopicEditWidget(TopicViewAndEditWidget topicViewAndEditWidget, final Manager manager, Topic _topic){
		this.topic = _topic;
		this.topicViewAndEditWidget = topicViewAndEditWidget;
		this.manager = manager;
		
		System.out.println("topic edit widg "+topic);
		
		textArea = new SpecialTextbox(manager.getTopicCache());
		
		
		tagBoard = new TagBoard(manager);
		subjectBoard = new SubjectBoard(manager,titleBox,tagBoard);
		seeAlsoBoard = new SeeAlsoBoard(manager);
		uploadBoard = new UploadBoard(manager,topic);
		
		mindMapBoard = new MindMapBoard(manager,topic,this);

		
		topPanel = new HorizontalPanel();
		topPanel.add(new HeaderLabel(manager.myConstants.title()));
		topPanel.add(titleBox);

		
		setupTopic();		
		
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(topPanel);
		
		panel.add(subjectBoard);
		panel.add(tagBoard);		
		panel.add(seeAlsoBoard);
		panel.add(uploadBoard);
		panel.add(mindMapBoard);
				
		panel.add(textArea);
	
	
		initWidget(panel);
	}
	
	
	
	private void setupTopic() {
		System.out.println("setupTopic");
		if(topic != null){
			titleBox.setText(topic.getTitle());		
			textArea.setText(topic.getLatestEntry().getData());
						
			subjectBoard.load(topic);
			tagBoard.load(topic);
			seeAlsoBoard.load(topic);
			
			
			
			/*
			 * Island creation
			 */
			if(topPanel.getWidgetCount() > 2){
				topPanel.remove(2);
			}
			if(!(topic instanceof Tag)){
				Button islandButton = new Button(manager.myConstants.tag_upgrade());
				islandButton.addClickListener(new ClickListener(){
					public void onClick(Widget sender) {
						makeThisAnIsland();
					}});				
				topPanel.add(islandButton);
			}else{
				topPanel.add(new Label(manager.myConstants.tag_topicIsA()));
			}
		}
	}

	private void makeThisAnIsland(){
		manager.getTagCache().makeMeATag(topic,new StdAsyncCallback(manager.myConstants.tag_upgradeAsync()){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				Tag tag = (Tag) result;
				manager.growIsland(tag);
				setupTopic();
			}

		});
	}

	public void save() {
		topic.getLatestEntry().setData(textArea.getText());
		topic.setTitle(titleBox.getText());
		
		topic.setSubject(subjectBoard.getSelectedSubject());
		
		tagBoard.saveThingsNowEvent(new StdAsyncCallback("save things now"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				Set otherTopicsToSave = (Set) result;
				topicViewAndEditWidget.save(topic,otherTopicsToSave);					
			}});
		
	}


}
