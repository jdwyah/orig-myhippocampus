package com.aavu.client.domain;

import com.aavu.client.domain.dto.TopicIdentifier;

/**
 * This is the basic instantiation of the abstract type Topic.
 * 
 * All topics that aren't special subclasses of this should be of this type. This let's us do things
 * like query the DB for only top-level topics (for a see also auto-complete) without saying
 * discriminator != meta && discriminator != occ etc etc
 * 
 * @author Jeff Dwyer
 * 
 */
public class RealTopic extends Topic {

	public RealTopic(TopicIdentifier to) {
		super(to);
	}

	public RealTopic() {
		super();
	}

	public RealTopic(User u, String d) {
		super(u, d);
	}

}
