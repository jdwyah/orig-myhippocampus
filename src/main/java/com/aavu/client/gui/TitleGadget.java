package com.aavu.client.gui;

import java.util.Date;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.commands.SaveDateCreatedCommand;
import com.aavu.client.domain.commands.SaveTitleCommand;
import com.aavu.client.gui.ext.EditableLabelExtension;
import com.aavu.client.gui.gadgets.Gadget;
import com.aavu.client.gui.gadgets.StatusPicker;
import com.aavu.client.service.Manager;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.widget.HeaderLabel;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePickerInterface;
import com.aavu.client.widget.datepicker.HDatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.aavu.client.widget.datepickertimeline.DatePickerTimeline;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TitleGadget extends Gadget {

	
	private EditableLabelExtension titleBox;
	private Topic topic;
	private StatusPicker picker;
	private DatePickerInterface datePicker;

	//private static SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	
	public TitleGadget(final Manager manager){
		super("");
		
		titleBox = new EditableLabelExtension("",new ChangeListener(){
			public void onChange(Widget sender) {								
				manager.getTopicCache().executeCommand(topic,new SaveTitleCommand(topic, titleBox.getText()),
						new StdAsyncCallback(ConstHolder.myConstants.save()){});				
			}			
		});
		
		//datePicker = new DatePickerTimeline(manager,400,200,null);
		
	datePicker = new HDatePicker(HorizontalPanel.ALIGN_RIGHT);	    
	    datePicker.setWeekendSelectable(true);
	    datePicker.setDateFormat(DateFormatter.DATE_FORMAT_MMDDYYYY);
	    
	  
	    	    
	    datePicker.addChangeListener(new ChangeListener(){
			public void onChange(final Widget sender) {
				DatePickerInterface dp = (DatePickerInterface) sender;
				System.out.println("cur "+dp.getCurrentDate());
				//System.out.println("dp "+dp.getText());
				System.out.println("dp "+dp.getSelectedDate());
				
				DeferredCommand.add(new Command(){

					public void execute() {
						SimpleDatePicker dp = (SimpleDatePicker) sender;
						Date cDate = dp.getSelectedDate();
						
						if(!topic.getCreated().equals(cDate)){
							System.out.println("\n\n!= "+cDate+" "+topic.getCreated());
							
							manager.getTopicCache().executeCommand(topic, 
									new SaveDateCreatedCommand(topic,cDate),new StdAsyncCallback(ConstHolder.myConstants.save()+"TitleDate"));
						}else{
							System.out.println("\n\nEQAL forget it");
						}
					}});
			}});
		
	    
	    
		CellPanel titleP = new HorizontalPanel();
		titleP.add(new HeaderLabel(ConstHolder.myConstants.title()));
		titleP.add(titleBox);
	
		picker = new StatusPicker(manager);
		
		titleP.add(picker);
		
		
		CellPanel dateP = new HorizontalPanel();
		dateP.add(new HeaderLabel(ConstHolder.myConstants.date()));
		dateP.add(datePicker.getWidget());
		
		VerticalPanel mainP = new VerticalPanel();
		mainP.add(titleP);
		mainP.add(dateP);
		initWidget(mainP);
	}

	//@Override
	public Image getPickerButton() {		
		throw new UnsupportedOperationException();
	}

	//@Override
	public int load(Topic topic) {
		this.topic = topic;
		titleBox.setText(topic.getTitle());
		picker.load(topic);
		
		datePicker.setSelectedDate(topic.getCreated());
		//datePicker.setCurrentDate(topic.getCreated());
	    						
		//datePicker.setText(df.format(mv.getStartDate()));
		
		return 0;
	}

	//@Override
	public boolean isOnForTopic(Topic topic) {
		return true;
	}
	
	//@Override
	public void onClick(Manager manager) {
		throw new UnsupportedOperationException();
	}
	
}
