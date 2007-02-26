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
public class HengerRender implements ITimeLineRender
{
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
        theme.setEventLabelWidth(400);
        
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
        // Band Decorators
        // ---------------------------------------------------------------
        
        // Phase 1
        SpanHighlightDecoratorOptions phase1Opts = SpanHighlightDecoratorOptions.create();
        phase1Opts.setStartDate("2900 BC");
        phase1Opts.setEndDate("2700 BC");
        phase1Opts.setColor("FFC080");
        phase1Opts.setOpacity(50);
        phase1Opts.setStartLabel("Phase");
        phase1Opts.setEndLabel("");
        phase1Opts.setTheme(theme);
        SpanHighlightDecorator phase1decorator = SpanHighlightDecorator.create(phase1Opts);
        bandDecorators.add(phase1decorator);        

        // Phase 2
        SpanHighlightDecoratorOptions phase2Opts = SpanHighlightDecoratorOptions.create();
        phase2Opts.setStartDate("2900 BC");
        phase2Opts.setEndDate("2400 BC");
        phase2Opts.setColor("#FFC08F");
        phase2Opts.setOpacity(50);
        phase2Opts.setStartLabel("");
        phase2Opts.setEndLabel("");
        phase2Opts.setTheme(theme);
        SpanHighlightDecorator phase2decorator = SpanHighlightDecorator.create(phase2Opts);
        bandDecorators.add(phase2decorator);        

        // Phase 3
        SpanHighlightDecoratorOptions phase3Opts = SpanHighlightDecoratorOptions.create();
        phase3Opts.setStartDate("2600 BC");
        phase3Opts.setEndDate("1600 BC");
        phase3Opts.setColor("#FFC000");
        phase3Opts.setOpacity(50);
        phase3Opts.setStartLabel("");
        phase3Opts.setEndLabel("");
        phase3Opts.setTheme(theme);
        SpanHighlightDecorator phase3decorator = SpanHighlightDecorator.create(phase3Opts);
        bandDecorators.add(phase3decorator);        
        
        PointHighlightDecoratorOptions startPointOpts = PointHighlightDecoratorOptions.create();
        startPointOpts.setDate("4000 BC");
        startPointOpts.setColor("#FFC080");
        startPointOpts.setOpacity(50);
        startPointOpts.setTheme(theme);
        PointHighlightDecorator startPointDecorator = PointHighlightDecorator.create(startPointOpts); 
        bandDecorators.add(startPointDecorator);
        

        PointHighlightDecoratorOptions endPointOpts = PointHighlightDecoratorOptions.create();
        endPointOpts.setDate("1600 BC");
        endPointOpts.setColor("#FFC080");
        endPointOpts.setOpacity(50);
        endPointOpts.setTheme(theme);
        PointHighlightDecorator endPointDecorator = PointHighlightDecorator.create(endPointOpts); 
        bandDecorators.add(endPointDecorator);
        
        // ---------------------------------------------------------------
        // Bands
        // ---------------------------------------------------------------
        
        BandOptions topOpts = BandOptions.create();
        topOpts.setWidth("5%");
        topOpts.setIntervalUnit(DateTime.CENTURY());
        topOpts.setIntervalPixels(200);
        topOpts.setShowEventText(false);
        topOpts.setTheme(theme);
        topOpts.setDate("3701 BC");

        BandInfo top = BandInfo.create(topOpts);
        top.setDecorators(bandDecorators);
        bandInfos.add(top);

        // Bands
        BandOptions bottomOpts = BandOptions.create();
        bottomOpts.setWidth("95%");
        bottomOpts.setTrackHeight(1.3f);
        bottomOpts.setTrackGap(0.1f);
        bottomOpts.setIntervalUnit(DateTime.CENTURY());
        bottomOpts.setIntervalPixels(50);
        bottomOpts.setShowEventText(true);
        bottomOpts.setTheme(theme);
        bottomOpts.setEventSource(eventSource);
        bottomOpts.setDate("3701 BC");
        bottomOpts.setZones(bandHotZones);
        bottomOpts.setTimeZone(0);

        BandInfo bottom = BandInfo.createHotZone(bottomOpts);
        bottom.setDecorators(bandDecorators);
        bandInfos.add(bottom);

        bottom.setSyncWith(0);
        bottom.setHighlight(true);
    }
    
}
