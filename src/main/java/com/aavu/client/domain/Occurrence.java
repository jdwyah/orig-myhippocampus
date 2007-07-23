package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractOccurrence;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Occurrence extends AbstractOccurrence implements Serializable, IsSerializable,
		ReallyCloneable {

	public Occurrence() {

	}

	/**
	 * This is a leaked concern of CGLIB
	 */
	// @Override
	public Object clone() {
		return copyPropsIntoParam(new Occurrence());
	}

	/**
	 * copy properties of _this_ into the parameter
	 * 
	 * @param o
	 * @return
	 */
	public Occurrence copyPropsIntoParam(Occurrence o) {
		super.copyPropsIntoParam(o);
		o.setData(getData());
		return o;
	}

	public Occurrence copyPropsButNotIDIntoParam(Occurrence o) {
		System.out.println("Occ copy rpops");
		super.copyPropsButNotIDIntoParam(o);
		o.setData(getData());
		return o;
	}



}
