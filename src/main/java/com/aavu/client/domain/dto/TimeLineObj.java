package com.aavu.client.domain.dto;

import java.util.Date;

import com.aavu.client.domain.HippoDate;
import com.aavu.client.gui.timeline.HasDate;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TimeLineObj implements IsSerializable, HasDate, Comparable {


	private TopicIdentifier topic;


	private HasDate date;

	public TimeLineObj() {
	}

	public TimeLineObj(TopicIdentifier topic, HasDate date) {
		this.topic = topic;
		this.date = date;
	}



	public TopicIdentifier getTopicIdentifier() {
		return topic;
	}

	// public void setEnd(Date end) {
	// this.end = end;
	// }
	//
	// public void setStart(Date start) {
	// this.start = start;
	// }


	public void setTopic(TopicIdentifier topic) {
		this.topic = topic;
	}

	public String toString() {
		return topic.getTopicTitle() + " " + getStartDate() + " " + getEndDate();
	}

	public Date getStartDate() {
		return date.getStartDate();
	}

	public Date getEndDate() {
		return date.getEndDate();
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
		return getLeftForDate(getStartDate());
	}

	public static Date getDateFromViewPanelX(int viewPanelX) {
		long asLong = viewPanelX * DIV;
		return new Date(asLong);
	}

	public static int getLeftForDate(Date date) {
		long div = (date.getTime() / DIV);
		int left = (int) div;
		if (div != left) {
			System.out.println("TLO Fail" + (div == left) + "div " + div + " left " + left);
		}
		return left;
	}

	public int getWidth() {
		if (getEndDate() == null) {
			return 10;
		} else {
			System.out.println("TLO.Width " + getLeftForDate(getEndDate()) + " "
					+ getLeftForDate(getStartDate()) + " End " + getEndDate() + " start "
					+ getStartDate());
			return getLeftForDate(getEndDate()) - getLeftForDate(getStartDate());
		}
	}


	public int compareTo(Object o) {
		TimeLineObj tl = (TimeLineObj) o;
		return getStartDate().compareTo(tl.getStartDate());
	}

	/**
	 * TLO's created from a basic Topic are not maleable. Only allow changes if created with a
	 * HippoDate obj
	 * 
	 * TODO rethink this a bit. Maybe another interface
	 */
	public boolean isMaleable() {
		return date instanceof HippoDate;
	}

	/**
	 * 
	 * @return
	 */
	public HasDate getHasDate() {
		return date;
	}

	public void setStartDateToX(int positionX) {
		if (date instanceof HippoDate) {
			HippoDate hdate = (HippoDate) date;
			System.out.println("pos: " + positionX + " " + getDateFromViewPanelX(positionX) + " ");

			Date newD = getDateFromViewPanelX(positionX);
			if (newD.before(getEndDate())) {
				hdate.setStartDate(newD);
			}

		} else {
			throw new UnsupportedOperationException();
		}


		System.out.println("TLO: Start Date = " + getStartDate());
	}

	public void setEndDateToX(int positionX) {
		if (date instanceof HippoDate) {
			HippoDate hdate = (HippoDate) date;
			System.out.println("pos: " + positionX + " " + getDateFromViewPanelX(positionX) + " ");

			Date newD = getDateFromViewPanelX(positionX);
			if (newD.after(getStartDate())) {
				hdate.setEndDate(newD);
			}
		} else {
			throw new UnsupportedOperationException();
		}
		System.out.println("TLO: End Date = " + getEndDate());
	}
}
