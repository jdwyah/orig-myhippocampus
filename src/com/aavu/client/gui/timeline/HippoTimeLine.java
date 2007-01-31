package com.aavu.client.gui.timeline;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.timeline.SimileTimeline;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.util.Logger;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class HippoTimeLine extends PopupWindow {

	private static final int HEIGHT = 400;
	private static final int WIDTH = 600;
	private SimileTimeline timeline;
	private TopicCache topicCache;


	/**
	 * 
	 * @param timeLinesObjs
	 */
	public HippoTimeLine(Manager manager,List timeLinesObjs){
		super(manager.newFrame(),manager.myConstants.timeline(),WIDTH,HEIGHT);
		this.topicCache = manager.getTopicCache();
		
		timeline = new SimileTimeline("foo");
		
		timeline.setPixelSize(WIDTH, HEIGHT);

		setContent(timeline);			
		
		//TODO BAD. we used to get the real onLoad() event, but GWM seems to have changed this.
		//If the simile jscript isn't loaded... bad.
		//3 second safety? hopefully that'll work.
		Timer t = new Timer(){
			public void run() {
				onLoad();
			}};
		t.schedule(3000);
	}


	//@Override
	protected void onLoad() {		
		
		Logger.log("HippoTimeline: onLoad()");
		
		topicCache.getTimelineObjs(new StdAsyncCallback("GetTimelineObjs"){

			public void onSuccess(Object result) {
				super.onSuccess(result);
				List timelines = (List) result;
				
//				for (Iterator iter = timelines.iterator(); iter.hasNext();) {
//					TimeLineObj tlo = (TimeLineObj) iter.next();
//					System.out.println("Received tlo "+tlo);
//				}
				
				
				JSONObject jo = new JSONObject();
				JSONArray events = new JSONArray();

				int i =0;
				for (Iterator iter = timelines.iterator(); iter.hasNext();) {
					TimeLineObj element = (TimeLineObj) iter.next();					
					events.set(i, element.getJSONObj());
					i++;
				}							
						
				jo.put("events",events);
				jo.put("dateTimeFormat", new JSONString("iso8601"));
				
				Logger.log("Sending to simile: "+jo.toString());
				timeline.load(jo);
				
				if(timelines.isEmpty()){
				Window.alert(Manager.myConstants.timeline_no_objs_msg());
				}
			}});

	

	}

	


}
