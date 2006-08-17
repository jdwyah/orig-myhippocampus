package com.aavu.server.service.gwt;

import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.User;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.UserService;

public class GWTUserServiceImpl extends GWTSpringController implements GWTUserService{
	
	private UserService userService;
	
	public User getCurrentUser() {
		System.out.println("GWT get current user...");
		ServerSideUser user = userService.getCurrentUser();
		
		User u = new User();
		u.setUsername(user.getUsername());
		u.setSupervisor(user.isSupervisor());
		System.out.println("fin "+u);
		return u;		
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	

}
