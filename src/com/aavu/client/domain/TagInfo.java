package com.aavu.client.domain;

public interface TagInfo {

	long getTagId();

	String getTagName();

	int getNumberOfTopics();

	int getLatitude();

	int getLongitude();

	void setLongitude(int i);

	void setLatitude(int i);

}
