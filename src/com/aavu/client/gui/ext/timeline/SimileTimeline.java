package com.aavu.client.gui.ext.timeline;

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

	//@Override
	protected void onLoad() {
		nativeOnLoad(name);
	}
	private native void nativeOnLoad(String name)/*-{
	 //alert("wnd"+$wnd);
	 //alert($wnd.Timeline);
	 //alert("doc"+$doc);
	 //alert("doc"+$doc.Timeline);	 
	 var Timeline = $wnd.Timeline;
	 
//	 alert("timeline: "+Timeline);

	 var bandInfos = [
		    Timeline.createBandInfo({
		        width:          "70%", 
		        intervalUnit:   Timeline.DateTime.MONTH, 
		        intervalPixels: 100
		    }),
		    Timeline.createBandInfo({
		        width:          "30%", 
		        intervalUnit:   Timeline.DateTime.YEAR, 
		        intervalPixels: 200
		    })
		  ];		  
		  
		  bandInfos[1].syncWith = 0;
		  bandInfos[1].highlight = true;
		  tl = Timeline.create($doc.getElementById(name), bandInfos);
	}-*/;
	
	
}
