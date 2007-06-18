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
 * Each user should have 1 'root' node.
 */

public class Root extends Topic implements IsSerializable, Serializable{
	
	public Root() {}
	
	public Root(User user){
		setPublicVisible(false);
		setUser(user);
		setTitle(user.getUsername()+" Root");
	}

		
	

}
