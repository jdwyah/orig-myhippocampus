package com.aavu.client.widget.edit;

import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicEditWidget extends Composite {

	
	private TextBox titleBox = new TextBox();
	private SpecialTextbox textArea = null;

	private TagBoard tagBoard;
	
	private Topic topic;
	private TopicViewAndEditWidget topicViewAndEditWidget;
	
	private SeeAlsoBoard seeAlsoBoard;
	
	public TopicEditWidget(TopicViewAndEditWidget topicViewAndEditWidget, Manager manager, Topic topic){
		this.topic = topic;
		this.topicViewAndEditWidget = topicViewAndEditWidget;
		
		System.out.println("topic edit widg "+topic);
		
		textArea = new SpecialTextbox(manager.getTopicCache());
		
		tagBoard = new TagBoard(manager);
		seeAlsoBoard = new SeeAlsoBoard(manager);
		
		setupTopic();
		
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(new HeaderLabel(Manager.myConstants.title()));
		panel.add(titleBox);
		
		panel.add(tagBoard);		
		panel.add(seeAlsoBoard);
		
			
		
		panel.add(textArea);
	
	
		initWidget(panel);
	}
	
	
	
	private void setupTopic() {
		System.out.println("setupTopic");
		if(topic != null){
			titleBox.setText(topic.getTitle());		
			textArea.setText(topic.getData());
						
			tagBoard.load(topic);
			seeAlsoBoard.load(topic);
		}
	}



	public void save() {
		topic.setData(textArea.getText());
		topic.setTitle(titleBox.getText());
		
		tagBoard.saveThingsNowEvent(new StdAsyncCallback("save things now"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				Set otherTopicsToSave = (Set) result;
				topicViewAndEditWidget.save(topic,otherTopicsToSave);					
			}});
		
	}


}
