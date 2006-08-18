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

	public Widget getWidget(boolean editable) {
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
	    
		return datePicker;
	}
	

	//@Override
	public Widget getEditorWidget(boolean editable) {
		// TODO Auto-generated method stub
		return null;
	}


}
