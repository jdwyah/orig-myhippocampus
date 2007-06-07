package com.aavu.server.service.gwt;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;

import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.service.remote.GWTUserService;
import com.aavu.server.service.UserService;
import com.aavu.server.util.gwt.GWTSpringControllerReplacement;

public class GWTUserServiceImpl extends GWTSpringControllerReplacement implements GWTUserService{
	private static final Logger log = Logger.getLogger(GWTUserServiceImpl.class);
	
	private UserService userService;
	
	public User getCurrentUser() throws HippoBusinessException {
	
		try{
			User user = userService.getCurrentUser();
			log.info("GWT get current user... "+user.getUsername());
			return user;
		}catch(UsernameNotFoundException u){
			throw new HippoBusinessException(u.getMessage());
		}		
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	

}
