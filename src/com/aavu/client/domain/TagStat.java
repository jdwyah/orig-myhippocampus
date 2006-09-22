package com.aavu.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TagStat implements IsSerializable{
	
	private Long  tagId;
	private Integer numberOfTopics;
		
	public TagStat(Long tagId, Integer numberOfTopics) {
		super();
		this.tagId = tagId;
		this.numberOfTopics = numberOfTopics;
	}
	public Integer getNumberOfTopics() {
		return numberOfTopics;
	}
	public void setNumberOfTopics(Integer numberOfTopics) {
		this.numberOfTopics = numberOfTopics;
	}
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
	
}
