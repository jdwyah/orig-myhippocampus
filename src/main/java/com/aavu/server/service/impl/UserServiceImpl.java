package com.aavu.server.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Subscription;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.QuickAddEntryCommand;
import com.aavu.client.domain.commands.SaveDatesCommand;
import com.aavu.client.exception.DuplicateUserException;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

/**
 * TODO remove ApplicationContextAware. This was introduced when UserService began needing a
 * reference to TopicService
 * 
 * TODO I feel like we shouldn't have to be updating the UserCache ourselves. Why is this? Right now
 * it's flaky because we need to remove users too.
 * 
 * @author Jeff Dwyer
 * 
 */
@Transactional
public class UserServiceImpl implements UserService, ApplicationContextAware {

	private static final Logger log = Logger.getLogger(UserServiceImpl.class);

	private static final long CANCELLED_SUBSCRIPTION_ID = 1;
	/**
	 * match with applicationContext-acegi-security.xml <bean id="anonymousProcessingFilter">
	 */
	public static final String ANONYMOUS = "anonymousUser";

	public static final String SAMPLE_TAG_TITLE = "Sample Movies";

	private UserDAO userDAO;

	private UserCache userCache;

	private TopicService topicService;

	private PasswordEncoder passwordEncoder;
	private SaltSource saltSource;

	private int maxUsers;

	private int startingInvitations;



	private MessageSource messageSource;

	public User getCurrentUser() throws UsernameNotFoundException {

		log.debug("getCurrentUser");


		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (null == auth) {
			throw new UsernameNotFoundException("No Authorizations");
		}

		Object obj = auth.getPrincipal();
		String username = "";

		if (obj instanceof UserDetails) {
			log.debug("instance of UserDetails");
			username = ((UserDetails) obj).getUsername();
		} else {
			log.debug("not a UserDetail, it's a " + obj.getClass().getName());
			username = obj.toString();
		}

		if (username.equals(ANONYMOUS)) {
			log.debug("Anonymous return null user");
			return null;
		}

		log.debug("loadUserByUsername " + username);


		try {

			ServerSideUser serverUser = (ServerSideUser) userCache.getUserFromCache(username);

			User u;
			if (serverUser == null) {

				u = userDAO.getUserByUsername(username);
				userCache.putUserInCache(new ServerSideUser(u));

			} else {
				u = serverUser.getUser();
			}

			return u;
		} catch (UsernameNotFoundException e) {
			log.debug(e);
			throw e;
		}

	}

	public User createUser(CreateUserRequestCommand comm) throws DuplicateUserException,
			HippoException {

		if (comm.isStandard()) {
			return createUser(comm.getUsername(), comm.getPassword(), comm.getEmail());
		} else if (comm.isOpenID()) {
			return createUser(comm.getOpenIDusernameDoNormalization(), null, comm.getEmail());
		} else {
			throw new RuntimeException("Command Neither standard nor open");
		}

	}

	private User createUser(String username, String userpass, String email)
			throws DuplicateUserException, HippoException {
		return createUser(username, userpass, email, false);
	}



	/**
	 * Return if the command has a unique username
	 */
	public boolean exists(String username) {
		try {
			userDAO.loadUserByUsername(username);
			return true;
		} catch (UsernameNotFoundException e) {
			return false;
		}
	}

	public boolean couldBeOpenID(String username) {
		return username.contains(".") || username.contains("=");
	}

	/**
	 * only openID users are allowed '.' || '=' and all openID usernames must have a '.' || '=' so,
	 * if it's got a '.' || '=' janrain.normalize() before the lookup
	 */
	public User getUserWithNormalization(String username) throws UsernameNotFoundException {

		if (couldBeOpenID(username)) {
			return userDAO.getUserByUsername(com.janrain.openid.Util.normalizeUrl(username));
		} else {
			return userDAO.getUserByUsername(username);
		}
	}

	public List<User> getAllUsers() {
		List<User> users = userDAO.getAllUsers();
		// if(log.isDebugEnabled()){
		// for (User user : users) {
		// log.info(user.getUsername()+" "+user.isSupervisor());
		// }
		// }
		return users;
	}



	/**
	 * TODO LOW AOP this security concern
	 */
	public void toggleEnabled(Integer id) throws PermissionDeniedException {
		log.info("toggleEnabled " + getCurrentUser().getUsername() + " "
				+ getCurrentUser().isSupervisor());
		if (getCurrentUser().isSupervisor()) {
			User user = userDAO.getUserForId(id);
			user.setEnabled(!user.isEnabled());
			userDAO.save(user);
			userCache.removeUserFromCache(user.getUsername());
		} else {
			throw new PermissionDeniedException("You don't have rights to do that.");
		}
	}

	public void delete(Integer id) throws PermissionDeniedException {
		if (getCurrentUser().isSupervisor()) {
			User user = userDAO.getUserForId(id);
			userDAO.delete(user);

			userCache.removeUserFromCache(user.getUsername());
		} else {
			throw new PermissionDeniedException("You don't have rights to do that.");
		}
	}

	public void toggleSupervisor(Integer id) throws PermissionDeniedException {
		if (getCurrentUser().isSupervisor()) {
			System.out.println("ID " + id);
			User user = userDAO.getUserForId(id);
			user.setSupervisor(!user.isSupervisor());
			userDAO.save(user);
			userCache.removeUserFromCache(user.getUsername());
		} else {
			throw new PermissionDeniedException("You don't have rights to do that.");
		}
	}

	/**
	 * lowercase usernames before creation
	 * 
	 * @return
	 * @throws HippoBusinessException
	 */
	public User createUser(String username, String userpass, String email, boolean superV)
			throws HippoException {
		return createUser(username, userpass, email, superV, new Date());
	}

	public User createUser(String username, String userpass, String email, boolean superV,
			Date dateCreated) throws HippoException {

		// hmm a bit odd having the logic catch in the
		//
		if (log.isDebugEnabled()) {
			log.debug("u: " + username + " p " + userpass);
		}

		User user = new User();
		user.setUsername(username.toLowerCase());
		user.setEmail(email);
		user.setSupervisor(superV);
		user.setEnabled(true);
		user.setInvitations(startingInvitations);
		user.setDateCreated(dateCreated);

		user = userDAO.save(user);

		if (userpass != null) {

			Object salt = saltSource.getSalt(new ServerSideUser(user));

			user.setPassword(passwordEncoder.encodePassword(userpass, salt));

		}

		User createdU = userDAO.save(user);

		setup(createdU, userpass);

		// important. otherwise we were getting directed to the user page in a logged in, but not
		// authenticated state, despite our redirect:/site/index.html
		SecurityContextHolder.getContext().setAuthentication(null);
		return createdU;
	}

	private void setup(User createdU, String userPass) throws HippoException {

		// PEND MED should we be using RunAs instead?
		// masquerade as the new user for a second while we set up their account
		Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
		TestingAuthenticationToken auth = new TestingAuthenticationToken(createdU.getUsername(),
				userPass, new GrantedAuthority[] { new GrantedAuthorityImpl("ROLE_TELLER"),
						new GrantedAuthorityImpl("ROLE_PERMISSION_LIST") });

		SecurityContextHolder.getContext().setAuthentication(auth);

		try {
			Root root = new Root(createdU);
			topicService.save(root);

			Topic movies = topicService.createNewIfNonExistent(SAMPLE_TAG_TITLE);

			Calendar c = Calendar.getInstance();

			addWithDate("Great Expectations", c, 2006, 12, 12, movies);
			addWithDate("Straight Story, The", c, 2007, 1, 14, movies);
			addWithDate("Casino Royale", c, 2007, 3, 12, movies);
			addWithDate("Bicycle Thief, The", c, 2007, 4, 14, movies);
			addWithDate("Children Of Men", c, 2007, 4, 18, movies);
			addWithDate("Paradise Now", c, 2007, 4, 27, movies);
			addWithDate("8 1/2", c, 2007, 7, 17, movies);
			addWithDate("American Graffiti", c, 2007, 7, 21, movies);


			Topic gettingStarted = topicService.createNewIfNonExistent(gm("setup.getStarted.0"));

			topicService.executeAndSaveCommand(new QuickAddEntryCommand(
					gm("setup.getStarted.entry1.0"), gm("setup.getStarted.entry1.1"),
					gettingStarted));

			topicService.executeAndSaveCommand(new QuickAddEntryCommand(
					gm("setup.getStarted.entry2.0"), gm("setup.getStarted.entry2.1"),
					gettingStarted));

			topicService.executeAndSaveCommand(new QuickAddEntryCommand(
					gm("setup.getStarted.entry3.0"), gm("setup.getStarted.entry3.1"),
					gettingStarted));

			topicService.executeAndSaveCommand(new QuickAddEntryCommand(
					gm("setup.getStarted.entry4.0"), gm("setup.getStarted.entry4.1"),
					gettingStarted));

			topicService.executeAndSaveCommand(new QuickAddEntryCommand(
					gm("setup.getStarted.entry5.0"), gm("setup.getStarted.entry5.1"),
					gettingStarted));

			topicService.executeAndSaveCommand(new QuickAddEntryCommand(
					gm("setup.getStarted.entry6.0"), gm("setup.getStarted.entry6.1"),
					gettingStarted));

		} catch (Exception e) {
			// Make sure to replace the previous authentication context
			SecurityContextHolder.getContext().setAuthentication(oldAuthentication);
			throw new HippoException(e);
		}

	}

	private void addWithDate(String name, Calendar proto, int year, int month, int day, Topic parent)
			throws HippoException {
		proto.set(year, month, day);
		Topic topic = topicService.createNewIfNonExistent(name, RealTopic.class, parent, null,
				proto.getTime());
		topicService.executeAndSaveCommand(new SaveDatesCommand(topic, proto.getTime()));
	}

	private String gm(String messageName) {
		return messageSource.getMessage(messageName, null, null);
	}

	public void changeToSubscriptionAndSave(User user, Subscription subscription, String paypalID) {

		user.setPaypalID(paypalID);

		user.setSubscription(subscription);
		userDAO.save(user);
		userCache.removeUserFromCache(user.getUsername());
	}


	/**
	 * Remove the paypal ID so they can switch to a different paypal account by cancelling, then
	 * re-signing up.
	 */
	public void subscriptionCancel(String paypalID) {
		Subscription cancelled = userDAO.getSubscriptionByID(CANCELLED_SUBSCRIPTION_ID);

		User user = userDAO.getForPaypalID(paypalID);

		changeToSubscriptionAndSave(user, cancelled, "");
		userCache.removeUserFromCache(user.getUsername());
	}

	/**
	 * TODO do something. NOTE: this req will often be received _before_ subscriptionNewSignup
	 * 
	 */
	public void subscriptionRecordPayment(long hippoID, String paypalID) {

		User userWithThisPaypal = userDAO.getForPaypalID(paypalID);
		User userToProcess = userDAO.getUserForId(hippoID);

		log.info(userToProcess.getId() + " " + userToProcess.getUsername() + " " + paypalID
				+ " PAID!");

	}

	/**
	 * Currently blocking signups for multiple users from 1 paypal account. This is because on
	 * cancel, we only receive the paypalID and if there are multiples, we wouldn't know who to
	 * cancel.
	 */
	public void subscriptionNewSignup(long hippoID, String paypalID, long subscriptionID,
			String userEmail) throws HippoBusinessException {

		User userToProcess = userDAO.getUserForId(hippoID);

		User otherUserWithThisPaypal = userDAO.getForPaypalID(paypalID);

		if (otherUserWithThisPaypal != null) {
			throw new HippoBusinessException("Already Have a user for that paypalID");
		}


		Subscription subscription = userDAO.getSubscriptionByID(subscriptionID);

		changeToSubscriptionAndSave(userToProcess, subscription, paypalID);

		userCache.removeUserFromCache(userToProcess.getUsername());
	}

	public List<Subscription> getAllUpgradeSubscriptions() {
		return userDAO.getAllUpgradeSubscriptions();
	}

	/**
	 * don't let it go negative
	 */
	public void addInvitationsTo(User inviter, int num) {
		int current = inviter.getInvitations();
		int newV = current + num;
		if (newV >= 0) {
			inviter.setInvitations(newV);
		}
		userDAO.save(inviter);

		userCache.removeUserFromCache(inviter.getUsername());
	}

	public boolean nowAcceptingSignups() {
		return userDAO.getUserCount() < maxUsers;
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


	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	public void setDeliciousUpdate() {
		User u = getCurrentUser();
		Date now = new Date();
		u.setLastDeliciousDate(now);
		log.debug("now " + now);
	}

	public void setGoogleAppsUpdate() {
		User u = getCurrentUser();
		Date now = new Date();
		u.setLastGoogleAppsDate(now);
		log.debug("now " + now);
	}

	@Required
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Required
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	@Required
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Required
	public void setSaltSource(SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	/**
	 * avoid circular reference problems by loading this way
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		setTopicService((TopicService) applicationContext.getBean("topicService"));
	}



}
