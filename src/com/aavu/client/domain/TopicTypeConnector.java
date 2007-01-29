package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractTopicTypeConnector;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TopicTypeConnector extends AbstractTopicTypeConnector implements Serializable, IsSerializable {

	public TopicTypeConnector(){}

	public TopicTypeConnector(Topic topic, Topic type, double lat, double longi) {
		super(topic,type,lat,longi);
	}
	public TopicTypeConnector(Topic topic, Topic type) {
		super(topic,type,-1.0,-1.0);
	}
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
}
