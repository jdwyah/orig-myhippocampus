package com.aavu.client.domain;
//Generated Jul 18, 2006 12:44:47 PM by Hibernate Tools 3.1.0.beta4

import java.util.HashSet;
import java.util.Set;

import org.gwtwidgets.client.svg.SVGBasicShape;

import com.aavu.client.domain.generated.AbstractUser;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * User generated by hbm2java
 */


/**
 * Extended by ServerSideUser on the Server to implement UserDetails,
 * Since getting the acegisecurity jar into client side land was a no go.
 * 
 */
public class User extends AbstractUser implements IsSerializable {


	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

}
