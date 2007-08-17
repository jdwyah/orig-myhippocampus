package com.aavu.client.domain;

import java.io.Serializable;

public class GSpreadsheet extends GoogleData implements Serializable, ReallyCloneable {

	public GSpreadsheet() {
	}

	// @Override
	public Object clone() {
		return copyProps(new GSpreadsheet());
	}
}
