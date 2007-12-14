package com.aavu.client.domain;

// Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.io.Serializable;

/**
 * Each user should have 1 'root' node.
 */

public class Root extends RealTopic implements Serializable {

	public Root() {
	}

	public Root(User user) {
		setPublicVisible(true);
		setUser(user);
		setTitle("Desktop");
	}

	/**
	 * Overriden so that we don't let them delete their Root
	 * 
	 * @return
	 */
	// @Override
	public boolean isDeletable() {
		return false;
	}

	/**
	 * Overriden so that we don't let them delete their Root
	 * 
	 * @return
	 */
	// @Override
	public boolean isPublicVisibleEditable() {
		return false;
	}

}
