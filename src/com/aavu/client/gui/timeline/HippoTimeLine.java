package com.aavu.client.gui.timeline;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.timeline.SimileTimeline;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class HippoTimeLine extends PopupWindow {

	private SimileTimeline timeline;
	private TopicCache topicCache;

	/**
	 * 
	 * @param timeLinesObjs
	 */
	public HippoTimeLine(Manager manager,List timeLinesObjs){
		super(manager.myConstants.timeline());
		this.topicCache = manager.getTopicCache();
		
		timeline = new SimileTimeline("foo");

		setContentPanel(timeline);				
	}


	//@Override
	protected void onLoad() {		
		
		topicCache.getTimelineObjs(new StdAsyncCallback("GetTimelineObjs"){

			public void onSuccess(Object result) {
				List timelines = (List) result;
				
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
				
				System.out.println(jo.toString());
				timeline.load(jo);
				
			}});

	

	}

	


}
