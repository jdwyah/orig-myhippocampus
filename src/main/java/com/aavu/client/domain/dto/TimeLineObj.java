package com.aavu.client.domain.dto;

import java.util.Date;

import com.aavu.client.domain.Topic;
import com.aavu.client.gui.timeline.HasDate;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TimeLineObj implements IsSerializable, HasDate, Comparable {


	private TopicIdentifier topic;

	private Date start;

	private Date end;

	public TimeLineObj() {
	}

	public TimeLineObj(TopicIdentifier topic, Date start, Date end, Topic typeTopic) {
		this.topic = topic;
		this.start = start;
		this.end = end;
	}

	public Date getEnd() {
		return end;
	}

	public Date getStart() {
		return start;
	}

	public TopicIdentifier getTopicIdentifier() {
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

	public String toString() {
		return topic.getTopicTitle() + " " + getStart() + " " + getEnd();
	}

	public Date getDate() {
		return getStart();
	}


	private static final long DIV = 60000;

	/**
	 * convert time from seconds before 1970 to minutes. signed int goes to 2,147,483,648 == ~68
	 * Years in seconds doing it in minutes instead gives us 1970 +/- 4085 years so 2115 BC in
	 * minute precision. Probably enough for now.
	 * 
	 * @return
	 */
	public int getLeft() {
		return getLeftForDate(getStart());
	}

	public static Date getDateForLeft(int left) {
		return new Date(left * DIV);
	}

	public static int getLeftForDate(Date date) {
		long div = (date.getTime() / DIV);
		int left = (int) div;
		if (div != left) {
			System.out.println("TLO Fail" + (div == left) + "div " + div + " left " + left);
		}
		return left;
	}

	public static long scale(long d) {
		return d * DIV;
	}

	public int compareTo(Object o) {
		TimeLineObj tl = (TimeLineObj) o;
		return getDate().compareTo(tl.getDate());
	}
}
