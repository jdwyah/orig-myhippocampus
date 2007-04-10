package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.PNGImage;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class StatusPicker extends Composite {
	
	public static final int CHANGE_TO_ISLAND_ACTION = 1; 
	public static final int CHANGE_TO_TOPIC_ACTION = 2;
	public static final int DELETE_ACTION = 3;
		
	private HorizontalPanel optionsP;
	private SimplePanel currentP;
	private ClickListener editListener;
	private PNGImage topicImage;
	private PNGImage islandImage;
	private ClickableImg topicO;
	private ClickableImg isleO;
	private ClickableImg deleteO;
	private Manager manager;
	private Topic topic;

	public StatusPicker(Manager manager){
		this.manager = manager;	
		
		HorizontalPanel mainP = new HorizontalPanel();			
		optionsP = new HorizontalPanel();
		currentP = new SimplePanel();
		
		editListener = new ClickListener(){			
			public void onClick(Widget sender) {
				toggle();
			}
		};
		topicImage = new PNGImage(ConstHolder.myConstants.topic_picker_topic_image(),20,20);
		topicImage.addClickListener(editListener);
		islandImage = new PNGImage(ConstHolder.myConstants.topic_picker_island_image(),20,20);
		islandImage.addClickListener(editListener);
				
		topicO = new ClickableImg(ConstHolder.myConstants.topic_picker_topic_image(),
				ConstHolder.myConstants.topic_picker_topic_tooltip(),20,20,CHANGE_TO_TOPIC_ACTION);
		isleO = new ClickableImg(ConstHolder.myConstants.topic_picker_island_image(),
				ConstHolder.myConstants.topic_picker_island_tooltip(),20,20,CHANGE_TO_ISLAND_ACTION);
		deleteO = new ClickableImg(ConstHolder.myConstants.topic_picker_delete_image(),
				ConstHolder.myConstants.topic_picker_delete_tooltip(),20,20,DELETE_ACTION);
		
		optionsP.add(topicO);
		optionsP.add(isleO);
		optionsP.add(deleteO);
		optionsP.setVisible(false);
		
		visualize(CHANGE_TO_ISLAND_ACTION);		
		
		mainP.add(currentP);
		mainP.add(optionsP);
		
		initWidget(mainP);
		addStyleName("H-StatusPicker");
		
	}
	public void updateImage(PNGImage image) {				
		currentP.clear();
		currentP.add(image);		
	}
	private void toggle(){		
		optionsP.setVisible(!optionsP.isVisible());
	}


	public void load(Topic topic) {
		this.topic = topic;
		if(topic instanceof Tag){
			visualize(CHANGE_TO_ISLAND_ACTION);
		}else{
			visualize(CHANGE_TO_TOPIC_ACTION);
		}
	}

	/**
	 * Execute the action, visualize on success
	 * 
	 * @param action_command
	 */
	private void fireAction(final int action_command){
		StdAsyncCallback callback = new StdAsyncCallback(""){
			public void onSuccess(Object result) {
				super.onSuccess(result);
				visualize(action_command);
			}};
		
		if(action_command == CHANGE_TO_ISLAND_ACTION){
			callback.setCall(ConstHolder.myConstants.topic_picker_island_tooltip());
			manager.changeState(topic,true,callback);
		}
		else if(action_command == CHANGE_TO_TOPIC_ACTION){
			callback.setCall(ConstHolder.myConstants.topic_picker_topic_tooltip());
			manager.changeState(topic,false,callback);
		}
		else if(action_command == DELETE_ACTION){
			callback.setCall(ConstHolder.myConstants.topic_picker_delete_tooltip());
			delete(callback);			
		}
		
	}
	
	private void delete(AsyncCallback callback) {
		if(Window.confirm(ConstHolder.myConstants.delete_warningS(topic.getTitle()))){
			manager.delete(topic,callback);
		}
	}
	
	
	private void visualize(int action_command){
		
		if(action_command == CHANGE_TO_ISLAND_ACTION){
			updateImage(islandImage);
			topicO.setVisible(true);
			isleO.setVisible(false);
		}else if(action_command == CHANGE_TO_TOPIC_ACTION){
			updateImage(topicImage);
			topicO.setVisible(false);
			isleO.setVisible(true);
		}else if(action_command == DELETE_ACTION){
			
		}
		
		optionsP.setVisible(false);
	}
	
	
	private class ClickableImg extends PNGImage implements ClickListener{

		private int action_command;
		
		public ClickableImg(String string, String tooltip,int i, int j, int action_command) {
			super(string,i,j);
			this.action_command = action_command;			
			addClickListener(this);			
			addMouseListener(new TooltipListener(tooltip));
		}

		public void onClick(Widget sender) {			
			fireAction(action_command);		
		}
		
	}

	
}