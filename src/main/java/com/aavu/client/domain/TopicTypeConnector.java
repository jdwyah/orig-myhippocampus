package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractTopicTypeConnector;

public class TopicTypeConnector extends AbstractTopicTypeConnector implements Serializable {

	public TopicTypeConnector() {
	}

	public TopicTypeConnector(Topic topic, Topic type, int lat, int longi) {
		super(topic, type, lat, longi);
	}

	// public TopicTypeConnector(Topic topic, Topic type) {
	// super(topic,type,-1.0,-1.0);
	// }
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	// @Override
	public String toString() {
		return getId() + " TTC top " + getTopic() + " " + getTopic().getId() + " type " + getType()
				+ " " + getType().getId();
	}

}
