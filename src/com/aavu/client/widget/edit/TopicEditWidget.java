package com.aavu.client.widget.edit;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.remote.GWTTagServiceAsync;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicEditWidget extends Composite implements ClickListener {

	private TextBox titleBox = new TextBox();
	private TextArea textArea = new TextArea();
	
	private Button saveButton = new Button("Save");
	
	private TagBoard tagBoard;
	
	private Topic topic;
	private TopicViewAndEditWidget topicViewAndEditWidget;
	
	
	public TopicEditWidget(TopicViewAndEditWidget topicViewAndEditWidget, GWTTagServiceAsync tagServiceAsync, Topic topic){
		this.topic = topic;
		this.topicViewAndEditWidget = topicViewAndEditWidget;
		
		saveButton.addClickListener(this);		
		
		tagBoard = new TagBoard(tagServiceAsync);
		
		setupTopic();
		
		VerticalPanel panel = new VerticalPanel();
		
		panel.add(new Label("Title"));
		panel.add(titleBox);
		
		panel.add(tagBoard);		
		
		//textArea.set
		
		panel.add(textArea);
	
		panel.add(saveButton);
	
		initWidget(panel);
	}
	
	
	private void setupTopic() {
		if(topic != null){
			titleBox.setText(topic.getTitle());
			textArea.setText(topic.getText());
						
			tagBoard.load(topic);
		}
	}


	public void onClick(Widget source) {
		if (source == saveButton){
			
			topic.setText(textArea.getText());
			topic.setTitle(titleBox.getText());
			
			tagBoard.saveThingsNowEvent();
					
			topicViewAndEditWidget.save(topic);			
		}
	}
}
