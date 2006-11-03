package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;

import com.aavu.client.domain.generated.AbstractAssociation;
import com.aavu.client.domain.generated.AbstractOccurrence;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Occurrence extends AbstractOccurrence implements Serializable,IsSerializable{
	
	public Occurrence(){
		
	}


	public Occurrence(User user, String title, String data, Date lastUpdated, Date created) {
		super(user,title,data,lastUpdated,created);
	}

	

}
