package com.aavu.server.service.gwt;

import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.User;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.UserService;

public class GWTUserServiceImpl extends GWTSpringController implements GWTUserService{
	
	private UserService userService;
	
	public User getCurrentUser() {

		ServerSideUser user = userService.getCurrentUser();
		
		User u = new User();
		u.setUsername(user.getUsername());
		return u;		
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	

}
