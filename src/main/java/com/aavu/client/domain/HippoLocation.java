package com.aavu.client.domain;

// Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.io.Serializable;

import com.google.gwt.maps.client.geom.LatLng;

/**
 * 
 */

public class HippoLocation extends MetaValue implements Serializable {

	private static final int SIGNIFICANT_DIGITS = 100000;
	private static final int FILTER = 20;
	private static final int SIG_DIG = 6;

	public HippoLocation() {
		setPublicVisible(true);
	}

	public IntPair getFilteredLocation() {
		return new IntPair((getLatitude() / (SIGNIFICANT_DIGITS * FILTER)), getLongitude()
				/ (SIGNIFICANT_DIGITS * FILTER));
	}

	public LatLng getLocation() {
		return new LatLng((double) getLatitude() / SIGNIFICANT_DIGITS, (double) getLongitude()
				/ SIGNIFICANT_DIGITS);
	}

	public void setLocation(LatLng point) {
		if (getTitle() == null) {
			setTitle(point.toUrlValue(SIG_DIG));
		}
		setLatitude((int) (point.getLatitude() * SIGNIFICANT_DIGITS));
		setLongitude((int) (point.getLongitude() * SIGNIFICANT_DIGITS));
	}

	public boolean mustHaveUniqueName() {
		return false;
	}

	public String getValue() {
		return getTitle();
	}

	public String toShortString() {
		return getLocation().toString();
	}

}
