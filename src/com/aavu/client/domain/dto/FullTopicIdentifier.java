package com.aavu.client.domain.dto;

import java.util.Date;

import com.aavu.client.domain.TopicTypeConnector;
import com.google.gwt.user.client.rpc.IsSerializable;

public class FullTopicIdentifier extends TopicIdentifier implements IsSerializable {

	private Date lastUpdated;
	private double latitudeOnIsland;
	private double longitudeOnIsland;
	
	public FullTopicIdentifier(){}
	
	/**
	 * NOTE these latitude & longitudes refer to our location upon a specific island.
	 * 
	 * 
	 * @param topicID
	 * @param topicTitle
	 * @param lastUpdated
	 * @param latitude
	 * @param longitude
	 */
	public FullTopicIdentifier(long topicID, String topicTitle,Date lastUpdated, double latitude, double longitude) {
		super(topicID,topicTitle);
		this.lastUpdated = lastUpdated;
		this.latitudeOnIsland = latitude;
		this.longitudeOnIsland = longitude;
	}

	public FullTopicIdentifier(TopicTypeConnector conn) {
		super(conn.getTopic().getId(),conn.getTopic().getTitle());
		this.lastUpdated = conn.getTopic().getLastUpdated();
		this.latitudeOnIsland = conn.getLatitude();
		this.longitudeOnIsland = conn.getLongitude();
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public double getLatitudeOnIsland() {
		return latitudeOnIsland;
	}

	public void setLatitudeOnIsland(double latitude) {
		this.latitudeOnIsland = latitude;
	}

	public double getLongitudeOnIsland() {
		return longitudeOnIsland;
	}

	public void setLongitudeOnIsland(double d) {
		this.longitudeOnIsland = d;
	}


	//@Override
	public String toString() {
		return "FTI: "+getTopicID()+" "+getTopicTitle()+" "+longitudeOnIsland+" "+latitudeOnIsland;
	}

	//@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());		
		result = PRIME * result + (int) (latitudeOnIsland * 1000);		
		result = PRIME * result + (int) (longitudeOnIsland * 1000);
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
		if (latitudeOnIsland != other.latitudeOnIsland)
			return false;
		if (longitudeOnIsland != other.longitudeOnIsland)
			return false;
		return true;
	}

	
	
}