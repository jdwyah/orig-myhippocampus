package com.aavu.client.domain;

import java.io.Serializable;

/**
 * Just a class to clarify hierarchy.
 * 
 * A topic's association will be of Type Meta.class, with Members of Type Topic.class
 * 
 * However, if the members are not MetaTopics, they will be of Type MetaValue, onw of our subclasses
 * like dates.
 * 
 * @author Jeff Dwyer
 */
public abstract class MetaValue extends Topic implements Serializable {

	public MetaValue() {
		super();
	}

	public MetaValue(User u, String d) {
		super(u, d);
	}

}
