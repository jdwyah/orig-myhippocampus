package com.aavu.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TagStat implements IsSerializable {
	
	private long  tagId;
	private int numberOfTopics;
	private String tagName;
	
	public TagStat(){}
	
	public TagStat(long tagId,String tagName, int numberOfTopics) {
		super();
		this.tagId = tagId;
		this.numberOfTopics = numberOfTopics;
		this.tagName = tagName;
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
	
	
	
}
