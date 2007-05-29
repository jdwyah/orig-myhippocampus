package com.aavu.client.widget.edit;


import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.SaveEntryTextCommand;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TopicViewAndEditWidget extends Composite implements ClickListener, ChangeListener{
	
	
	private TopicWidget topicWidget;
	private TopicEditWidget topicEditWidget;

	private VerticalPanel topicPanel;
		
	public Topic topic;
	private Manager manager;
	private SaveNeededListener saveNeeded;
	
	
	public TopicViewAndEditWidget(Manager manager, SaveNeededListener saveNeeded) {
		this.saveNeeded = saveNeeded;
		this.manager = manager;
		
		
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		
		topicPanel = new VerticalPanel();
		
		topicWidget = new TopicWidget();
		
		mainPanel.add(topicPanel);		
		
		
		initWidget(mainPanel);
		setStyleName("H-ViewEdit");
	}	
	
	
	public void load(Topic topic){
		this.topic = topic;
		topicWidget.load(topic);
		topicWidget.addClickListener(this);		
		
		topicEditWidget = new TopicEditWidget(this,manager,topic);
		topicEditWidget.addChangeListener(this);
		
		activateMainView();
		System.out.println("############################## "+topic.getTitle());
		
		
	}
		
	

	public void onClick(Widget source){
		if (source == topicWidget){			
			activateEditView();
		}				
	}
	


	public void activateMainView(){
	
		topicPanel.clear();
		topicPanel.add(topicWidget);				

	}
	
	public void activateEditView() {
		
		topicPanel.clear();
		topicPanel.add(topicEditWidget);				
						
	}

	public Entry getEntry(){
		return topic.getLatestEntry();
	}
	public String getEntryText(){		
		return getEditEntryText();
	}

	private String getEditEntryText() {
		return topicEditWidget.getCurrentText();
	}


	public void onChange(Widget sender) {		
		saveNeeded.onChange(this);
	}


	public AbstractCommand getSaveCommand() {
		return new SaveEntryTextCommand(topic,getEntryText());
	}


	public Topic getTopic() {
		return topic;
	}
	
	
}
