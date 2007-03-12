package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TimeGadget extends Gadget implements CloseListener {


	private Topic topic;
	private Manager manager;
	private VerticalPanel metasPanel;
	private VerticalPanel mainPanel;
	
	public TimeGadget(Manager _manager){		
		
		super(ConstHolder.myConstants.gadget_time_title());
		
		this.manager = _manager;
		
		
		metasPanel = new VerticalPanel();		
		
		mainPanel = new VerticalPanel();
		
		Button addEditButton = new Button(ConstHolder.myConstants.meta_add_edit());
		addEditButton.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				manager.editMetas(TimeGadget.this);
			}});
		
		mainPanel.add(metasPanel);		
		mainPanel.add(addEditButton);		
		
		initWidget(mainPanel);		
		
		addStyleName("H-TimeGadget");		
		
	}

	//@Override
	public int load(Topic topic) {
		this.topic = topic;
		
		return 1;
	}

	//@Override
	public ImageButton getPickerButton() {		
		ImageButton b = new ImageButton(ConstHolder.myConstants.img_gadget_time(),40,60);
		b.addMouseListener(new TooltipListener(0,40,ConstHolder.myConstants.gadget_time_title()));
		return b;
	}


	//@Override
	public boolean isOnForTopic(Topic topic) {
		return topic.hasTimeMetas();
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	

	

	
}
