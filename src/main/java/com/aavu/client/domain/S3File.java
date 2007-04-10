package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class S3File extends URI implements Serializable,IsSerializable, ReallyCloneable {
	
	public S3File(){}
		
	public S3File(User user, String filename, String s3postfix) {
		setUser(user);
		
		setUri(s3postfix);
		
		/*
		 * parse out just the file name, (no path)
		 */
		if(filename == null){
			filename = "unnamed";
		}
		String title = filename;
		int i = filename.lastIndexOf('\\');
		if(i != -1){
			title = filename.substring(i+1);
		}		
		setTitle(title);
		
		/*
		 * store path & name in data
		 */
		setData(filename);
	}

	//@Override
	public Object clone() {				   		
		return copyProps(new S3File());
	}

	
}
