package com.aavu.client.gui;

import org.gwm.client.GFrame;
import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameEvent;
import org.gwm.client.event.GFrameListener;

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

public class EntryEditWindow extends PopupWindow implements SaveNeededListener, GFrameListener {

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
		
		frame.setDefaultCloseOperation(GFrame.DO_NOTHING_ON_CLOSE);
		
		frame.addFrameListener(this);
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
	public void frameClosed(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameClosing(GFrameEvent evt) {
		if(saveButton.isSaveNeeded()){
			if(Window.confirm(Manager.myConstants.close_without_saving())){
				frame.close();
			}			
		}else{
			frame.close();
		}
	}
	public void frameIconified(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameMaximized(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameMinimized(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameMoved(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameOpened(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameResized(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	public void frameRestored(GFrameEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
}
