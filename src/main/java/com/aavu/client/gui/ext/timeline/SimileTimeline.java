package com.aavu.client.gui.ext.timeline;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.SimplePanel;

public class SimileTimeline extends SimplePanel {

	private String name;
	
	public SimileTimeline(String name){
		this.name = name;
		
		Element e = DOM.createDiv();		
		
		DOM.setAttribute(e, "id", name);
				
		setElement(e);
	
		setStyleName("H-TimeLine");
	}

	
	public void load(JSONObject jo) {
		nativeLoad(name,jo.toString(),GWT.getModuleBaseURL());				
	}	
	
	
	public String getName() {
		return name;
	}

	private native void nativeLoad(String name,String jsonString,String baseURL)/*-{
	var jsonData = eval('(' + jsonString + ')')
	 //alert("wnd"+$wnd);
	 //alert($wnd.Timeline);
	 //alert("doc"+$doc);
	 //alert("doc"+$doc.Timeline);	 
	 var Timeline = $wnd.Timeline;	 
	 //alert("timeline: "+Timeline);

	 //alert("json "+jsonData);
	 //alert("json.events "+jsonData.events);
	 //alert("json.date "+jsonData.dateTimeFormat);
 	 var eventSource = new Timeline.DefaultEventSource();
	 
	 var bandInfos = [
		    Timeline.createBandInfo({
		        width:          "70%", 
		        intervalUnit:   Timeline.DateTime.MONTH, 
		        intervalPixels: 100,
		        eventSource:    eventSource,
        		date:           "Jan 1 2007 00:00:00 GMT"
		    }),
		    Timeline.createBandInfo({
		        width:          "30%", 
		        intervalUnit:   Timeline.DateTime.YEAR, 
		        intervalPixels: 200,
		        showEventText:  false,
        		trackHeight:    0.5,
        		trackGap:       0.2,
		        eventSource:    eventSource,
        		date:           "Jan 1 2007 00:00:00 GMT"
		    })
		  ];		  
    		  
		  bandInfos[1].syncWith = 0;
		  bandInfos[1].highlight = true;
	
		
		  tl = Timeline.create($doc.getElementById(name), bandInfos);		  
 	
 		  eventSource.loadJSON(jsonData, baseURL);    		  
	}-*/;

	private native void nativeLoadSpecial(String name,String jsonString,String baseURL)/*-{
	var jsonData = eval('(' + jsonString + ')')
	 //alert("wnd"+$wnd);
	 //alert($wnd.Timeline);
	 //alert("doc"+$doc);
	 //alert("doc"+$doc.Timeline);	 
	 var Timeline = $wnd.Timeline;	 
	 //alert("timeline: "+Timeline);

	 //alert("json "+jsonData);
	 //alert("json.events "+jsonData.events);
	 //alert("json.date "+jsonData.dateTimeFormat);
 	 var eventSource = new Timeline.DefaultEventSource();
	 
	 var bandInfos = [
	      
		    Timeline.createHotZoneBandInfo({
		    zones: [
            {   start:    "Feb 01 2007 00:00:00 GMT-0500",
                end:      "March 01 2007 00:00:00 GMT-0500",
                magnify:  5,
                unit:     Timeline.DateTime.WEEK
            }
        	],
		        width:          "70%", 
		        timeZone:       -5,
		        intervalUnit:   Timeline.DateTime.MONTH, 
		        intervalPixels: 100,
		        eventSource:    eventSource,
        		date:           "Jan 1 2007 00:00:00 GMT"
		    }),
		    Timeline.
		    createHotZoneBandInfo({
        		zones: [
            		{   start:    "Feb 01 2007 00:00:00 GMT-0500",
                		end:      "March 01 2007 00:00:00 GMT-0500",
                		magnify:  5,
                		unit:     Timeline.DateTime.WEEK
            		}
        		],		    		    
		        width:          "30%", 
		        showEventText:  false,
        		trackHeight:    0.5,
        		trackGap:       0.2,
		        intervalUnit:   Timeline.DateTime.YEAR, 
		        intervalPixels: 200,
		        eventSource:    eventSource,
        		date:           "Jan 1 2007 00:00:00 GMT"
		    })
		  ];		  
    		  
		  bandInfos[1].syncWith = 0;
		  bandInfos[1].highlight = true;
	
		
		  tl = Timeline.create($doc.getElementById(name), bandInfos);		  
 	
 		  eventSource.loadJSON(jsonData, baseURL);    		  
	}-*/;
	
	
}
