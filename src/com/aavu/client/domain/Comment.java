package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Comment extends Entry implements Serializable,IsSerializable, ReallyCloneable {

	//@Override
	public Object clone() {				   		
		return copyProps(new Comment());
	}

}
