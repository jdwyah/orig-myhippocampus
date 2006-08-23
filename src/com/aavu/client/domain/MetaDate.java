package com.aavu.client.domain;

import java.util.Date;

import org.gwtwidgets.client.ui.cal.CalendarPanel;

import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaDate extends Meta {

	private static final String TYPE = "Date";

	//@Override
	public String getType() {
		return TYPE;
	}

	public Widget getWidget() {

		HorizontalPanel widget = new HorizontalPanel();
		Label label = new Label(getName());

		
		widget.add(label);
		widget.add(new Label("6/6/03"));
		
		return widget;
	}
	
	//@Override
	public Widget getEditorWidget() {
		HorizontalPanel widget = new HorizontalPanel();
		
		DatePicker datePicker = new SimpleDatePicker("mydate");
	    
	    // Set whether or not you want the weekends selectable
	    datePicker.setWeekendSelectable(true);
	    datePicker.setCurrentDate(new Date());	    
	    datePicker.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				//transientValue = ((DatePicker)sender).getCurrentDate();				
			}	    	
	    });
	    
	    // Set the Date Format
	    datePicker.setDateFormat(DateFormatter.DATE_FORMAT_DDMONYYYY);
	    
	    widget.add(new Label(getName()));
	    widget.add(datePicker);
	    
		return widget;
		
	}



}
