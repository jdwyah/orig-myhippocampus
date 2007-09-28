package com.aavu.client.domain.commands;

import java.util.Set;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SaveTopicBasicCommand extends AbstractCommand implements IsSerializable {

	private int latitude = -1;
	private int longitude = -1;

	public SaveTopicBasicCommand() {
	};

	public SaveTopicBasicCommand(Topic topic, String title) {
		super(topic);
		setData(title);
	}

	public SaveTopicBasicCommand(Topic topic, int latitude, int longitude) {
		super(topic);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// @Override
	public void executeCommand() {
		if (getData() != null) {
			getTopic(0).setTitle(getData());
		} else {
			getTopic(0).setLatitude(latitude);
			getTopic(0).setLongitude(longitude);
		}
	}

	// @Override
	public String toString() {
		return "SaveTopicBasic ID " + getTopicID(0) + " " + getData() + " " + latitude + " "
				+ longitude;
	}

	// @Override
	public boolean updatesTitle() {
		return true;
	};



	/**
	 * update all tags that contain this topic
	 */
	// @Override
	public Set getAffectedTopics() {
		return getTopic(0).getTypesAsTopics();
	}



}
