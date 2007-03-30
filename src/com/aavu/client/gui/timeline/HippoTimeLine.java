package com.aavu.client.gui.timeline;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwm.client.GInternalFrame;
import org.gwm.client.event.GFrameEvent;
import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.async.StdAsyncCallback;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.gui.ext.GUIEffects;
import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.glossary.SimpleTopicDisplay;
import com.aavu.client.gui.timeline.renderers.HippoRender;
import com.aavu.client.service.Manager;
import com.aavu.client.service.cache.TopicCache;
import com.aavu.client.strings.ConstHolder;
import com.aavu.client.util.Logger;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.netthreads.gwt.simile.timeline.client.TimeLine;
import com.netthreads.gwt.simile.timeline.client.TimeLineClickListener;
import com.netthreads.gwt.simile.timeline.client.TimeLineWidget;

public class HippoTimeLine extends Composite implements TimeLineClickListener {

	private static final String TZ = " 00:00:00 GMT-0600";
	private transient static SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy");
	public static String getDateInJSON(Date date) {
		return sdf.format(date)+TZ;
	}
	
	private static final int WIDTH = 680;
	private static final int HEIGHT = 400;
		
	private static final int MIN_RESIZE = 300;
	
	private TimeLineWidget simileWidget;
	
	private TopicCache topicCache;
	private TimeLineObj[] timeLinesObjs;
	private Manager manager;
	private CloseListener closeListener;
	private HippoRender render;
	private SimplePanel holder;
	private int width;
	private int height;
	
	
	private static int counter = 0;
	
	
	/**
	 * NOTE: you can't put a timeline in a TabPanel. Weird JS problem from the timeline.js
	 * 
	 * @param timeLinesObjs
	 */
	public HippoTimeLine(Manager manager){
		this(manager,WIDTH,HEIGHT,null);
	}
	public HippoTimeLine(Manager manager,int width,int height, CloseListener close){
		
		this.topicCache = manager.getTopicCache();		
		this.manager = manager;
		this.closeListener = close;
		this.width = width;
		this.height = height;
		
		//timeline = new SimileTimeline("foo_"+counter++);		
		

		render = new HippoRender();
		System.out.println("SIZE "+width+" "+height);
		
	
		holder = new SimplePanel();
		
		initWidget(holder);
		
	}


	//@Override
//	protected void onLoad() {
//		super.onLoad();
//		//TODO BAD. we used to just process here, but GWM seems to have changed this.
//		//If the simile jscript isn't loaded... bad.
//		//3 second safety? hopefully that'll work.
//		
//		delayedLoad(timeLinesObjs);
//		
//		
//		/*Timer t = new Timer(){
//			public void run() {
//				delayedLoad(timeLinesObjs);
//			}};
//		t.schedule(1000);*/
//	}


	//@Override
//	protected void delayedLoad(final TimeLineObj[] timeLinesObjs) {		
//		
//		Logger.log("HippoTimeline: onLoad()");		
//		
//		if(timeLinesObjs != null){
//			
//			load(timeLinesObjs);
//			
//		}else{
//
//			topicCache.getTimelineObjs(new StdAsyncCallback("GetTimelineObjs"){
//
//				public void onSuccess(Object result) {
//					super.onSuccess(result);
//					TimeLineObj[] timelines = (TimeLineObj[]) result;
//
////					for (Iterator iter = timelines.iterator(); iter.hasNext();) {
////					TimeLineObj tlo = (TimeLineObj) iter.next();
////					System.out.println("Received tlo "+tlo);
////					}
//
//					load(timelines);
//
//				}});
//		}
//	}

	public void load(List timelines){
				
		
		if(timelines == null){
			return;	
		}
		
		
		JSONObject jo = new JSONObject();
		JSONArray events = new JSONArray();
		
		//<Date,Integer>
		Map monthBucket = new HashMap();
		
		int i = 0;
		for (Iterator iter = timelines.iterator(); iter.hasNext();) {
			TimeLineObj tlo = (TimeLineObj) iter.next();
//			System.out.println("C "+tlo.getJSONObj());
			
			Date monthOf = new Date(tlo.getStart().getYear(),tlo.getStart().getMonth(),1);
			
			Integer cur = (Integer) monthBucket.get(monthOf);
			if(cur == null){
				monthBucket.put(monthOf, new Integer(1));
			}else{
				monthBucket.put(monthOf, new Integer(cur.intValue() + 1));
			}
			
			events.set(i, tlo.getJSONObj());	
			i++;
		}
		
		render.clearHotZones();
		for (Iterator iter = monthBucket.keySet().iterator(); iter.hasNext();) {
			Date date = (Date) iter.next();
			Integer numberOfTimePoints = (Integer) monthBucket.get(date);
			
			System.out.println("checking bucket "+date+" "+numberOfTimePoints);
			
			if(numberOfTimePoints.intValue() > 10){
				System.out.println("adding zone ");
				render.addZone(date,numberOfTimePoints.intValue());
			}
		}
		//render.thing();	
		
		jo.put("events",events);		
		jo.put("dateTimeFormat", new JSONString("iso8601"));
		
		Logger.log("Sending to simile: "+jo.toString());
		//timeline.load(jo);
		
		/*if(timelines.size() == 0){
			Window.alert(ConstHolder.myConstants.timeline_no_objs_msg());
		}*/
		
		simileWidget = new TimeLineWidget(height+"px",width+"px", render);	
				
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
		
		
		holder.setWidget(simileWidget);
		
	}
	private boolean setClickListener() {
		
		TimeLine t = simileWidget.getTimeLine();
		if(t != null){
			t.setClickListener(this);
			return true;
		}
		return false;
	}
	
	
	public void onClick(final int x,final int y, final String description) {
		
		System.out.println("HIPPO "+x+" "+y+" "+description);
		long id = Long.parseLong(description);
		
		
		showPreview(id,x,y);
		
//		closeListener.close();
//		
//		manager.bringUpChart(id);
	}
	
	private void showPreview(final long id, final int x, final int y){
		manager.getTopicCache().getTopicByIdA(id, new StdAsyncCallback("Preview"){
			public void onSuccess(Object result) {
				super.onSuccess(result);

				Topic topic = (Topic) result;				
				
				PreviewPopup window = new PreviewPopup(manager.newFrame(),topic,y,x);
				
				System.out.println("SHOWING POPUP at "+x+" "+y);
			}});
	}
	
	private class PreviewPopup extends PopupWindow {

		private static final int WIDTH = 400;
		private static final int HEIGHT = 200;
				
		public PreviewPopup(GInternalFrame frame,Topic topic,int top,int left) {
			super(frame,topic.getTitle(),WIDTH,HEIGHT);
			SimpleTopicDisplay display = new SimpleTopicDisplay(topic,manager,this);		
			frame.setLocation(top, left);
			setContent(display);
		}		
	}
	
	
	public void resize(GFrameEvent evt) {
		if(simileWidget != null){
			
			int newWidth = evt.getGFrame().getWidth() - 30;
			int newHeight = evt.getGFrame().getHeight();

			System.out.println("HippoTimeline RSIZE "+newWidth+" "+newHeight);

			newWidth = newWidth > MIN_RESIZE ? newWidth : MIN_RESIZE;
			newHeight = newHeight > MIN_RESIZE ? newHeight : MIN_RESIZE;
			
			simileWidget.setWidth(newWidth + "px");
			simileWidget.setHeight(newHeight + "px");         
			simileWidget.layout();
		}
	}
	


}
