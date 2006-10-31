package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;

import com.aavu.client.domain.generated.AbstractAssociation;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Occurrence extends Topic implements Serializable,IsSerializable{
	
	public Occurrence(){
		
	}

	//TODO what to do with description? What should the title be?
	public Occurrence(User user, String description,String url, String notes) {
		super();
		setUser(user);
		setTitle(url);
	
		//important. helps activates edit area
		setData("<BODY contentEditable=true>"+notes+"</BODY>");
	}

	

}
