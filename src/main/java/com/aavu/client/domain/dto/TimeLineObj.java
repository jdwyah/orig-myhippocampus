package com.aavu.client.domain.dto;

import java.util.Date;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.gui.timeline.HasDate;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TimeLineObj implements IsSerializable, HasDate {
	
	private transient static final DateTimeFormat sdf = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	public static String getDateInJSON(Date date) {
		return sdf.format(date);
	}
	private TopicIdentifier topic;
		
	private Date start;
	
	private Date end;
	
	public TimeLineObj(){}
	
	public TimeLineObj(TopicIdentifier topic, Date start, Date end) {
		this.topic = topic;
		this.start = start;
		this.end = end;
	}
	public Date getEnd() {
		return end;
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
	public JSONValue getJSONObj() {
		JSONObject jo = new JSONObject();

		jo.put("start", new JSONString(getDateInJSON(getStart())));
		jo.put("title", new JSONString(topic.getTopicTitle()));
		jo.put("description", new JSONString(getTopic().getTopicID()+""));
		jo.put("link", new JSONString("HippoTest.html#"+getTopic().getTopicID()));

		return jo;
	}
	public Date getStart() {
		return start;
	}
	public TopicIdentifier getTopic() {
		return topic;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
	public void setStart(Date start) {
		this.start = start;
	}

	
	public void setTopic(TopicIdentifier topic) {
		this.topic = topic;
	}
	public String toString(){
		return topic.getTopicTitle()+" "+getStart()+" "+getEnd();
	}

	public Date getDate() {
		return getStart();
	}
}
