package com.aavu.client.domain.dto;

import com.aavu.client.domain.TagInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TagStat implements IsSerializable, TagInfo {
	
	private long  tagId;
	private int numberOfTopics;
	private String tagName;
	private int latitude;
	private int longitude;
	
	public TagStat(){}
	
	

	public TagStat(long tagId, int numberOfTopics, String tagName, int latitude, int longitude) {
		super();
		this.tagId = tagId;
		this.numberOfTopics = numberOfTopics;
		this.tagName = tagName;
		this.latitude = latitude;
		this.longitude = longitude;
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

	
	
	
	
}
