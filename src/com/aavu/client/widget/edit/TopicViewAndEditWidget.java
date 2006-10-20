package com.aavu.client.widget.edit;


import java.util.Set;

import org.gwtwidgets.client.wrap.Effect;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.TopicWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.HippoCache;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicViewAndEditWidget extends Composite implements ClickListener{
	
	
	private Button editTextButton = new Button("Edit Text");
	private Button cancelButton = new Button("Cancel");	
	private Button saveButton = new Button("Save");
	
	private TopicWidget topicWidget;
	private TopicEditWidget topicEditWidget;

	private VerticalPanel topicPanel;
	private VerticalPanel lp;
	
	public Topic topic;
	private Manager manager;
	private TopicWindow window;	
	
	
	public TopicViewAndEditWidget(TopicWindow window, Manager manager) {
		this.manager = manager;
		this.window = window;
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		
		lp = new VerticalPanel();
		
		topicPanel = new VerticalPanel();
		
		cancelButton.addClickListener(this);
		editTextButton.addClickListener(this);
		saveButton.addClickListener(this);
		
		mainPanel.add(lp);
		mainPanel.add(topicPanel);
		
		
		initWidget(mainPanel);
	}	
	
	
	public void load(Topic topic){
		this.topic = topic;
		topicWidget = new TopicWidget(topic);
		topicEditWidget = new TopicEditWidget(this,manager,topic);
		
		activateMainView();
		System.out.println("############################## "+topic.getTitle());
		window.setText(topic.getTitle());
		
		Effect.highlight(topicPanel);
	}
		
	

	public void onClick(Widget source){
		if (source == cancelButton){
			activateMainView();
		}
		else if (source == editTextButton){
			activateEditView();
		}
		else if (source == saveButton){
			topicEditWidget.save();									
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
		lp.add(cancelButton);
		lp.add(saveButton);		
	}
	

	public void save(Topic topic2, Set otherTopicsToSave) {
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!");
		//TODO is this good or bad? a bit early.. 
		load(topic2);
		
		manager.getTopicCache().save(topic2, otherTopicsToSave, new StdAsyncCallback("topicDetail save") {				
			
			public void onSuccess(Object result) {		
				System.out.println("????????????????????");
				//this should prevent double saves
				Topic[] saved = (Topic[]) result;
				load(saved[0]);
				
				activateMainView();
			}
		});	

	}
	
}
