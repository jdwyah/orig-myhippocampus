package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicEditWidget extends Composite {

	
	private TextBox titleBox = new TextBox();
	private SpecialTextbox textArea = null;

	private TagBoard tagBoard;
	
	private Topic topic;
	private TopicViewAndEditWidget topicViewAndEditWidget;
	
	
	
	public TopicEditWidget(TopicViewAndEditWidget topicViewAndEditWidget, Manager manager, Topic topic){
		this.topic = topic;
		this.topicViewAndEditWidget = topicViewAndEditWidget;
		
		System.out.println("topic edit widg "+topic);
		
		textArea = new SpecialTextbox(manager.getTopicCache());
		
		tagBoard = new TagBoard(manager);
		
		setupTopic();
		
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(new Label("Title"));
		panel.add(titleBox);
		
		panel.add(tagBoard);		
		
		//textArea.set
		
		//SpecialTextbox	ww = new SpecialTextbox();
		
		
		
//		FocusPanel fp = new FocusPanel();
//		fp.addKeyboardListener(ww);
//		
//		fp.add(ww);
//		panel.add(fp);
		
		panel.add(textArea);
	
	
		initWidget(panel);
	}
	
	
	
	private void setupTopic() {
		System.out.println("setupTopic");
		if(topic != null){
			titleBox.setText(topic.getTitle());		
			textArea.setText(topic.getData());
						
			tagBoard.load(topic);
		}
	}



	public void save() {
		topic.setData(textArea.getText());
		topic.setTitle(titleBox.getText());
		
		tagBoard.saveThingsNowEvent(new StdAsyncCallback("save things now"){

			public void onSuccess(Object result) {
				topicViewAndEditWidget.save(topic);					
			}});
	}


}
