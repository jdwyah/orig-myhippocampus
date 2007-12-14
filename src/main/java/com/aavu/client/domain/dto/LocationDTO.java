package com.aavu.client.domain.dto;

import java.io.Serializable;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.MetaLocation;

public class LocationDTO implements Serializable {

	private TopicIdentifier topic;
	private HippoLocation location;
	private MetaLocation meta;

	public LocationDTO() {
	}

	public LocationDTO(TopicIdentifier topic, HippoLocation location, MetaLocation meta) {
		super();
		this.topic = topic;
		this.location = location;
		this.meta = meta;
	}

	public HippoLocation getLocation() {
		return location;
	}

	public void setLocation(HippoLocation location) {
		this.location = location;
	}

	public MetaLocation getMeta() {
		return meta;
	}

	public void setMeta(MetaLocation meta) {
		this.meta = meta;
	}

	public TopicIdentifier getTopic() {
		return topic;
	}

	public void setTopic(TopicIdentifier topic) {
		this.topic = topic;
	}

	public String getOnMapTitle() {
		return topic.getTopicTitle() + " " + getMeta().getTitle() + " " + getLocation().getTitle();
	}


}
