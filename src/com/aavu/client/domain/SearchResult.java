package com.aavu.client.domain;

public class SearchResult {

	private long topicID;
	private String title;
	private String text;
		
	public SearchResult(long topicID, String title, String text) {
		super();
		this.topicID = topicID;
		this.title = title;
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getTopicID() {
		return topicID;
	}
	public void setTopicID(long topicID) {
		this.topicID = topicID;
	}
	
	
	
}
