package com.aavu.server.service.impl;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;

import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.exception.DuplicateUserException;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class UserServiceImpl implements UserService {

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	private UserDAO userDAO;

	public void createUser(CreateUserRequestCommand comm) throws DuplicateUserException {
		ServerSideUser user = new ServerSideUser();


		createUser(comm.getUsername(),comm.getPassword());


	}



	private  void createUser(String username,String userpass) throws DuplicateUserException{

		//hmm a bit odd having the logic catc in the 
		//


		log.debug("u: "+username+" p "+userpass);
		log.debug("pp: "+hashPassword(userpass));

		ServerSideUser user = new ServerSideUser();
		user.setUsername(username);
		user.setPassword(hashPassword(userpass));		

		userDAO.save(user);



	}

	private String hashPassword(String password) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16);			
		} catch (NoSuchAlgorithmException nsae) {

		}
		return hashword;
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


	public List<ServerSideUser> getAllUsers() {
		return userDAO.getAllUsers();
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}



	public void toggleEnabled(Integer id) {				
		ServerSideUser user = userDAO.getUserForId(id);
		user.setEnabled(!user.isEnabled());
		userDAO.save(user);
	}



	public void delete(Integer id) {
		ServerSideUser user = userDAO.getUserForId(id);
		userDAO.delete(user);
	}



	public void toggleSupervisor(Integer id) {
		ServerSideUser user = userDAO.getUserForId(id);
		user.setSupervisor(!user.isSupervisor());
		userDAO.save(user);
	}



}
