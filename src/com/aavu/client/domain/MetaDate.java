package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.DatePicker;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.aavu.client.widget.edit.TopicCompleter;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaDate extends Meta implements IsSerializable,Serializable {

	private static final String TYPE = "Date";
	private transient static SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");
	
	private transient Topic topic;
	private transient Topic mv;
	
	private transient static TopicCache topicService;	
	public static void setTopicService(TopicCache topicService) {
		MetaDate.topicService = topicService;
	}

	
	//@Override
	public String getType() {
		return TYPE;
	}

	public Widget getWidget(Topic topic) {

		HorizontalPanel widget = new HorizontalPanel();
		Label label = new Label(getName());
		
		widget.add(label);
		Topic mv = (Topic) topic.getSingleMetaValueFor(this);
		if(mv != null){
						
			widget.add(new Label(" "+df.format(new Date(Long.parseLong(mv.getData())))));	
		}
		else{
			widget.add(new Label(""));
		}
		
		
		return widget;
	}
	
	//@Override
	public Widget getEditorWidget(final Topic topic) {
		this.topic = topic;
		
		HorizontalPanel widget = new HorizontalPanel();
		
		
		DatePicker datePicker = new SimpleDatePicker("mydate");
	    
	    // Set whether or not you want the weekends selectable
	    datePicker.setWeekendSelectable(true);

	    // Set the Date Format
	    datePicker.setDateFormat(DateFormatter.DATE_FORMAT_DDMONYYYY);
	    
	    mv = (Topic) topic.getSingleMetaValueFor(this);
	    
	    if(mv != null){
	    	long longDate = Long.parseLong(mv.getData());
	    	Date date = new Date(longDate);
	    	datePicker.setCurrentDate(date);
	    	
	    	SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");						
	    	datePicker.setText(df.format(date));
	    }else{
	    	mv = new Topic();
	    }
	    	    
	    datePicker.addChangeListener(new ChangeListener(){
			public void onChange(Widget sender) {
				DatePicker dp = (DatePicker) sender;
				String val = dp.getSelectedDate().getTime()+"";
				
//				Topic newTopicValue = new Topic(getUser(),df.format(dp.getSelectedDate()));
//				newTopicValue.setData(val);
//				topic.addMetaValue(MetaDate.this, newTopicValue);
//				
				
				mv.setTitle(df.format(dp.getSelectedDate()));
				mv.setData(val);
				
				topicService.save(mv, new StdAsyncCallback("Meta Date Save"){
					public void onSuccess(Object result) {	
						super.onSuccess(result);
						Topic[] res = (Topic[]) result;
						topic.addMetaValue(MetaDate.this, res[0]);
					}});
	    
			}	    	
	    });
	    
	    
	    widget.add(new Label(getName()));
	    widget.add(datePicker);
	    
		return widget;
		
	}



}
