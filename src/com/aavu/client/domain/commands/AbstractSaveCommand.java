package com.aavu.client.domain.commands;

import com.aavu.client.domain.Topic;
import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class AbstractSaveCommand implements IsSerializable {

	private long topicID;
	private long id1;
	private long id2;	
	private String data;
	
	protected transient Topic topic;
	protected transient Topic topic1;
	protected transient Topic topic2;
	
	public AbstractSaveCommand(){}

	public abstract void executeCommand();
	
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public long getId1() {
		return id1;
	}

	public void setId1(long id1) {
		this.id1 = id1;
	}

	public long getId2() {
		return id2;
	}

	public void setId2(long id2) {
		this.id2 = id2;
	}

	public long getTopicID() {
		return topicID;
	}

	public void setTopicID(long topicID) {
		this.topicID = topicID;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Topic getTopic1() {
		return topic1;
	}

	public void setTopic1(Topic topic1) {
		this.topic1 = topic1;
	}

	public Topic getTopic2() {
		return topic2;
	}

	public void setTopic2(Topic topic2) {
		this.topic2 = topic2;
	};
	
	
	
}
