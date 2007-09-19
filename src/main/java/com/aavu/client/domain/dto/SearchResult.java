package com.aavu.client.domain.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResult implements IsSerializable {

	private long topicID;
	private String title;
	private String text;
	private float score;
	private boolean publicVisible;

	public SearchResult() {
	}

	public SearchResult(long topicID, float score, String title, String text, boolean publicVisible) {
		super();
		this.topicID = topicID;
		this.score = score;
		this.title = title;
		this.text = text;
		this.publicVisible = publicVisible;
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

	public String toString() {
		return getTopicID() + " " + getScore() + " " + getTitle() + " " + getText();
	}

	public TopicIdentifier getTopicIdentifier() {
		return new TopicIdentifier(topicID, title, publicVisible);
	}

}
