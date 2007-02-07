package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EntryEditWindow extends PopupWindow implements SaveNeededListener {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 460;
	private Manager manager;
	private SaveStopLight saveButton;
	private TopicViewAndEditWidget topicViewAndEditW;
	
	public EntryEditWindow(Topic topic, Manager manager, GInternalFrame frame) {
		super(frame,topic.getTitle(),WIDTH,HEIGHT);
		this.manager = manager;

		topicViewAndEditW = new TopicViewAndEditWidget(manager,this);
		topicViewAndEditW.load(topic);
		topicViewAndEditW.activateEditView();
		
		saveButton = new SaveStopLight(new ClickListener(){
				public void onClick(Widget sender) {
					save();	
				}
			});
		VerticalPanel mainP = new VerticalPanel();
		
		mainP.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		mainP.add(topicViewAndEditW);
		mainP.add(saveButton);
		
		setContent(mainP);
	}
	private void save() {
		manager.getTopicCache().save(topicViewAndEditW.getTopic(),topicViewAndEditW.getSaveCommand(),
				new StdAsyncCallback(""){
					public void onSuccess(Object result) {					
						super.onSuccess(result);
						saveButton.saveAccomplished();
						topicViewAndEditW.activateMainView();
					}});		
	}			

	public void onChange(Widget sender) {
		saveButton.setSaveNeeded();
	}
}
