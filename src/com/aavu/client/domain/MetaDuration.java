package com.aavu.client.domain;

import java.util.Date;
import java.util.Map;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.service.Manager;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaDuration extends Meta {

	private static final String TYPE = "Time Span";
	private static SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");

	private Map metaMap;


	//@Override
	public String getType() {
		return TYPE;
	}

	public String serialize(Date start, Date end){
		StringBuffer sb = new StringBuffer(";");
		sb.insert(0, start.getTime());
		sb.append(end.getTime());
		return sb.toString();
	}

	public Date[] unSerialize(String str){
		String[] split = str.split(";");
		Date start = new Date(Long.parseLong(split[0]));
		Date end = new Date(Long.parseLong(split[1]));
		return new Date[]{start, end};
	}


	public Widget getWidget(Map mmp) {

		HorizontalPanel widget = new HorizontalPanel();
		Label label = new Label(getName());

		widget.add(label);
		Object mv = mmp.get(toMapIdx());

		Date[] dates = new Date[2];
		if(mv != null){			
			dates= unSerialize(mv.toString());
		}
		
		if(dates[0] != null){
			widget.add(new Label(Manager.myConstants.startDuration()+df.format(dates[0])));
		}else{
			widget.add(new Label(Manager.myConstants.startDuration()));
		}
		if(dates[1] != null){
			widget.add(new Label(Manager.myConstants.endDuration()+df.format(dates[1])));
		}else{
			widget.add(new Label(Manager.myConstants.endDuration()));
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

		// Set the Date Format
		datePicker.setDateFormat(DateFormatter.DATE_FORMAT_DDMONYYYY);

		String mv = (String)mmp.get(toMapIdx());	    

		if(mv != null){
			long longDate = Long.parseLong(mv);
			Date date = new Date(longDate);
			datePicker.setCurrentDate(date);
			SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");						
			datePicker.setText(df.format(new Date(Long.parseLong(mv.toString()))));
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

		

		widget.add(new Label(getName()));
		widget.add(datePicker);
		widget.add(datePicker);

		return widget;

	}



}
