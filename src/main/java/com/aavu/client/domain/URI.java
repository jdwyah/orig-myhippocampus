package com.aavu.client.domain;

import java.io.Serializable;

import com.aavu.client.domain.generated.AbstractOccurrenceAbstractURI;

public class URI extends AbstractOccurrenceAbstractURI implements Serializable, ReallyCloneable {

	public URI() {
	}

	// @Override
	public Object clone() {
		return copyProps(new URI());
	}

	public URI copyPropsIntoParam(URI o) {
		super.copyPropsIntoParam(o);
		o.setUri(getUri());
		return o;
	}

	public URI copyPropsButNotIDIntoParam(URI o) {
		System.out.println("URI copy rpops");
		super.copyPropsButNotIDIntoParam(o);
		o.setUri(getUri());
		return o;
	}
}
