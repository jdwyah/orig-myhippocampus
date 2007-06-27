package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractTopicOccurrenceConnector;


public class TopicOccurrenceConnector extends AbstractTopicOccurrenceConnector implements Serializable {

	
	public TopicOccurrenceConnector(){}

	public TopicOccurrenceConnector(Topic topic, Occurrence occurrence) {
		super(topic, occurrence, -1, -1);
	}


}