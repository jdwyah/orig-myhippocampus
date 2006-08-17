package com.aavu.client.domain;

import java.util.Date;

import org.gwtwidgets.client.ui.cal.CalendarPanel;

import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class MetaDate extends MetaValue {

	private static final String TYPE = "Date";
	private Date value;
	private Date transientValue;
	//private DatePicker datePicker;
	

	public MetaDate(){
		this(new Date());
	}
	public MetaDate(Date date){
		this.value = date;
	}
	
	//@Override
	public String getType() {
		return TYPE;
	}

	//@Override
	public Widget getWidget() {
		DatePicker datePicker = new SimpleDatePicker("mydate");
	    
	    // Set whether or not you want the weekends selectable
	    datePicker.setWeekendSelectable(true);
	    datePicker.setCurrentDate(value);
	    
	    datePicker.addChangeListener(new ChangeListener(){

			public void onChange(Widget sender) {
				transientValue = ((DatePicker)sender).getCurrentDate();
				
			}
	    	
	    });
	    
	    // Set the Date Format
	    datePicker.setDateFormat(DateFormatter.DATE_FORMAT_DDMONYYYY);
	    
		return datePicker;
	}

	//@Override
	public boolean equals(Object other) {
		// TODO Auto-generated method stub
		return false;
	}

	//@Override
	protected Object getValue() {
		return value;
	}

	//@Override
	public void save() {
		value = transientValue;
		
	}


}
