package com.aavu.server.service.gwt;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;


/**
 * TEST CLASS ONLY it has CGLIB in the title so it tricks into thinking 
 * it's cglib enhanced.
 * 
 * @author Jeff Dwyer
 *
 */
public class TopicCGLIB extends Topic {

	public TopicCGLIB(User u, String d) {
		super(u,d);
	}

}
