package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractAssociation;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Occurrence extends Topic implements Serializable,IsSerializable{
	
	public Occurrence(){
		
	}

	public Occurrence(User user, String url, String notes) {
		setUser(user);
		setTitle(url);
		setData(notes);
	}

	

}
