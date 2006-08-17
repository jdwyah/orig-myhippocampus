package com.aavu.server.domain;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.log4j.Logger;

import com.aavu.client.domain.User;

public class ServerSideUser extends User implements UserDetails{
	
	private static final Logger log = Logger.getLogger(ServerSideUser.class);
	
	public ServerSideUser(){
		setEnabled(true);
	}

	public GrantedAuthority[] getAuthorities() {
		GrantedAuthority[] rtn = new GrantedAuthority[1];
		if(isSupervisor()){
			log.debug("adding supervisor permissions");		
			rtn[0] = new MyAuthority("ROLE_SUPERVISOR");
			return rtn;
		}else{
			rtn[0] = new MyAuthority("");
			return rtn;
		}
		
	}
	
	private class MyAuthority implements GrantedAuthority{
		private String auth;
		public MyAuthority(String auth){
			this.auth = auth;
		}		
		public String getAuthority() {
			return auth;
		}		
	}

	
}
