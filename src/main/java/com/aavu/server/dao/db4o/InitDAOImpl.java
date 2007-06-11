package com.aavu.server.dao.db4o;

import org.apache.log4j.Logger;
import org.db4ospring.support.Db4oDaoSupport;

import com.aavu.server.dao.InitDAO;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public class InitDAOImpl extends Db4oDaoSupport implements InitDAO {
	private static final Logger log = Logger.getLogger(InitDAOImpl.class);

	private UserService userService;
	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public void doInit() {
		try {
			log.debug("doInit");

			userService.createUser("test","test","j@d.com",false);
			userService.createUser("jdwyah","jdwyah","j@d.com",true);			
			
		} catch (Exception e) {
			log.error("Failed doInit "+e);
		}
	}

}
