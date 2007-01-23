package com.aavu.client.domain;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * 
 * 
 * @author Jeff Dwyer
 *
 */
public abstract class TopicWithLocation implements java.io.Serializable, IsSerializable {


     private Topic topic;
     private int latitude;
     private int longitude;
     
     // Constructors
    /** default constructor */
    public TopicWithLocation() {
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

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

    




}


