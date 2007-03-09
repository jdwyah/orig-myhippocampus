package com.aavu.client.gui;

import org.gwtwidgets.client.ui.ImageButton;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.StatusPicker;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.HeaderLabel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TitleGadget extends Gadget {

	
	private EditableLabelExtension titleBox;
	private Topic topic;
	private StatusPicker picker;

	public TitleGadget(final Manager manager){
		super("");
		
		titleBox = new EditableLabelExtension("",new ChangeListener(){
			public void onChange(Widget sender) {								
				manager.getTopicCache().save(topic,new SaveTitleCommand(topic, titleBox.getText()),
						new StdAsyncCallback(ConstHolder.myConstants.save()){});				
			}			
		});
		
		CellPanel titleP = new HorizontalPanel();
		titleP.add(new HeaderLabel(ConstHolder.myConstants.title()));
		titleP.add(titleBox);
	
		picker = new StatusPicker(manager);
		
		titleP.add(picker);
		
		initWidget(titleP);
	}

	//@Override
	public ImageButton getPickerButton() {		
		throw new UnsupportedOperationException();
	}

	//@Override
	public int load(Topic topic) {
		this.topic = topic;
		titleBox.setText(topic.getTitle());
		picker.load(topic);
		return 0;
	}

	//@Override
	public boolean isOnForTopic(Topic topic) {
		return true;
	}
	
	
	
}
