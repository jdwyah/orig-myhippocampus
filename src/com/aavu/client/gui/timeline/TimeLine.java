package com.aavu.client.gui.timeline;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.service.Manager;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TimeLine extends PopupWindow {

	private static SimpleDateFormat df = new SimpleDateFormat("M/d/yyyy");
	
	private static final int TOTAL_WIDTH = 300;

	private static final int DEFAULT_WIDTH = 10;
	
	Date startDate = new Date(2004,1,1);
	Date endDate = new Date(2006,1,1);
	long span = endDate.getTime() - startDate.getTime();
	
	/**
	 * List <TimeLineObj>
	 * @param timeLinesObjs
	 */
	public TimeLine(Manager manager,List timeLinesObjs){
		super(manager.myConstants.helloWorld());
		
		VerticalPanel times = new VerticalPanel();
		
		int row = 0;
		
		
		
		for (Iterator iter = timeLinesObjs.iterator(); iter.hasNext();) {
			TimeLineObj timeLineObj = (TimeLineObj) iter.next();
			
			if(timeLineObj.getStart() == null && timeLineObj.getStart() == null){
				continue;
			}
			
			//user start or end whichever isn't null
			int loc = getLoc((timeLineObj.getStart() == null) ? timeLineObj.getEnd() : timeLineObj.getStart());
			
			int width = getWidth(timeLineObj.getStart(), timeLineObj.getEnd());
			
			System.out.println("width "+width);
			
			times.add(new LineOfTime(timeLineObj.getTopic(),loc,width));
						
			row++;
		}
	
		
		setContentPanel(times);
				
	}
	
	private int getLoc(Date date){				
		long thisGuy = date.getTime() - startDate.getTime();
		
		double pct = (double)thisGuy / (double)span;
		
		return (int)(pct * TOTAL_WIDTH);		
	}
	
	private int getWidth(Date start, Date end){
		if(start == null || end == null){
			return DEFAULT_WIDTH;
		}
		long thisGuy = end.getTime() - start.getTime();
		
		System.out.println("this guy "+thisGuy);
		
		
		return (int) (TOTAL_WIDTH * ((double)thisGuy / (double)span)) ;
	}
	
}
