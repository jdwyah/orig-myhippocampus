package com.aavu.client.gui.timeline;

import java.util.List;

import com.aavu.client.gui.ext.PopupWindow;
import com.aavu.client.gui.ext.timeline.SimileTimeline;
import com.aavu.client.service.Manager;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class HippoTimeLine extends PopupWindow {

	private SimileTimeline timeline;

	/**
	 * 
	 * @param timeLinesObjs
	 */
	public HippoTimeLine(Manager manager,List timeLinesObjs){
		super(manager.myConstants.timeline());

		timeline = new SimileTimeline("foo");

		setContentPanel(timeline);				
	}


	//@Override
	protected void onLoad() {		

		JSONObject jo = new JSONObject();
		JSONArray events = new JSONArray();

		for(int i=0;i<5;i++){
			events.set(i,getTimeObj(i));
		}
				
		jo.put("events",events);
		jo.put("dateTimeFormat", new JSONString("iso8601"));
		
		timeline.load(jo);

	}

	/*
	 * 		{
	 *		'dateTimeFormat': 'iso8601',
	 *		'events' : [
	 *		        {'start': '1924',
	 *		        'title': 'Barfusserkirche',
	 *		        'description': 'by Lyonel Feininger, American/German Painter, 1871-1956',
	 *		        'image': 'http://images.allposters.com/images/AWI/NR096_b.jpg',
	 *		        'link': 'http://www.allposters.com/-sp/Barfusserkirche-1924-Posters_i1116895_.htm'
	 *		        },
	 *
	 */
	private JSONObject getTimeObj(int i){
		JSONObject jo = new JSONObject();

		int d = 2004+i;

		jo.put("start", new JSONString(d+""));
		jo.put("title", new JSONString("Title "+i));
		jo.put("description", new JSONString("blah blah"));

		return jo;
	}


}
