package com.aavu.server.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.User;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;

public class UserDAOHibernateImplTest extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(UserDAOHibernateImplTest.class);

	private UserDAO userDAO;
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void testDelete() {
		
		fail("Not yet implemented");
	}

	public void testGetUserByUsername() {
		String USER = "jdwyah";
		User u = userDAO.getUserByUsername(USER);		
		assertEquals(USER, u.getUsername());
		
		
	}

	public void testGetUserForId() {
		fail("Not yet implemented");
	}

	public void testLoadUserByUsername() {
		String USER = "jdwyah";
		ServerSideUser u = userDAO.loadUserByUsername(USER);		
		assertEquals(USER, u.getUsername());
		
	}

	public void testSave() {
		String A = "dsafd";
		
		User u = new User();
		u.setUsername(A);
		
		List<User> list = userDAO.getAllUsers();
		
		
		userDAO.save(u);
		
		User saved = userDAO.getUserByUsername(A);
		
		assertEquals(A, saved.getUsername());
		assertNotSame(0, saved.getId());
		
		List<User> listPost = userDAO.getAllUsers();
		
		assertEquals(listPost.size(), list.size() +1);
		log.debug("User list size "+list.size());
	}

}
