package com.aavu.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TopicIdentifier implements IsSerializable {

	private long topicID;
	private String topicTitle;
	
	public TopicIdentifier(){
	}
	
	public TopicIdentifier(long topicID, String topicTitle) {
		super();
		this.topicID = topicID;
		this.topicTitle = topicTitle;
	}
	public long getTopicID() {
		return topicID;
	}
	public void setTopicID(long topicID) {
		this.topicID = topicID;
	}
	public String getTopicTitle() {
		return topicTitle;
	}
	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}
	
	public String toString(){
		return getTopicID()+" "+getTopicTitle();
	}
}
