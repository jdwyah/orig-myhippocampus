package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WebLink extends URI implements Serializable,IsSerializable {
	
	public WebLink(){}
	
	public WebLink(User user, String description, String url, String notes) {
		setUser(user);
		setTitle(description);
		setUri(url);
		setData(notes);
	}

}