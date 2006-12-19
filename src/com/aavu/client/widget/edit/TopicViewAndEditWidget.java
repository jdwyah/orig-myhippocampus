package com.aavu.client.widget.edit;


import java.util.Set;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.TopicWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicViewAndEditWidget extends Composite implements ClickListener{
	
	
	private Button editTextButton;
	private Button cancelButton;	
	private Button previewButton;
	
	private TopicWidget topicWidget;
	private TopicEditWidget topicEditWidget;

	private VerticalPanel topicPanel;
	private VerticalPanel lp;
	
	public Topic topic;
	private Manager manager;
	private SaveNeededListener saveNeeded;
	
	
	//TODO fix window null
	public TopicViewAndEditWidget(Manager manager, SaveNeededListener saveNeeded) {
		this.saveNeeded = saveNeeded;
		this.manager = manager;
		
		editTextButton = new Button(manager.myConstants.topic_edit());
		cancelButton = new Button(manager.myConstants.topic_cancel());	
		previewButton = new Button(manager.myConstants.topic_preview());
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		
		lp = new VerticalPanel();
		
		topicPanel = new VerticalPanel();
		
		cancelButton.addClickListener(this);
		editTextButton.addClickListener(this);
		previewButton.addClickListener(this);
		
		mainPanel.add(lp);
		mainPanel.add(topicPanel);
		
		
		initWidget(mainPanel);
	}	
	
	
	public void load(Topic topic){
		this.topic = topic;
		topicWidget = new TopicWidget(manager,topic);
		topicEditWidget = new TopicEditWidget(this,manager,topic);
		
		activateMainView();
		System.out.println("############################## "+topic.getTitle());
		
		
		Effect.highlight(topicPanel);
	}
		
	

	public void onClick(Widget source){
		if (source == cancelButton){
			activateMainView();
		}
		else if (source == editTextButton){
			activateEditView();
		}
		else if (source == previewButton){

			saveNeeded.onChange(this);
			topicWidget.setText(getEditEntryText());
			activateMainView();
		}
		
		
	}
	


	public void activateMainView(){
	
		topicPanel.clear();
		topicPanel.add(topicWidget);				

		lp.clear();
		lp.add(editTextButton);
		
	}
	
	public void activateEditView() {
		
		topicPanel.clear();
		topicPanel.add(topicEditWidget);				
				
		lp.clear();
		lp.add(previewButton);
		lp.add(cancelButton);		
		
	}

	public Entry getEntry(){
		return topic.getLatestEntry();
	}
	public String getEntryText(){		
		return topicWidget.getText();
	}

	private String getEditEntryText() {
		return topicEditWidget.getCurrentText();
	}
	
	
}
