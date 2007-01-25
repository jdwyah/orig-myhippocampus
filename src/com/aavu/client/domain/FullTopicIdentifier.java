package com.aavu.client.domain;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class FullTopicIdentifier extends TopicIdentifier implements IsSerializable {

	private Date lastUpdated;
	private double latitude;
	private double longitude;
	
	public FullTopicIdentifier(){}
	
	public FullTopicIdentifier(long topicID, String topicTitle,Date lastUpdated, float latitude, float longitude) {
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double d) {
		this.longitude = d;
	}



	//@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());		
		result = PRIME * result + (int) (latitude * 1000);		
		result = PRIME * result + (int) (longitude * 1000);
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
		if (lastUpdated == null) {
			if (other.lastUpdated != null)
				return false;
		} else if (!lastUpdated.equals(other.lastUpdated))
			return false;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		return true;
	}

	
	
}
