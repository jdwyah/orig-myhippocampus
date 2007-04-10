package com.aavu.client.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import com.aavu.client.domain.generated.AbstractOccurrence;
import com.aavu.client.domain.subjects.Subject;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Occurrence extends AbstractOccurrence implements Serializable,IsSerializable, ReallyCloneable {
	
	public Occurrence(){
		
	}

    /**
     * This is a leaked concern of CGLIB
     */
	//@Override
	public Object clone() {				   		
		return copyPropsIntoParam(new Occurrence());
	}
	
	/**
	 * copy properties of _this_ into the parameter
	 * 
	 * @param o
	 * @return
	 */
	public Occurrence copyPropsIntoParam(Occurrence o){
		o.setId(getId());
		o.setUser(getUser());
		o.setTitle(getTitle());
		o.setData(getData());
		o.setLastUpdated(getLastUpdated());
		o.setCreated(getCreated());
		return o;
	}
	
	public Occurrence(User user, String title, String data, Date lastUpdated, Date created) {
		super(user,title,data,lastUpdated,created);
	}

	

}
