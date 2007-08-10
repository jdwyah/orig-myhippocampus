package com.aavu.client.domain;

// Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Each user should have 1 'root' node.
 */

public class Root extends Topic implements IsSerializable, Serializable {

	public Root() {
	}

	public Root(User user) {
		setPublicVisible(false);
		setUser(user);
		setTitle(user.getUsername() + " Root");
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
}
