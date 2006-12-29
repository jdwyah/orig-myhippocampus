package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.widget.datepicker.DateFormatter;
import com.aavu.client.widget.datepicker.SimpleDatePicker;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MetaDate extends Meta implements IsSerializable,Serializable {

	private static final String TYPE = "Date";
	
	private transient Topic topic;
	private transient HippoDate mv;
	
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
		HippoDate mv = (HippoDate) topic.getSingleMetaValueFor(this);
		if(mv != null){
						
			widget.add(new Label(" "+mv.getTitle()));	
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
				
		SimpleDatePicker datePicker = new SimpleDatePicker("mydate");
	    
	    // Set whether or not you want the weekends selectable
	    datePicker.setWeekendSelectable(true);

	    // Set the Date Format
	    datePicker.setDateFormat(DateFormatter.DATE_FORMAT_MMDDYYYY);
	    
	    mv = (HippoDate) topic.getSingleMetaValueFor(this);
	    
	    if(mv != null){
	    	
	    	datePicker.setCurrentDate(mv.getDate());
	    	
	    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");						
	    	datePicker.setText(df.format(mv.getDate()));
	    }else{
	    	mv = new HippoDate();
	    }
	    	    
	    datePicker.addChangeListener(new ChangeListener(){
			public void onChange(final Widget sender) {
				SimpleDatePicker dp = (SimpleDatePicker) sender;
				System.out.println("cur "+dp.getCurrentDate());
				System.out.println("dp "+dp.getText());
				System.out.println("dp "+dp.getSelectedDate());
				
				DeferredCommand.add(new Command(){

					public void execute() {
						SimpleDatePicker dp = (SimpleDatePicker) sender;
						Date cDate = dp.getSelectedDate();
						//String val = cDate.getTime()+"";
						
						mv.setDate(cDate);
						
						//mv.setTitle(df.format(cDate));
						//mv.setData(val);
												
						//topicService.getTopicIdentForNameOrCreateNew(linkTo, callback)
						
						topicService.save(mv, new StdAsyncCallback("Meta Date Save"){
							public void onSuccess(Object result) {	
								super.onSuccess(result);
								Topic[] res = (Topic[]) result;
								topic.addMetaValue(MetaDate.this, res[0]);
							}});
			    
					}});
			
			}	    	
	    });
	    
	    
	    widget.add(new Label(getName()));
	    widget.add(datePicker);
	    
		return widget;
		
	}



}
