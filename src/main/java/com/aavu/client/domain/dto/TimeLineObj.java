package com.aavu.client.domain.dto;

import java.util.Date;

import com.aavu.client.gui.timeline.HasDate;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TimeLineObj implements IsSerializable, Comparable {


	private static final long DIV = 60000;


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

	private HasDate date;


	private TopicIdentifier topic;



	public TimeLineObj() {
	}

	public TimeLineObj(TopicIdentifier topic, HasDate date) {
		this.topic = topic;
		this.date = date;
	}

	public int compareTo(Object o) {
		TimeLineObj tl = (TimeLineObj) o;
		return getStartDate().compareTo(tl.getStartDate());
	}

	public Date getEndDate() {
		return date.getEndDate();
	}

	/**
	 * 
	 * @return
	 */
	public HasDate getHasDate() {
		return date;
	}

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

	public Date getStartDate() {
		return date.getStartDate();
	}

	public TopicIdentifier getTopicIdentifier() {
		return topic;
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

	/**
	 * TODO instanceof
	 * 
	 * @param positionX
	 */
	public Date setEndDateToX(int positionX) {


		// System.out.println("pos: " + positionX + " " + getDateFromViewPanelX(positionX) );

		Date newD = getDateFromViewPanelX(positionX);
		if (newD.after(getStartDate())) {
			date.setEndDate(newD);
		}

		System.out.println("Eat endDateSet");

		// System.out.println("TLO: End Date = " + getEndDate());

		return date.getEndDate();

	}


	public Date setStartDateToX(int positionX) {

		// System.out.println("pos: " + positionX + " " + getDateFromViewPanelX(positionX) + " ");

		Date newD = getDateFromViewPanelX(positionX);

		date.setStartDate(newD);

		if (null != getEndDate() && newD.after(getEndDate())) {
			date.setEndDate(newD);
		}



		// System.out.println("TLO: Start Date = " + getStartDate());

		return date.getStartDate();
	}


	public String toString() {
		return topic.getTopicTitle() + " " + getStartDate() + " " + getEndDate();
	}
}
