package com.aavu.client.domain;
//Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.util.HashSet;
import java.util.Set;

import org.gwtwidgets.client.svg.SVGBasicShape;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * User generated by hbm2java
 */


/**
 * Extended by ServerSideUser on the Server to implement UserDetails,
 * Since getting the acegisecurity jar into client side land was a no go.
 * 
 */
public class User implements IsSerializable {


	// Fields    

	private long id;
	private String username;
	/*
	 * @gwt.typeArgs <com.aavu.client.domain.TopicGWT>
	 */
	private Set topics = new HashSet(0);
	private boolean enabled;
	private boolean credentialsNonExpired;
	private String password;


	// Constructors

	/** default constructor */
	public User() {
	}


	/** full constructor */
	public User(String username, Set topics) {
		this.username = username;
		this.topics = topics;
	}



	// Property accessors

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set getTopics() {
		return this.topics;
	}

	public void setTopics(Set topics) {
		this.topics = topics;
	}




	public boolean equals(Object other) {
		if ( (this == other ) ) return true;
		if ( (other == null ) ) return false;
		if ( !(other instanceof User) ) return false;
		User castOther = ( User ) other; 

		return ( (this.getUsername()==castOther.getUsername()) || ( this.getUsername()!=null && castOther.getUsername()!=null && this.getUsername().equals(castOther.getUsername()) ) );
	}

	public int hashCode() {
		int result = 17;
//freemarker.core.ArithmeticEngine a = null;
SVGBasicShape shap = null;

		result = 37 * result + ( getUsername() == null ? 0 : this.getUsername().hashCode() );

		return result;
	}


	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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


	public boolean isEnabled() {
		return enabled;
	}


	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}   

}
