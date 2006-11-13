package com.aavu.client.domain;

public class SearchResult {

	private long topicID;
	private String title;
	private String text;
	private float score;
		
	public SearchResult(long topicID, float score, String title, String text) {
		super();
		this.topicID = topicID;
		this.score = score;
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
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	public String toString(){
		return getTopicID()+" "+getScore()+" "+getTitle()+" "+getText();
	}
	
}
