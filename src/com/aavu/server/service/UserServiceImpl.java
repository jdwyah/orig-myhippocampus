package com.aavu.server.service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.gwtwidgets.server.rpc.GWTSpringController;

import com.aavu.client.domain.User;
import com.aavu.client.service.remote.UserService;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;

public class UserServiceImpl extends GWTSpringController implements UserService{
	
	private UserDAO userDAO;

	public User getCurrentUser() {

		System.out.println("getCurrentUser");
		
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = "";
		if (obj instanceof UserDetails) {
			System.out.println("instance of UserDetails");
			username = ((UserDetails)obj).getUsername();			
		} else {
			System.out.println("not a UserDetail, it's a "+obj.getClass().getName());
			username = obj.toString();
		}

		
		System.out.println("obj "+obj);
		
		System.out.println("username "+username);
		//UserDetails user = (UserDetails) obj;
		//System.out.println("user "+user);
		
		//System.out.println("Is a regular USER obj "+(obj instanceof User));
		
		User u = new User();
		u.setUsername(username);
		return u;
		
		//return userDAO.getUserNamed(user.getUsername());

	}
	
	
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
