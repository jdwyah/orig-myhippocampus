package com.aavu.client.gui.timeline.renderers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.timeline.HippoTimeLine;
import com.aavu.client.util.Logger;
import com.netthreads.gwt.simile.timeline.client.BandInfo;
import com.netthreads.gwt.simile.timeline.client.BandOptions;
import com.netthreads.gwt.simile.timeline.client.DateTime;
import com.netthreads.gwt.simile.timeline.client.EventSource;
import com.netthreads.gwt.simile.timeline.client.HotZoneBandOptions;
import com.netthreads.gwt.simile.timeline.client.ITimeLineRender;
import com.netthreads.gwt.simile.timeline.client.Theme;
import com.netthreads.gwt.simile.timeline.client.TimeLineWidget;


/**
 * Render timeline
 * 
 */
public class HippoRender implements ITimeLineRender
{
    private static final int EVENT_LABEL_WIDTH = 200;

    private List myHotZones = new ArrayList();
    private List bottomHotZones = new ArrayList();
    
	/**
     * Create timeline custom elements.
     * 
     * NOTE. not sure what it was, perhaps sethighlight(true)? that made the hotzones start working.
     * 
     *@param widget Timeline rendered into this widget.
     */
    public void render(TimeLineWidget widget)
    {
    
    	ArrayList bandInfos = widget.getBandInfos();
    	ArrayList bandHotZones = widget.getBandHotZones();
    	ArrayList bandDecorators = widget.getBandDecorators();
    	EventSource eventSource = widget.getEventSource();

    	Theme theme = widget.getTheme();
        theme.setEventLabelWidth(EVENT_LABEL_WIDTH);
      
        
        bandHotZones.addAll(myHotZones);
               
        //NOTE! this sysout freezes FF recursive toString explosion
        //System.out.println("\n\n\n\n\nRENDER "+myHotZones.size()+"\n\n"+bandHotZones);
        
        
        // ---------------------------------------------------------------
        // Bands
        // ---------------------------------------------------------------
//        
//        BandOptions topOpts = BandOptions.create();
//        topOpts.setWidth("20%");
//        topOpts.setIntervalUnit(DateTime.MONTH());
//        topOpts.setIntervalPixels(100);
//        topOpts.setZones(bandHotZones);
//        topOpts.setShowEventText(true);
//        topOpts.setEventSource(eventSource);
//        topOpts.setTheme(theme);
//        topOpts.setDate("Jan 1 2007 00:00:00 GMT");
//
//        BandInfo top = BandInfo.create(topOpts);
//        top.setDecorators(bandDecorators);
//        bandInfos.add(top);

        BandOptions mainOpts = BandOptions.create();
        mainOpts.setWidth("85%");
        //mainOpts.setTrackHeight(.5f);
        //mainOpts.setTrackGap(0.2f);
        mainOpts.setIntervalUnit(DateTime.MONTH());
        mainOpts.setIntervalPixels(100);
        mainOpts.setShowEventText(true);
        mainOpts.setTheme(theme);
        mainOpts.setEventSource(eventSource);
        mainOpts.setDate("Jan 1 2007 00:00:00 GMT");

        mainOpts.setZones(bandHotZones);

        mainOpts.setTimeZone(0);

        BandInfo mainBand = BandInfo.createHotZone(mainOpts);
        mainBand.setDecorators(bandDecorators);
        bandInfos.add(mainBand);
        
        //mainBand.setSyncWith(0);
        mainBand.setHighlight(true);
        
        
        
        // Bands
        BandOptions bottomOpts = BandOptions.create();
        bottomOpts.setWidth("15%");
        bottomOpts.setTrackHeight(.2f);
        bottomOpts.setTrackGap(0.2f);
        bottomOpts.setIntervalUnit(DateTime.YEAR());
        bottomOpts.setIntervalPixels(200);
        bottomOpts.setShowEventText(false);
        bottomOpts.setTheme(theme);
        bottomOpts.setEventSource(eventSource);
        bottomOpts.setDate("Jan 1 2007 00:00:00 GMT");
        
        bottomOpts.setZones(bottomHotZones);
        
        bottomOpts.setTimeZone(0);

        BandInfo bottom = BandInfo.createHotZone(bottomOpts);
        bottom.setDecorators(bandDecorators);
        bandInfos.add(bottom);
        bottom.setSyncWith(0);
        bottom.setHighlight(true);
        
    }

    public void clearHotZones(){
    	myHotZones.clear();
    	bottomHotZones.clear();
    }
    /**
     *  
     *   HotZones, two events too close together so we are going to 
     *   'stretch' the time along them to make the gap between them
     *   larger but still maintain the flow of time.
     *   
     * @param date
     * @param howManyInThisMonth
     */
    public void addZone(Date date, int howManyInThisMonth) {	  
				
      
        HotZoneBandOptions topHotZoneOpts = HotZoneBandOptions.create();
        topHotZoneOpts.setStart(HippoTimeLine.getDateInJSON(date));
        
        HotZoneBandOptions bottomHotZoneOpts = HotZoneBandOptions.create();
        bottomHotZoneOpts.setStart(HippoTimeLine.getDateInJSON(date));
        
        System.out.println("new hot zone from "+HippoTimeLine.getDateInJSON(date)+" to ");
        
        //set to last day of the month by going to next month, 
        //then back 1 day (range is 1-31, so 0 is -1)
        date.setMonth(date.getMonth()+1);        
        date.setDate(0);
        topHotZoneOpts.setEnd(HippoTimeLine.getDateInJSON(date));
        bottomHotZoneOpts.setEnd(HippoTimeLine.getDateInJSON(date));
                
        System.out.println(HippoTimeLine.getDateInJSON(date)+" ! ");
        
        if(howManyInThisMonth > 40){
        	topHotZoneOpts.setMagnify(10);
        	bottomHotZoneOpts.setMagnify(5);
        }else{
        	topHotZoneOpts.setMagnify(5);
        	bottomHotZoneOpts.setMagnify(3);
        }
        
        topHotZoneOpts.setUnit(DateTime.WEEK());
        topHotZoneOpts.setMultiple(1);       
        
        bottomHotZoneOpts.setUnit(DateTime.MONTH());
        bottomHotZoneOpts.setMultiple(1);
        
        myHotZones.add(topHotZoneOpts);
        bottomHotZones.add(bottomHotZoneOpts);
        
        
	}

}
