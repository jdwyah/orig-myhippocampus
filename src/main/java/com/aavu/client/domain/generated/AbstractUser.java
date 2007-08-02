package com.aavu.client.domain.generated;

import java.util.Date;

import com.aavu.client.domain.Subscription;
import com.google.gwt.user.client.rpc.IsSerializable;

// Generated Oct 31, 2006 9:16:47 AM by Hibernate Tools 3.1.0.beta5



/**
 * AbstractUser generated by hbm2java
 */
public abstract class AbstractUser implements IsSerializable, java.io.Serializable {

	// Fields

	private String email;
	private boolean enabled;
	private long id;
	private int invitations;
	private String password;
	private String paypalID;
	private Date lastDeliciousDate;


	private Subscription subscription;

	private boolean supervisor;

	private String username;

	// Constructors

	/** default constructor */
	public AbstractUser() {
	}

	/** full constructor */
	public AbstractUser(String username, String password, boolean enabled, boolean supervisor) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.supervisor = supervisor;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AbstractUser))
			return false;
		AbstractUser castOther = (AbstractUser) other;

		return ((this.getUsername() == castOther.getUsername()) || (this.getUsername() != null
				&& castOther.getUsername() != null && this.getUsername().equals(
				castOther.getUsername())));
	}

	// Property accessors
	public long getId() {
		return this.id;
	}

	public int getInvitations() {
		return invitations;
	}

	public String getPassword() {
		return this.password;
	}


	public String getPaypalID() {
		return paypalID;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public String getUsername() {
		return this.username;
	}

	public int hashCode() {
		int result = 17;


		result = 37 * result + (getUsername() == null ? 0 : this.getUsername().hashCode());



		return result;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public boolean isSupervisor() {
		return this.supervisor;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setInvitations(int invitations) {
		this.invitations = invitations;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public void setPaypalID(String paypalID) {
		this.paypalID = paypalID;
	}

	public void setSubscription(Subscription subscription) {
		this.subscription = subscription;
	}

	public void setSupervisor(boolean supervisor) {
		this.supervisor = supervisor;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getLastDeliciousDate() {
		return lastDeliciousDate;
	}

	public void setLastDeliciousDate(Date lastDeliciousDate) {
		this.lastDeliciousDate = lastDeliciousDate;
	}


}
