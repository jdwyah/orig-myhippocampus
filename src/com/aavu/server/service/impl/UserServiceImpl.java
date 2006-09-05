package com.aavu.server.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;

import com.aavu.client.domain.User;
import com.aavu.client.exception.DuplicateUserException;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class UserServiceImpl implements UserService {

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	private UserDAO userDAO;

	private Boolean hackUserSwitch;
	
	public User getCurrentUser() {

		log.debug("getCurrentUser");
		
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String username = "";
		if (obj instanceof UserDetails) {
			log.debug("instance of UserDetails");
			username = ((UserDetails)obj).getUsername();			
		} else {
			log.debug("not a UserDetail, it's a "+obj.getClass().getName());
			username = obj.toString();
		}
		
		log.debug("loadUserByUsername "+username);
		if(hackUserSwitch && username.equals("anonymousUser")){
			log.debug("hack switch to test");
			username = "test";
		}
		
		
		try {
			return userDAO.getUserByUsername(username);	
		} catch (UsernameNotFoundException e) {
			log.warn(e);
			throw e;
		}
		
	}
	
	public void createUser(CreateUserRequestCommand comm) throws DuplicateUserException {
		User user = new User();


		createUser(comm.getUsername(),comm.getPassword());


	}



	private void createUser(String username,String userpass) throws DuplicateUserException{
		createUser(username,userpass,false);
	}

	private String hashPassword(String password) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes("UTF-8"));
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16);
						
		} catch (NoSuchAlgorithmException nsae) {
			log.error(nsae);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		}
		
		return pad(hashword,32,'0');
	}
	
	private String pad(String s, int length, char pad) {
		StringBuffer buffer = new StringBuffer(s);
		while (buffer.length() < length) {
			buffer.insert(0, pad);
		}
		return buffer.toString();
	}


	/**
	 * Return if the command has a unique username
	 */
	public boolean isUnique(CreateUserRequestCommand comm) {		
		try{
			userDAO.loadUserByUsername(comm.getUsername());			
			return false; 			
		}catch (UsernameNotFoundException e) {
			return true;
		}		
	}


	public List<User> getAllUsers() {
		return userDAO.getAllUsers();
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	public void setHackUserSwitch(Boolean hackUserSwitch) {
		this.hackUserSwitch = hackUserSwitch;
	}

	public void toggleEnabled(Integer id) {				
		User user = userDAO.getUserForId(id);
		user.setEnabled(!user.isEnabled());
		userDAO.save(user);
	}



	public void delete(Integer id) {
		User user = userDAO.getUserForId(id);
		userDAO.delete(user);
	}



	public void toggleSupervisor(Integer id) {
		System.out.println("ID "+id);
		User user = userDAO.getUserForId(id);
		user.setSupervisor(!user.isSupervisor());
		userDAO.save(user);
	}

	public void createUser(String username, String userpass, boolean superV) {

		//hmm a bit odd having the logic catc in the 
		//
		log.debug("u: "+username+" p "+userpass);
		log.debug("pp: "+hashPassword(userpass));
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(hashPassword(userpass));		
		user.setSupervisor(superV);
		
		userDAO.save(user);

	}



}
