package com.aavu.server.domain;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

import com.aavu.client.domain.User;

public class ServerSideUser extends User implements UserDetails{



	public GrantedAuthority[] getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
