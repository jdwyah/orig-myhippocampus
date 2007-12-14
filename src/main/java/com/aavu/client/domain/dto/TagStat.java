package com.aavu.client.domain.dto;

import java.io.Serializable;

import com.aavu.client.domain.TagInfo;
import com.aavu.client.domain.Topic;

public class TagStat implements Serializable, TagInfo {

	private long tagId;
	private int numberOfTopics;
	private String tagName;
	private int latitude;
	private int longitude;
	private boolean publicVisible;

	public TagStat() {
	}

	public TagStat(Topic t) {
		super();
		this.tagId = t.getId();
		this.numberOfTopics = t.getTypes().size();
		this.tagName = t.getTitle();
		this.latitude = t.getLatitude();
		this.longitude = t.getLongitude();
		this.publicVisible = t.isPublicVisible();
	}

	public TagStat(long tagId, int numberOfTopics, String tagName, int latitude, int longitude,
			boolean publicVisible) {
		super();
		this.tagId = tagId;
		this.numberOfTopics = numberOfTopics;
		this.tagName = tagName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.publicVisible = publicVisible;
	}



	public int getNumberOfTopics() {
		return numberOfTopics;
	}

	public void setNumberOfTopics(int numberOfTopics) {
		this.numberOfTopics = numberOfTopics;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}



	public TopicIdentifier getTopicIdentifier() {
		return new TopicIdentifier(tagId, tagName, publicVisible);
	}



}
