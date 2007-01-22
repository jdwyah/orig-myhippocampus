package com.aavu.client.domain;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FullTopicIdentifier extends TopicIdentifier implements IsSerializable {

	private Date lastUpdated;
	
	public FullTopicIdentifier(){}
	
	public FullTopicIdentifier(long topicID, String topicTitle,Date lastUpdated) {
		super(topicID,topicTitle);
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
}
