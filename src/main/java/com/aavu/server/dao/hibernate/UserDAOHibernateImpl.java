package com.aavu.server.dao.hibernate;

import java.util.List;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.cas.CasAuthoritiesPopulator;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Subscription;
import com.aavu.client.domain.User;
import com.aavu.server.dao.InitDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.UserService;

public class UserDAOHibernateImpl extends HibernateDaoSupport implements UserDAO, UserDetailsService, CasAuthoritiesPopulator {
	
	private static final Logger log = Logger.getLogger(UserDAOHibernateImpl.class);
	
	private boolean init;
	private UserService userService;
	private InitDAO initDAO;
	
	public void delete(User user) {
		getHibernateTemplate().delete(user);
	}

	public List<User> getAllUsers() {
		return getHibernateTemplate().find("from User");
	}

	/**
	 * Uses username.toLowerCase()
	 */
	public User getUserByUsername(String username) throws UsernameNotFoundException {		
		//Hack to run a 1-time initialization of the DB
		//
		if(init){			
			initDAO.doInit();
			init = false;
		}
		log.debug("Inited");
		
		List<User> list = getHibernateTemplate().findByNamedParam("from User where username = :name", "name", username.toLowerCase());
		log.debug("list "+list);
		log.debug("list "+list.size());
		
		if(list.size() != 1){
			if(!username.equals("anonymousUser")){
				log.debug("getUserByUsername UsernameNotFoundException "+list.size()+" users for "+username);
			}
			throw new UsernameNotFoundException("Username not found or duplicate.");
		}else{			
			log.debug("load user success "+list.get(0));
			User u = (User) list.get(0);
			log.debug("user: "+u);
			log.debug("u "+u.getUsername());
			return u;
		}
		
	
	//	return (User) DataAccessUtils.uniqueResult(getHibernateTemplate().findByNamedParam("from User where username = :name", "name", username));
	}

	public User getUserForId(long id) {
		log.debug("DAOhere user for id "+id);
		return (User) DataAccessUtils.uniqueResult(getHibernateTemplate().findByNamedParam("from User where id = :id", "id", id));
	}

	/**
	 * Uses username.toLowerCase()
	 */
	public ServerSideUser loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		log.debug("here");
		if(username == null){
			throw new UsernameNotFoundException("Username null not found");
		}
		List<User> users = getHibernateTemplate().findByNamedParam("from User where username = :name", "name", username.toLowerCase());

		log.debug("Found "+users.size()+" users for username "+username);

		if(users.size() != 1){			
			if(users.size() != 0){
				throw new UsernameNotFoundException("Duplicate Username Problem: "+username);
			} else {
				throw new UsernameNotFoundException("Username not found: "+username);
			}
		}else{			
			log.debug("load user success "+users.get(0));
			User u = (User) users.get(0);
			return new ServerSideUser(u);
		}
	}

	/**
	 * Save, ensuring that newly created users have the 'none' subscription first.  
	 * (non-Javadoc)
	 * @see com.aavu.server.dao.UserDAO#save(com.aavu.client.domain.User)
	 */
	public User save(User user) {
		log.warn(" "+user+" "+user.getSubscription());
		
		if(user.getSubscription() == null){
			log.warn("go get subscription!");
			Subscription none = (Subscription) DataAccessUtils.requiredSingleResult(getHibernateTemplate().find("from Subscription where id = 1"));			
			user.setSubscription(none);
		}else{
			log.warn("SUB: "+user.getSubscription().getId()+" "+user.getSubscription().getDescription()+" "+user.getSubscription().getMaxTopics());
		}
		getHibernateTemplate().saveOrUpdate(user);
		return user;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setInitDAO(InitDAO initDAO) {
		this.initDAO = initDAO;
	}

	public User getForPaypalID(String paypalID) {
		return (User) DataAccessUtils.uniqueResult(getHibernateTemplate().findByNamedParam("from User where paypalId = :id", "id", paypalID));
	}

	public Subscription getSubscriptionByID(long subscriptionID) {
		return (Subscription) DataAccessUtils.uniqueResult(getHibernateTemplate().findByNamedParam("from Subscription where id = :id", "id", subscriptionID));
	}

	public List<Subscription> getAllSubscriptions() {
		return getHibernateTemplate().find("from Subscription");
	}
	public List<Subscription> getAllUpgradeSubscriptions() {
		return getHibernateTemplate().find("from Subscription where maxTopics > "+User.PREMIUM_CUTOFF);
	}

	public UserDetails getUserDetails(String username) throws AuthenticationException {
		log.info("getting userdetails "+username);
//		try {
			return loadUserByUsername(username);	
//		} catch (UsernameNotFoundException e) {
//			log.info("OpenID login "+username+" creating temp user.");
//			User u = new User();
//			u.setUsername(username);
//			return new ServerSideUser(u);
//		}
		
	}

	/**
	 * use iterate() to avoid returning rows. Hibernate ref "11.13. Tips & Tricks"
	 * 
	 * grrrrr... started throwing a classcastexception, but not repeatable..
	 */
	public long getUserCount() {		
		try{
			return (Long)getHibernateTemplate().iterate("select count(*) from User").next();
		}catch (ClassCastException e) {
			log.error(e.getMessage());
			return 10000;
		}
	}

	

}
