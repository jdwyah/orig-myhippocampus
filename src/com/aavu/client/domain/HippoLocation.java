package com.aavu.client.domain;
// Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;

import org.gwtwidgets.client.util.SimpleDateFormat;

import com.aavu.client.widget.autocompletion.Completable;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.mapitz.gwt.googleMaps.client.GLatLng;


/**
 *
 */

public class HippoLocation extends MetaValue implements IsSerializable, Serializable{
	
	private static final int SIGNIFICANT_DIGITS = 100000;
	
	public HippoLocation(){
		setPublicVisible(false);
	}	
	
	public GLatLng getLocation(){				
		return new GLatLng((double)getLatitude()/SIGNIFICANT_DIGITS,
				(double)getLongitude()/SIGNIFICANT_DIGITS);    
	}
	public void setLocation(GLatLng point){
		if(getTitle() == null){
			setTitle(point.toUrlValue());
		}
		setLatitude((int) (point.lat()*SIGNIFICANT_DIGITS));
		setLongitude((int) (point.lng()*SIGNIFICANT_DIGITS));
	}

	public boolean mustHaveUniqueName() {
		return false;
	}
	
	public String getValue(){
		return getTitle();
	}
	
	
	public String toShortString() {		
		return getLocation().toString();
	}

}
