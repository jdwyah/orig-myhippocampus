package com.aavu.client.domain;


import com.google.gwt.user.client.rpc.IsSerializable;

public class TopicIdentifier implements IsSerializable, Comparable {

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

	/**
	 * no compartToIgnoreCase in GWT String impl
	 */
	public int compareTo(Object o) {
		if (o instanceof TopicIdentifier) {
			TopicIdentifier other = (TopicIdentifier) o;
			return getTopicTitle().toLowerCase().compareTo(other.getTopicTitle().toLowerCase());
		}if (o instanceof String) {						
			return getTopicTitle().toLowerCase().compareTo(((String) o).toLowerCase() );
		}else{
			throw new UnsupportedOperationException();
		}		
	}
}
