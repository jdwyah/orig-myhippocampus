package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Entry extends Occurrence implements Serializable,IsSerializable {
	
	public Entry (){
		super();
		
		//important. helps activates edit area
		setData("<BODY contentEditable=true></BODY>");
	}
	
}
