package com.aavu.client.domain;

import java.io.Serializable;

public class Comment extends Entry implements Serializable, ReallyCloneable {

	// @Override
	public Object clone() {
		return copyPropsIntoParam(new Comment());
	}

}
