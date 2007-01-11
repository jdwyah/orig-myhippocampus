package com.aavu.server.dao.hibernate;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.server.dao.InitDAO;
import com.aavu.server.service.UserService;

public class InitDAOHibernateImpl extends HibernateDaoSupport implements InitDAO {
	private static final Logger log = Logger.getLogger(InitDAOHibernateImpl.class);
	
	private UserService userService;
	
	public void doInit() {
		try {
			log.debug("doInit");

//			userService.createUser("test","test",false);
//			userService.createUser("jdwyah","jdwyah",true);
//			userService.createUser("vpech","vpech",true);
			
		} catch (Exception e) {
			log.error("Failed doInit "+e);
		}
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
