package com.aavu.client.gui.gadgets;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Topic;
import com.aavu.client.gui.ext.TooltipListener;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.edit.TopicWidget;
import com.aavu.client.wiki.TextDisplay;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TimeGadget extends Gadget {


	private Topic topic;
	private Manager manager;
	
	public TimeGadget(Manager _manager){		
		
		super(ConstHolder.myConstants.gadget_time_title());
		
		this.manager = _manager;
		
		
		Label l = new Label("d");
		
	
		initWidget(l);		
		
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
	
	

	

	
}
