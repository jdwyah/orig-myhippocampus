package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractTopicOccurrenceConnector;


public class TopicOccurrenceConnector extends AbstractTopicOccurrenceConnector implements Serializable {

	
	public TopicOccurrenceConnector(){}

	public TopicOccurrenceConnector(Topic topic, Occurrence link) {
		super(topic, link, -1, -1);
	}


}