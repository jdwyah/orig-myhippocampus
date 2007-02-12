package com.aavu.client.gui;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.service.Manager;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.edit.TagBoard;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class CenterTopicDisplayer extends Composite {
	
	private TagBoard tagBoard;
	
	private EditableLabelExtension titleBox;
		
	private Topic topic;
	private Manager manager;
	
	public CenterTopicDisplayer(final Manager manager){
			
		this.manager = manager;
		
		tagBoard = new TagBoard(manager);		
				
		VerticalPanel mainPanel = new VerticalPanel();
		
				
		
		titleBox = new EditableLabelExtension("",new ChangeListener(){
			public void onChange(Widget sender) {								
				manager.getTopicCache().save(topic,new SaveTitleCommand(topic, titleBox.getText()),
						new StdAsyncCallback(Manager.myConstants.save()){});				
			}			
		});
		
		CellPanel titleP = new HorizontalPanel();
		titleP.add(new HeaderLabel(Manager.myConstants.title()));
		titleP.add(titleBox);
		titleP.addStyleName("H-Gadget");
		
		mainPanel.add(titleP);
		
				
		mainPanel.add(tagBoard);
		
		
		initWidget(mainPanel);
		
		addStyleName("H-AbsolutePanel");
		addStyleName("H-CenterInfo");		
		
		setVisible(false);
	}


	public void load(Topic topic) {
		
		this.topic = topic;
		
		setVisible(true);
		
		titleBox.setText(topic.getTitle());
				
		tagBoard.load(topic);
		
	}

	public void unload() {
		setVisible(false);
	}
	
	
}
