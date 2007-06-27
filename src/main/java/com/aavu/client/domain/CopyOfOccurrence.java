package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;

import com.aavu.client.domain.generated.CopyOfAbstractOccurrence;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CopyOfOccurrence extends CopyOfAbstractOccurrence implements Serializable,IsSerializable, ReallyCloneable, OccurrenceI {
	
	public CopyOfOccurrence(){
		
	}

    /**
     * This is a leaked concern of CGLIB
     */
	//@Override
	public Object clone() {				   		
		return copyPropsIntoParam(new CopyOfOccurrence());
	}
	
	/**
	 * copy properties of _this_ into the parameter
	 * 
	 * @param o
	 * @return
	 */
	public CopyOfOccurrence copyPropsIntoParam(CopyOfOccurrence o){
		o.setId(getId());
		o.setUser(getUser());
		o.setTitle(getTitle());
		o.setData(getData());
		o.setLastUpdated(getLastUpdated());
		o.setCreated(getCreated());
		return o;
	}
	
	
	

}
