package com.aavu.client.gui.timeline.renderers;

import java.util.ArrayList;

import com.netthreads.gwt.simile.timeline.client.BandInfo;
import com.netthreads.gwt.simile.timeline.client.BandOptions;
import com.netthreads.gwt.simile.timeline.client.DateTime;
import com.netthreads.gwt.simile.timeline.client.EventSource;
import com.netthreads.gwt.simile.timeline.client.HotZoneBandOptions;
import com.netthreads.gwt.simile.timeline.client.ITimeLineRender;
import com.netthreads.gwt.simile.timeline.client.PointHighlightDecorator;
import com.netthreads.gwt.simile.timeline.client.PointHighlightDecoratorOptions;
import com.netthreads.gwt.simile.timeline.client.SpanHighlightDecorator;
import com.netthreads.gwt.simile.timeline.client.SpanHighlightDecoratorOptions;
import com.netthreads.gwt.simile.timeline.client.Theme;
import com.netthreads.gwt.simile.timeline.client.TimeLineWidget;


/**
 * Render timeline
 * 
 */
public class HippoRender implements ITimeLineRender
{
    private static final int EVENT_LABEL_WIDTH = 200;

	/**
     * Create timeline custom elements.
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
                
        
        // ---------------------------------------------------------------
        // HotZones, two events too close together so we are going to 
        // 'stretch' the time along them to make the gap between them
        // larger but still maintain the flow of time.
        // ---------------------------------------------------------------
        HotZoneBandOptions hotZone1Opts = HotZoneBandOptions.create();
        hotZone1Opts.setStart("2280 BC");
        hotZone1Opts.setEnd("1930 BC");
        hotZone1Opts.setMagnify(2);
        hotZone1Opts.setUnit(DateTime.CENTURY());
        hotZone1Opts.setMultiple(2);
        bandHotZones.add(hotZone1Opts);
        
        
        
        // ---------------------------------------------------------------
        // Bands
        // ---------------------------------------------------------------
        
        BandOptions topOpts = BandOptions.create();
        topOpts.setWidth("70%");
        topOpts.setIntervalUnit(DateTime.MONTH());
        topOpts.setIntervalPixels(100);
        
        topOpts.setShowEventText(true);
        topOpts.setEventSource(eventSource);
        topOpts.setTheme(theme);
        topOpts.setDate("Jan 1 2007 00:00:00 GMT");

        BandInfo top = BandInfo.create(topOpts);
        top.setDecorators(bandDecorators);
        bandInfos.add(top);

        // Bands
        BandOptions bottomOpts = BandOptions.create();
        bottomOpts.setWidth("30%");
        bottomOpts.setTrackHeight(.5f);
        bottomOpts.setTrackGap(0.2f);
        bottomOpts.setIntervalUnit(DateTime.YEAR());
        bottomOpts.setIntervalPixels(200);
        bottomOpts.setShowEventText(false);
        bottomOpts.setTheme(theme);
        bottomOpts.setEventSource(eventSource);
        bottomOpts.setDate("Jan 1 2007 00:00:00 GMT");
        bottomOpts.setZones(bandHotZones);
        bottomOpts.setTimeZone(0);

        BandInfo bottom = BandInfo.createHotZone(bottomOpts);
        bottom.setDecorators(bandDecorators);
        bandInfos.add(bottom);

        bottom.setSyncWith(0);
        bottom.setHighlight(true);
    }
    
}
