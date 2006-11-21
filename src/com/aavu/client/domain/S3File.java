package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class S3File extends URI implements Serializable,IsSerializable {
	
	public S3File(){}
		
	public S3File(User user, String filename, String s3postfix, String notes) {
		setUser(user);
		setTitle(filename);
		setUri(s3postfix);
		setData(notes);
	}

}
