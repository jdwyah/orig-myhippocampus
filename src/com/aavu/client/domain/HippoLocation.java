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
	
	public HippoLocation(){
		setPublicVisible(false);
	}	

	public HippoLocation(User u, String d) {		
		super(u,d);
		setPublicVisible(false);
	}

	public GLatLng getLocation(){		
		String[] locs = getTitle().split(",");		
		double lat = Double.parseDouble(locs[0]);
		double longi = Double.parseDouble(locs[1]);
		return new GLatLng(lat,longi);    
	}
	public void setLocation(GLatLng point){
		setTitle(point.toUrlValue());
	}

	public boolean mustHaveUniqueName() {
		return false;
	}

	public String toShortString() {		
		return getLocation().toString();
	}

}
