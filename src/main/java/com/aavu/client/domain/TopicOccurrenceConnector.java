package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractTopicOccurrenceConnector;


public class TopicOccurrenceConnector extends AbstractTopicOccurrenceConnector implements
		Serializable {


	public TopicOccurrenceConnector() {
	}

	public TopicOccurrenceConnector(Topic topic, Occurrence occurrence) {
		this(topic, occurrence, -1, -1);
	}

	public TopicOccurrenceConnector(Topic topic, Occurrence occurrence, int lat, int lng) {
		super(topic, occurrence, lat, lng);
	}

	public String toString() {
		String tstr = getTopic() == null ? "null" : getTopic().getTitle();
		String ostr = getOccurrence() == null ? "null" : getOccurrence().getTitle();
		return "TOC Topic " + tstr + " Occ " + ostr;
	}
}