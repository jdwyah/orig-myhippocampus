package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.CopyOfAbstractOccurrenceAbstractURI;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyOfURI extends CopyOfAbstractOccurrenceAbstractURI implements Serializable,IsSerializable,ReallyCloneable{

	public CopyOfURI(){
		
	}

	//@Override
	public Object clone() {
		CopyOfURI uri = new CopyOfURI();
		copyProps(uri);
		return uri;
	}
}
