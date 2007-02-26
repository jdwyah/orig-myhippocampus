package com.aavu.client.gui.timeline;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.timeline.SimileTimeline;
import com.aavu.client.gui.timeline.renderers.HengerRender;
import com.aavu.client.gui.timeline.renderers.HippoRender;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.util.Logger;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.netthreads.gwt.simile.timeline.client.ITimeLineRender;
import com.netthreads.gwt.simile.timeline.client.TimeLineWidget;

public class HippoTimeLine extends Composite {


	private static final int WIDTH = 680;
	private static final int HEIGHT = 400;
		
	private TimeLineWidget simileWidget;
	
	private TopicCache topicCache;
	private TimeLineObj[] timeLinesObjs;
	
	
	private static int counter = 0;
	
	
	/**
	 * NOTE: you can't put a timeline in a TabPanel. Weird JS problem from the timeline.js
	 * 
	 * @param timeLinesObjs
	 */
	public HippoTimeLine(Manager manager,TimeLineObj[] timeLinesObjs){
		this(manager,timeLinesObjs,WIDTH,HEIGHT);
	}
	public HippoTimeLine(Manager manager,TimeLineObj[] timeLinesObjs,int width,int height){
		this.timeLinesObjs = timeLinesObjs;
		this.topicCache = manager.getTopicCache();
		
		
		
		//timeline = new SimileTimeline("foo_"+counter++);		
		

		ITimeLineRender render = new HippoRender();
		simileWidget = new TimeLineWidget("100%", "100%", render);
						
		initWidget(simileWidget);
	
	}


	//@Override
	protected void onLoad() {
		super.onLoad();
		//TODO BAD. we used to just process here, but GWM seems to have changed this.
		//If the simile jscript isn't loaded... bad.
		//3 second safety? hopefully that'll work.
		
		delayedLoad(timeLinesObjs);
		
		
		/*Timer t = new Timer(){
			public void run() {
				delayedLoad(timeLinesObjs);
			}};
		t.schedule(1000);*/
	}


	//@Override
	protected void delayedLoad(final TimeLineObj[] timeLinesObjs) {		
		
		Logger.log("HippoTimeline: onLoad()");		
		
		if(timeLinesObjs != null){
			
			load(timeLinesObjs);
			
		}else{

			topicCache.getTimelineObjs(new StdAsyncCallback("GetTimelineObjs"){

				public void onSuccess(Object result) {
					super.onSuccess(result);
					TimeLineObj[] timelines = (TimeLineObj[]) result;

//					for (Iterator iter = timelines.iterator(); iter.hasNext();) {
//					TimeLineObj tlo = (TimeLineObj) iter.next();
//					System.out.println("Received tlo "+tlo);
//					}

					load(timelines);

				}});
		}
	}

	protected void load(TimeLineObj[] timelines){
		
				
		for (int i = 0; i < timelines.length; i++) {
			TimeLineObj tlo = timelines[i];
			System.out.println("Received tlo "+tlo);
		}		
		
		JSONObject jo = new JSONObject();
		JSONArray events = new JSONArray();
		
		for (int i = 0; i < timelines.length; i++) {
			TimeLineObj tlo = timelines[i];			
			//System.out.println("C "+tlo.getJSONObj());
			events.set(i, tlo.getJSONObj());		
		}							
		
		jo.put("events",events);		
		jo.put("dateTimeFormat", new JSONString("iso8601"));
		
		Logger.log("Sending to simile: "+jo.toString());
		//timeline.load(jo);
		
		if(timelines.length == 0){
			Window.alert(Manager.myConstants.timeline_no_objs_msg());
		}
		
		
		
		


		//panel = new ScrollPanel();

		simileWidget.loadJSON(jo.toString());
		
	}
	


}
