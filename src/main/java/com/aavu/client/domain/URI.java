package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractOccurrenceAbstractURI;
import com.google.gwt.user.client.rpc.IsSerializable;

public class URI extends AbstractOccurrenceAbstractURI implements Serializable,IsSerializable,ReallyCloneable{

	public URI(){
		
	}

	//@Override
	public Object clone() {				   		
		return copyProps(new URI());
	}
}
