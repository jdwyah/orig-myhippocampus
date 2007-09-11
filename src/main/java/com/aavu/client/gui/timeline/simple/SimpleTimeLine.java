package com.aavu.client.gui.timeline.simple;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.gwm.client.event.GFrameEvent;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.TopicPreviewLink;
import com.aavu.client.gui.timeline.CloseListener;
import com.aavu.client.gui.timeline.HippoTimeline;
import com.aavu.client.service.Manager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class SimpleTimeLine extends Composite implements HippoTimeline {

	private static final DateTimeFormat decadeFormat = DateTimeFormat.getFormat("yyyy");
	private static final DateTimeFormat yearFormat = DateTimeFormat.getFormat("yyyy");
	private static final DateTimeFormat monthFormat = DateTimeFormat.getFormat("MMM yyyy");
	private static final DateTimeFormat weekFormat = DateTimeFormat.getFormat("MMM, d yyyy");
	private static final DateTimeFormat dayFormat = DateTimeFormat.getFormat("MMM, dd");
		
	public static final long MSEC_PER_DAY = 1000*60*60*24;
	public static final long MSEC_PER_WEEK = MSEC_PER_DAY * 7;
	public static final long MSEC_PER_MONTH = MSEC_PER_DAY * 30;
	public static final long MSEC_PER_YEAR = MSEC_PER_DAY * 365;
	public static final long MSEC_PER_DECADE = MSEC_PER_DAY * 3650;
	
	
	private static final double DETAIL_PCT = .85;
	
	private static final double DETAIL_SCALE = 2.5;
	private static final int HEIGHT = 400;

	private static final int MIN_RESIZE = 300;

	private static final int WIDTH = 680;

	private static final int MAX_MEMBERS = 10;
	private static final int TIME_SEGMENTS = 10;
	
	
	private int height;
	private Manager manager;

	private CellPanel timePanel;

	private SimplePanel detailPanel;

	private TimeSegment[] segments;

	private Date startDate;
	private Date endDate;

	

	public SimpleTimeLine(Manager manager,int width,int height, CloseListener close){
		
		super();
		
		this.manager = manager;
		this.height = height;
		
		timePanel = new HorizontalPanel();
		
		segments = new TimeSegment[TIME_SEGMENTS];

		detailPanel = new SimplePanel(); 
		
		for(int i =0; i < TIME_SEGMENTS; i++){
			TimeSegment seg = new TimeSegment(detailPanel,i);
			timePanel.add(seg);
			segments[i] = seg;
		}
		
		
		CellPanel mainPanel = new VerticalPanel();
		mainPanel.add(timePanel);
		mainPanel.add(detailPanel);
		
		
		initWidget(mainPanel);
		
	}
	
	



	

	private void drawDates(long start, long interval) {
		
		long cur = start;
		
		DateTimeFormat formatter = getFormatter(interval);
		
		for (int i = 0; i < segments.length; i++) {
			TimeSegment seg = segments[i];			
			
			seg.setDate(cur,interval,formatter);
			
			cur += interval;			
		}
		
	}


	private DateTimeFormat getFormatter(long interval) {
		
		if(interval < MSEC_PER_WEEK){
			System.out.println("MSEC_PER_WEEK "+interval+" "+MSEC_PER_WEEK);
			return dayFormat;
		}else if(interval < MSEC_PER_MONTH){
			System.out.println("MSEC_PER_MONTH "+interval+" "+MSEC_PER_MONTH);
			return weekFormat;
		}else if(interval < MSEC_PER_YEAR){
			System.out.println("MSEC_PER_YEAR "+interval+" "+MSEC_PER_YEAR);
			return monthFormat;
		}else if(interval < MSEC_PER_DECADE){
			System.out.println("MSEC_PER_DECADE "+interval+" "+MSEC_PER_DECADE);
			return yearFormat;
		}
		return decadeFormat;
	}







	protected void add(TimeLineObj tlo,long start, long interval) {
		
		int bucket = (int) ((tlo.getStartDate().getTime() - start) / interval);
		
		//System.out.println(bucket+" "+tlo.getDate().getTime()+" - "+start+" / "+interval);
		
		try{
			segments[bucket].add(tlo);
		}catch (Exception e) {
			segments[segments.length -1].add(tlo);
			System.out.println("fail "+e.getMessage());
		}
		
	}
	
	

	
	public void clear(){
		
	}


	public Widget getWidget() {
		return this;
	}


	
	public void add(List timelines) {
				
		
		//TreeOfTime tree = new TreeOfTime(1,3,MAX_MEMBERS);
		
	
		long start = Long.MAX_VALUE;
		long end = Long.MIN_VALUE;
		for (Iterator iterator = timelines.iterator(); iterator.hasNext();) {
			TimeLineObj tlo = (TimeLineObj) iterator.next();
			long time = tlo.getStartDate().getTime(); 
			if(time > end){
				end = time;
			}
			if(time < start){
				start = time;
			}
		}
		
		
		long span = end - start;		
		long interval = span / TIME_SEGMENTS;
		
		drawDates(start,interval);
		
		for (Iterator iterator = timelines.iterator(); iterator.hasNext();) {
			TimeLineObj tlo = (TimeLineObj) iterator.next();
			
			add(tlo,start,interval);
			
		}
		
		
	}








	public void onChange(final Widget sender) {
		
	
	}



	private void redraw() {
		
	}

	public void resize(int newWidth, int newHeight) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
