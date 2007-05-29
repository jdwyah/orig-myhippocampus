package com.aavu.client.gui.timeline.simple;

import java.util.Date;

import com.aavu.client.domain.dto.TimeLineObj;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TimeSegment extends Composite {
	
	private TimeLabel dateLabel;
	private TimeDisplay timeDisplay;
	private SimplePanel detailPanel;
	private Label moreLabel;
	private int moreCount;
	private long interval;
	private long start;
	
	private class TimeLabel extends Label implements MouseListener, ClickListener {
		public TimeLabel(){			
			super();
			addClickListener(this);
			addMouseListener(this);
		}

		public void onMouseDown(Widget sender, int x, int y) {}
		public void onMouseMove(Widget sender, int x, int y) {}
		public void onMouseUp(Widget sender, int x, int y) {}	
		
		public void onMouseEnter(Widget sender) {
			
			addStyleName("H-TimeLabel-Hover");
		}

		public void onMouseLeave(Widget sender) {
			removeStyleName("H-TimeLabel-Hover");
		}


		public void onClick(Widget sender) {
			//zoom()
		}
	}

	public TimeSegment(SimplePanel detailPanel,int index){
		this.detailPanel = detailPanel;
		
		CellPanel mainPanel = new VerticalPanel();
		
		
		
		timeDisplay = new TimeDisplay(index);
		mainPanel.add(timeDisplay);
		
		dateLabel = new TimeLabel();
		mainPanel.add(dateLabel);
		
				
		initWidget(mainPanel);
	}

	public void add(TimeLineObj tlo) {
		timeDisplay.add(tlo,start,interval);
		
//		if(numberPanel.getWidgetCount() < 6){
//			TopicPreviewLink l = new TopicPreviewLink(tlo.getTopic(),20,null,detailPanel,manager);
//			numberPanel.add(l);			
//		}else if(numberPanel.getWidgetCount() == 6){
//			moreCount = 1;
//			moreLabel = new Label("1 more...");
//			numberPanel.add(moreLabel);		
//		}else{
//			moreCount++;
//			moreLabel.setText(moreCount+" more...");		
//		}
	}

	public void setDate(long start, long interval, DateTimeFormat formatter) {
		dateLabel.setText(formatter.format(new Date(start)));
		this.start = start;
		this.interval = interval;
	}
}
