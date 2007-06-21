package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;


public class OccurrenceWithLocation implements IsSerializable, Serializable {

	private Occurrence occurrence;
	private int latitude;
	private int longitude;

	public OccurrenceWithLocation(){}

	public OccurrenceWithLocation(Occurrence occ) {
		this.occurrence = occ;
		latitude = -1;
		longitude = -1;
	}

	public Occurrence getOccurrence() {
		return occurrence;
	}
	public void setOccurrence(Occurrence occurrence) {
		this.occurrence = occurrence;
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
		final int prime = 31;
		int result = 1;
		result = prime * result + latitude;
		result = prime * result + longitude;
		result = prime * result
				+ ((occurrence == null) ? 0 : occurrence.hashCode());
		return result;
	}

	//@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OccurrenceWithLocation))
			return false;
		final OccurrenceWithLocation other = (OccurrenceWithLocation) obj;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		if (occurrence == null) {
			if (other.occurrence != null)
				return false;
		} else if (!occurrence.equals(other.occurrence))
			return false;
		return true;
	}





}