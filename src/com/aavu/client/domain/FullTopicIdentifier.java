package com.aavu.client.domain;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FullTopicIdentifier extends TopicIdentifier implements IsSerializable {

	private Date lastUpdated;
	private int latitude;
	private int longitude;
	
	public FullTopicIdentifier(){}
	
	public FullTopicIdentifier(long topicID, String topicTitle,Date lastUpdated, int latitude, int longitude) {
		super(topicID,topicTitle);
		this.lastUpdated = lastUpdated;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
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

	//@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + latitude;
		result = PRIME * result + longitude;
		return result;
	}

	//@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof FullTopicIdentifier))
			return false;
		final FullTopicIdentifier other = (FullTopicIdentifier) obj;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		return true;
	}
	
}
