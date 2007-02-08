package com.aavu.client.domain.commands;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.exception.HippoBusinessException;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Ok, here's how this works. We don't want to serialize the whole topic, send it to the 
 * server and then hope/pray/hack that things get saved right with respect to all of the
 * lazy loading / persistent set / CGLIB etc munging that we did on the way to the client.
 * 
 * Instead we implement our logic in commands. 
 * 
 * These nuggets have everything they need to affect the changes. 
 * We'll run them here on the client, to update our local state, but then we'll serialize
 * them and send them over to the server where they will be hydrated, run and saved.
 * 
 * Contract for the server is that it will hypdrate the 3 transient topics (if necessary)
 * with the id's in the three variables.
 * 
 * Again, this seems like a bunch of rigamorole, but it makes the object graph connecting/unconnecting
 * a non-issue, which is really nice. It's also very easy on the network connection.
 * 
 * @param topic
 * @param command
 * @param callback
 */
public abstract class AbstractSaveCommand implements IsSerializable {

	private long topicID;
	private long id1;
	private long id2;	
	private String data;
	
	protected transient Topic topic;
	protected transient Topic topic1;
	protected transient Topic topic2;
	
	public AbstractSaveCommand(){}

	public AbstractSaveCommand(Topic topic) {
		this(topic,null,null);	
	}
	public AbstractSaveCommand(Topic topic, Topic topic1) {
		this(topic,topic1,null);
	}
	public AbstractSaveCommand(Topic topic, Topic topic1,Topic topic2) {
		this.topic = topic;
		this.topic1 = topic1;
		this.topic2 = topic2;
		
		if(topic != null){
			setTopicID(topic.getId());
		}
		if(topic1 != null){
			setId1(topic1.getId());
		}
		if(topic2 != null){
			setId2(topic2.getId());
		}
	}

	public abstract void executeCommand() throws HippoBusinessException;
		
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
	}

	public boolean updatesTitle() {	
		return false;
	}

	public boolean affectedTag() {
		return false;		
	}
	public Tag getAffectedTag(){
		return null;
	}
	
	
}
