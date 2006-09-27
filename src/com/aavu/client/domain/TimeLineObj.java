package com.aavu.client.domain;

import java.util.Date;

public class TimeLineObj {
	
	private TopicIdentifier topic;
	private Date start;
	private Date end;
		
	public TimeLineObj(TopicIdentifier topic, Date start, Date end) {
		super();
		this.topic = topic;
		this.start = start;
		this.end = end;
	}
	
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public TopicIdentifier getTopic() {
		return topic;
	}
	public void setTopic(TopicIdentifier topic) {
		this.topic = topic;
	}
	
	
}
