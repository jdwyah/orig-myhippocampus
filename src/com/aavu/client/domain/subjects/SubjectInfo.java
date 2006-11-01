package com.aavu.client.domain.subjects;

/**
 * Class to represent one row of ancillary info for a Subject
 * 
 * 
 * @author Jeff Dwyer
 *
 */
public class SubjectInfo {
	
	private String type;
	private String value;
	/**
	 * Should we offer the option to make this a Topic
	 */
	private boolean topicable;
	
	public SubjectInfo(String type, String value, boolean topicable) {
		super();
		this.type = type;
		this.value = value;
		this.topicable = topicable;
	}

	public boolean isTopicable() {
		return topicable;
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
		return value;
	}
		
}
