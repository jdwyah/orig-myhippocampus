package com.aavu.client.domain;

import java.io.Serializable;

public class GDocument extends GoogleData implements Serializable, ReallyCloneable {

	public GDocument() {
	}

	// @Override
	public Object clone() {
		return copyProps(new GDocument());
	}
}
