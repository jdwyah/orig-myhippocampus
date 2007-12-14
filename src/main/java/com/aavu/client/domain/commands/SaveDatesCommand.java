package com.aavu.client.domain.commands;

import java.io.Serializable;
import java.util.Date;

import com.aavu.client.domain.Topic;

public class SaveDatesCommand extends AbstractCommand implements Serializable {

	private Date startDate;
	private Date endDate;

	public SaveDatesCommand() {
	};

	public SaveDatesCommand(Topic topic, Date startDate) {
		this(topic, startDate, null);
	}

	public SaveDatesCommand(Topic topic, Date startDate, Date endDate) {
		super(topic);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	// @Override
	public void executeCommand() {

		getTopic(0).setCreated(startDate);
		if (getTopic(0).usesLastUpdated()) {
			getTopic(0).setLastUpdated(endDate);
		}
	}

	// @Override
	public String toString() {
		return "SaveDatesCommand ID " + getTopicID(0) + " " + startDate + " " + endDate;
	}



}
