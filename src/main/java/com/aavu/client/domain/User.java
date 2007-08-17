package com.aavu.client.domain;

//Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import com.aavu.client.domain.generated.AbstractUser;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * User generated by hbm2java
 */


/**
 * Extended by ServerSideUser on the Server to implement UserDetails, Since getting the
 * acegisecurity jar into client side land was a no go.
 * 
 */
public class User extends AbstractUser implements IsSerializable {

	public static final int PREMIUM_CUTOFF = 75;

	public User() {
		setEnabled(true);
		setSupervisor(false);
	}

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isPremiumAccount() {
		return getSubscription().getMaxTopics() > PREMIUM_CUTOFF;
	}

	public int getWorldSize(int totalNumberOfTopics, int numberOfTags) {
		// 2000 @ 0 tags
		// 4000 @ 30 tags
		return 2000 + 66 * numberOfTags;
	}

	// @Override
	public String toString() {
		return getId() + " " + getUsername();
	}



}
