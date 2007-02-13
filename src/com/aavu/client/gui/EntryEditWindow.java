package com.aavu.client.gui;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GInternalFrameEvent;
import org.gwm.client.event.GInternalFrameListener;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.edit.SaveNeededListener;
import com.aavu.client.widget.edit.TopicViewAndEditWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class EntryEditWindow extends PopupWindow implements SaveNeededListener, GInternalFrameListener {

	private static final int WIDTH = 700;
	private static final int HEIGHT = 500;
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
		
		addInternalFrameListener(this);
	}
	private void save() {
		manager.getTopicCache().save(topicViewAndEditW.getTopic(),topicViewAndEditW.getSaveCommand(),
				new StdAsyncCallback(""){
					public void onSuccess(Object result) {					
						super.onSuccess(result);
						saveButton.saveAccomplished();
						topicViewAndEditW.load(topicViewAndEditW.getTopic());
						topicViewAndEditW.activateMainView();	
						manager.bringUpChart(topicViewAndEditW.getTopic());
					}});		
	}			

	public void onChange(Widget sender) {
		saveButton.setSaveNeeded();
	}
	public void frameClosed(GInternalFrameEvent evt) {
		//Window.alert("closed");
		//evt.
	}
	public void frameIconified(GInternalFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameMaximized(GInternalFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameMinimized(GInternalFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameMoved(GInternalFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameOpened(GInternalFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameResized(GInternalFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameRestored(GInternalFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
}
