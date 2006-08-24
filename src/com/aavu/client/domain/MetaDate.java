package com.aavu.client.domain;

import java.util.Date;
import java.util.Map;

import org.gwtwidgets.client.ui.cal.CalendarPanel;
import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.widget.datepicker.DateFormat;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaDate extends Meta {

	private static final String TYPE = "Date";
	private Map metaMap;

	//@Override
	public String getType() {
		return TYPE;
	}

	public Widget getWidget(Map mmp) {

		HorizontalPanel widget = new HorizontalPanel();
		Label label = new Label(getName());

		
		widget.add(label);
		Object mv = mmp.get(toMapIdx());
		if(mv != null){
			widget.add(new Label(mv.toString()));	
		}
		else{
			widget.add(new Label(""));
		}
		
		
		return widget;
	}
	
	//@Override
	public Widget getEditorWidget(Map mmp) {
		this.metaMap = mmp;
		
		HorizontalPanel widget = new HorizontalPanel();
		
		
		DatePicker datePicker = new SimpleDatePicker("mydate");
	    
	    // Set whether or not you want the weekends selectable
	    datePicker.setWeekendSelectable(true);
	    
	    String mv = (String)mmp.get(this);
	    if(mv != null){
	    	long longDate = Long.parseLong(mv);
	    	Date date = new Date(longDate);
	    	datePicker.setCurrentDate(date);
	    }
	    	    
	    datePicker.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				DatePicker dp = (DatePicker) sender;
				//MetaValue mv = new MetaValue();
				//mv.setValue(""+dp.getSelectedDate().getTime());
				String val = dp.getSelectedDate().getTime()+"";
				metaMap.put(toMapIdx(), val);
			}	    	
	    });
	    
	    // Set the Date Format
	    datePicker.setDateFormat(DateFormatter.DATE_FORMAT_DDMONYYYY);
	    
	    widget.add(new Label(getName()));
	    widget.add(datePicker);
	    
		return widget;
		
	}



}
