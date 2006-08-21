package com.aavu.server.dao.hibernate;

import java.util.List;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.User;
import com.aavu.server.dao.InitDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.service.UserService;

public class UserDAOHibernateImpl extends HibernateDaoSupport implements UserDAO, UserDetailsService {
	
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

	public User getUserByUsername(String username) {		
		//Hack to run a 1-time initialization of the DB
		//
		if(init){			
			initDAO.doInit();
			init = false;
		}
		log.debug("Inited");
		
		List<User> list = getHibernateTemplate().findByNamedParam("from User where username = :name", "name", username);
		log.debug("list "+list);
		log.debug("list "+list.size());
		User u = list.get(0);
		log.debug("user: "+u);
		log.debug("u "+u.getUsername());
	
		log.debug("now for real");
		return (User) DataAccessUtils.uniqueResult(getHibernateTemplate().findByNamedParam("from User where username = :name", "name", username));
	}

	public User getUserForId(Integer id) {
		log.debug("here user for id");
		return (User) DataAccessUtils.uniqueResult(getHibernateTemplate().findByNamedParam("from User where id = :id", "id", id));
	}

	public ServerSideUser loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		
		log.debug("here");
		
		List<User> users = getHibernateTemplate().findByNamedParam("from User where username = :name", "name", username);

		log.debug("Found "+users.size()+" users for username "+username);

		if(users.size() != 1){
			System.out.println("UsernameNotFoundException "+users.size()+" users.");
			throw new UsernameNotFoundException("Username not found or duplicate.");
		}else{			
			log.debug("load user success "+users.get(0));
			User u = (User) users.get(0);
			return new ServerSideUser(u);
		}
	}

	public void save(User user) {
		getHibernateTemplate().saveOrUpdate(user);
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

}
