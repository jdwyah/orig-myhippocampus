package com.aavu.server.service.gwt;

import com.aavu.client.domain.User;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.server.service.UserService;

public class GWTUserServiceImpl extends org.gwtwidgets.server.spring.GWTSpringController implements GWTUserService{
	
	private UserService userService;
	
	public User getCurrentUser() {
		System.out.println("GWT get current user...");
		User user = userService.getCurrentUser();
				
		return user;		
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	

}
