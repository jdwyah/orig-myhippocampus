package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyOfS3File extends CopyOfURI implements Serializable,IsSerializable, ReallyCloneable {
	
	public CopyOfS3File(){}
		
	public CopyOfS3File(User user, String filename, String s3postfix) {
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
		CopyOfS3File cc = new CopyOfS3File();
		copyProps(cc);
		return cc;
	}

	
}
