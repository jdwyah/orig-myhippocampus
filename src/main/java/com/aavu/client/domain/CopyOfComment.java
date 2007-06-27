package com.aavu.client.domain;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyOfComment extends CopyOfEntry implements Serializable,IsSerializable, ReallyCloneable {

	//@Override
	public Object clone() {				   		
		return copyPropsIntoParam(new CopyOfComment());
	}

}
