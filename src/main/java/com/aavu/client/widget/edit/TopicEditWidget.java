package com.aavu.client.widget.edit;

import java.util.Set;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicEditWidget extends Composite implements SourcesChangeEvents {
		
	private SpecialTextbox textArea = null;

	//private TagBoard tagBoard;
	//private SubjectBoard subjectBoard;
	
	private Topic topic;
	private TopicViewAndEditWidget topicViewAndEditWidget;	

	private Manager manager;
	
	public TopicEditWidget(TopicViewAndEditWidget topicViewAndEditWidget, final Manager manager, Topic _topic){
		this.topic = _topic;
		this.topicViewAndEditWidget = topicViewAndEditWidget;
		this.manager = manager;
		
		System.out.println("topic edit widg "+topic);
		
		textArea = new SpecialTextbox(manager.getTopicCache());
		
				
		//tagBoard = new TagBoard(manager);
		//subjectBoard = new SubjectBoard(manager,titleBox,tagBoard);
			
		
		VerticalPanel panel = new VerticalPanel();		
		
		//panel.add(subjectBoard);
		//panel.add(tagBoard);		
				
		panel.add(textArea);
	
	
		initWidget(panel);

		
	}
	
	
	//@Override
	protected void onAttach() {
		super.onAttach();
		setupTopic();	
	}



	private void setupTopic() {
		System.out.println("setupTopic");
		if(topic != null){		
			textArea.setText(topic.getLatestEntry().getData());			
		}
	}

	
	public String getCurrentText(){
		return textArea.getText();
	}



	public void addChangeListener(ChangeListener listener) {
		textArea.addChangeListener(listener);
	}



	public void removeChangeListener(ChangeListener listener) {
		textArea.removeChangeListener(listener);
	}


}
