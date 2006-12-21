package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Entry extends Occurrence implements Serializable,IsSerializable {
	
	private static final String INIT_STR = "<BODY contentEditable=true>";
	private static final String INIT_STR_END = "</BODY>";
	
	public Entry (){
		super();
		
		//important. helps activates edit area
		setData(INIT_STR+INIT_STR_END);
	}
	public void setInnerHTML(String html){
		setData(INIT_STR+html+INIT_STR_END);
	}
	public boolean isEmpty(){
		return getData() == null ||
				(getData().length() == INIT_STR.length()); 
	}
	
}
