package com.aavu.server.service.impl;

import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.aavu.client.domain.Root;
import com.aavu.client.domain.Subscription;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.DuplicateUserException;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.service.UserService;
import com.aavu.server.util.CryptUtils;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class UserServiceImpl implements UserService {

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	private static final long CANCELLED_SUBSCRIPTION_ID = 1;
	
	private UserDAO userDAO;

	private EditDAO editDAO;
	
	private int maxUsers;

	private int startingInvitations;
	

	public User getCurrentUser() throws UsernameNotFoundException {

		log.debug("getCurrentUser");

		try{
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

			try {
				return userDAO.getUserByUsername(username);	
			} catch (UsernameNotFoundException e) {
				log.debug(e);
				throw e;
			}


		}catch (NullPointerException e) {
			//This seems to get thrown on some errors. ie a servlet error comes here,
			//then something in SecurityContextHolder.getContext().getAuthentication().getPrincipal()
			//NPE's and then we get this NPE exception instead of the original exception.
			//rethrow as UserNotFound since we, hopefully, know what to do with that.
			/*
			 *  java.lang.NullPointerException com.aavu.server.service.impl.UserServiceImpl.getCurrentUser(UserServiceImpl.java:39)
 				sun.reflect.GeneratedMethodAccessor94.invoke(Unknown Source)
 				sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
 				... AOP schtuff 				
 				org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:161)
 				org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:204) $Proxy1.getCurrentUser(Unknown Source)
 				com.aavu.server.web.controllers.BasicController.getDefaultModel(BasicController.java:46)
 				com.aavu.server.web.controllers.BasicController.handleRequestInternal(BasicController.java:37)
			 */
			throw new UsernameNotFoundException("No Authentication Context");
		}
		
	}

	public User createUser(CreateUserRequestCommand comm) throws DuplicateUserException, HippoBusinessException {

		if(comm.isStandard()){
			return createUser(comm.getUsername(),comm.getPassword(),comm.getEmail());
		}else if(comm.isOpenID()){
			return createUser(comm.getOpenIDusernameDoNormalization(),null,comm.getEmail());
		}else{
			throw new RuntimeException("Command Neither standard nor open");
		}

	}

	private User createUser(String username,String userpass,String email) throws DuplicateUserException, HippoBusinessException{
		return createUser(username,userpass,email,false);
	}

	


	/**
	 * Return if the command has a unique username
	 */
	public boolean exists(String username) {		
		try{
			userDAO.loadUserByUsername(username);			
			return true; 			
		}catch (UsernameNotFoundException e) {
			return false;
		}		
	}

	public boolean couldBeOpenID(String username){
		return username.contains(".") || username.contains("=");
	}
	
	/**
	 * only openID users are allowed '.' || '=' and all openID usernames must have a '.' || '='
	 * so, if it's got a '.' || '=' janrain.normalize() before the lookup
	 */
	public User getUserWithNormalization(String username)  throws UsernameNotFoundException {

		if(couldBeOpenID(username)){
			return userDAO.getUserByUsername(com.janrain.openid.Util.normalizeUrl(username));
		}else{
			return userDAO.getUserByUsername(username);
		}
	}

	public List<User> getAllUsers() {
		List<User> users = userDAO.getAllUsers();
//		if(log.isDebugEnabled()){
//			for (User user : users) {
//				log.info(user.getUsername()+" "+user.isSupervisor());
//			}
//		}
		return users;
	}

	

	/**
	 * TODO LOW AOP this security concern
	 */
	public void toggleEnabled(Integer id) throws PermissionDeniedException {	
		log.info("toggleEnabled "+getCurrentUser().getUsername()+" "+getCurrentUser().isSupervisor());
		if(getCurrentUser().isSupervisor()){
			User user = userDAO.getUserForId(id);
			user.setEnabled(!user.isEnabled());
			userDAO.save(user);
		}else{
			throw new PermissionDeniedException("You don't have rights to do that.");
		}
	}

	public void delete(Integer id) throws PermissionDeniedException {
		if(getCurrentUser().isSupervisor()){
			User user = userDAO.getUserForId(id);
			userDAO.delete(user);
		}else{
			throw new PermissionDeniedException("You don't have rights to do that.");
		}
	}

	public void toggleSupervisor(Integer id) throws PermissionDeniedException {
		if(getCurrentUser().isSupervisor()){
			System.out.println("ID "+id);
			User user = userDAO.getUserForId(id);
			user.setSupervisor(!user.isSupervisor());
			userDAO.save(user);
		}else{
			throw new PermissionDeniedException("You don't have rights to do that.");
		}
	}

	/**
	 * lowercase usernames before creation
	 * @return 
	 * @throws HippoBusinessException 
	 */
	public User createUser(String username, String userpass, String email, boolean superV) throws HippoBusinessException {

		//hmm a bit odd having the logic catch in the 
		//
		if(log.isDebugEnabled()){
			log.debug("u: "+username+" p "+userpass);
			log.debug("pp: "+CryptUtils.hashString(userpass));
		}

		User user = new User();
		user.setUsername(username.toLowerCase());		
		user.setEmail(email);
		user.setSupervisor(superV);
		user.setEnabled(true);
		user.setInvitations(startingInvitations);
		
		if(userpass != null){
			user.setPassword(CryptUtils.hashString(userpass));
		}
		
		User createdU = userDAO.save(user);
		
		setup(createdU);
		
		return createdU;
	}

	private void setup(User createdU) throws HippoBusinessException {
		Root r = new Root(createdU);
		
		r = (Root)editDAO.save(r);
		
		Topic movies = new Topic(createdU,"Movies");		
		movies.addType(r, 100,100);		
		movies = editDAO.save(movies);
		
		
		
		Topic people = new Topic(createdU,"People");		
		people.addType(r, 400,400);		
		people = editDAO.save(people);
		
		
		
	}

	public void changeToSubscriptionAndSave(User user, Subscription subscription, String paypalID) {
		
		user.setPaypalID(paypalID);
		
		user.setSubscription(subscription);
		userDAO.save(user);
		
	}


	/**
	 * Remove the paypal ID so they can switch to a different paypal account by cancelling,
	 * then re-signing up.
	 */	
	public void subscriptionCancel(String paypalID) {
		Subscription cancelled = userDAO.getSubscriptionByID(CANCELLED_SUBSCRIPTION_ID);
		
		User user = userDAO.getForPaypalID(paypalID);
		
		changeToSubscriptionAndSave(user, cancelled,"");
	}

	/**
	 * TODO do something.
	 * NOTE: this req will often be received _before_ subscriptionNewSignup
	 * 
	 */
	public void subscriptionRecordPayment(long hippoID,String paypalID) {
		
		User userWithThisPaypal = userDAO.getForPaypalID(paypalID);
		User userToProcess = userDAO.getUserForId(hippoID);
		
		log.info(userToProcess.getId()+" "+userToProcess.getUsername()+" "+paypalID+" PAID!");	
	}

	/**
	 * Currently blocking signups for multiple users from 1 paypal account.
	 * This is because on cancel, we only receive the paypalID and if there are
	 * multiples, we wouldn't know who to cancel.
	 */
	public void subscriptionNewSignup(long hippoID,String paypalID, long subscriptionID,String userEmail) throws HippoBusinessException {
		
		User userToProcess = userDAO.getUserForId(hippoID);
		
		User otherUserWithThisPaypal = userDAO.getForPaypalID(paypalID);
		
		if(otherUserWithThisPaypal != null){
			throw new HippoBusinessException("Already Have a user for that paypalID");
		}
		
		
		Subscription subscription = userDAO.getSubscriptionByID(subscriptionID);

		changeToSubscriptionAndSave(userToProcess, subscription,paypalID);
		
	}

	public List<Subscription> getAllUpgradeSubscriptions() {
		return userDAO.getAllUpgradeSubscriptions();
	}

	/**
	 * don't let it go negative
	 */
	public void addInvitationsTo(User inviter,int num) {
		int current = inviter.getInvitations();
		int newV = current + num;
		if(newV >= 0){
			inviter.setInvitations(newV);
		}
		userDAO.save(inviter);
	}

	public boolean nowAcceptingSignups() {
		return userDAO.getUserCount() <  maxUsers;
	}

	@Required
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	@Required
	public void setMaxUsers(int maxUsers) {
		this.maxUsers = maxUsers;
	}

	@Required
	public void setStartingInvitations(int startingInvitations) {
		this.startingInvitations = startingInvitations;
	}
	@Required
	public void setEditDAO(EditDAO editDAO) {
		this.editDAO = editDAO;
	}
	


}
