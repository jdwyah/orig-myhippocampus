package com.aavu.client.domain.dto;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResult implements IsSerializable {

	private long id;
	private String title;
	private String text;
	private float score;
	private boolean publicVisible;
	private User user;

	public SearchResult() {
	}

	public SearchResult(Topic topic, TopicIdentifier parent, float score, final String highlightText) {
		this(topic, score, highlightText);
		setId(parent.getTopicID());
	}

	public SearchResult(Topic topic, float score, final String highlightText) {
		this.id = topic.getId();
		this.score = score;
		this.title = topic.getTitle();
		this.text = highlightText;
		this.publicVisible = topic.isPublicVisible();
		this.user = topic.getUser();
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

	public long getId() {
		return id;
	}

	public void setId(long topicID) {
		this.id = topicID;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public boolean isPublicVisible() {
		return publicVisible;
	}


	public void setPublicVisible(boolean publicVisible) {
		this.publicVisible = publicVisible;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public String toString() {
		return getId() + " " + getScore() + " " + getTitle() + " " + getText();
	}

	public TopicIdentifier getTopicIdentifier() {
		return new TopicIdentifier(id, title, publicVisible);
	}

}
