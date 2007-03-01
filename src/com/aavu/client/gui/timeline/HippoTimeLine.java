package com.aavu.client.gui.timeline;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.gui.ViewMemberWindow;
import com.aavu.client.gui.ext.PopupWindow;
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
import com.netthreads.gwt.simile.timeline.client.TimeLine;
import com.netthreads.gwt.simile.timeline.client.TimeLineClickListener;
import com.netthreads.gwt.simile.timeline.client.TimeLineWidget;

public class HippoTimeLine extends Composite implements TimeLineClickListener {


	private static final int WIDTH = 680;
	private static final int HEIGHT = 400;
		
	private TimeLineWidget simileWidget;
	
	private TopicCache topicCache;
	private TimeLineObj[] timeLinesObjs;
	private Manager manager;
	private CloseListener closeListener;
	
	
	private static int counter = 0;
	
	
	/**
	 * NOTE: you can't put a timeline in a TabPanel. Weird JS problem from the timeline.js
	 * 
	 * @param timeLinesObjs
	 */
	public HippoTimeLine(Manager manager,TimeLineObj[] timeLinesObjs){
		this(manager,timeLinesObjs,WIDTH,HEIGHT,null);
	}
	public HippoTimeLine(Manager manager,TimeLineObj[] timeLinesObjs,int width,int height, CloseListener close){
		this.timeLinesObjs = timeLinesObjs;
		this.topicCache = manager.getTopicCache();		
		this.manager = manager;
		this.closeListener = close;
		
		//timeline = new SimileTimeline("foo_"+counter++);		
		

		ITimeLineRender render = new HippoRender();
		System.out.println("SIZE "+width+" "+height);
		simileWidget = new TimeLineWidget(height+"px",width+"px", render);
						
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

	public void load(TimeLineObj[] timelines){
		
		simileWidget.clearData();
				
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
		
		//simileWidget.getTimeLine() is null for a while, so just keep circling back until it isn't
		Timer t = new Timer(){
			public void run() {
				if(setClickListener()){
					cancel();
				}else{
					scheduleRepeating(500);
				}
			}};
		t.schedule(100);
		
		
	}
	private boolean setClickListener() {
		TimeLine t = simileWidget.getTimeLine();
		if(t != null){
			t.setClickListener(this);
			return true;
		}
		return false;
	}
	
	
	public void onClick(int x, int y, String description) {
		
		System.out.println("HIPPO "+x+" "+y+" "+description);
		long id = Long.parseLong(description);
		
		closeListener.close();
		
		manager.bringUpChart(id);
	}
	


}
