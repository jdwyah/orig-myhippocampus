package com.aavu.server.service.gwt;

import org.acegisecurity.userdetails.UsernameNotFoundException;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.server.service.UserService;
import com.aavu.server.util.gwt.GWTSpringControllerReplacement;

public class GWTUserServiceImpl extends GWTSpringControllerReplacement implements GWTUserService{
	
	private UserService userService;
	
	public User getCurrentUser() throws HippoBusinessException {
		System.out.println("GWT get current user...");
		try{
			User user = userService.getCurrentUser();
			return user;
		}catch(UsernameNotFoundException u){
			throw new HippoBusinessException(u.getMessage());
		}		
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	

}
