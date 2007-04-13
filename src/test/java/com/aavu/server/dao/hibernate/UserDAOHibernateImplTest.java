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

	

	public void testGetUserByUsername() {
		String USER = "jdwyah";
		User u = userDAO.getUserByUsername(USER);		
		assertEquals(USER, u.getUsername());
		
		
	}


	public void testLoadUserByUsername() {
		String USER = "test";
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
		assertFalse(saved.isSupervisor());
		assertTrue(saved.isEnabled());
		assertTrue(saved.isAccountNonExpired());
		
		
		List<User> listPost = userDAO.getAllUsers();
		
		assertEquals(listPost.size(), list.size() +1);
		log.debug("User list size "+list.size());
	}
	
	public void testEdit() {
		String A = "dsafd";
		String B = "sdfn&S*AS";
		
		User u = new User();
		u.setUsername(A);
		
		List<User> list = userDAO.getAllUsers();
				
		userDAO.save(u);
		
		User saved = userDAO.getUserByUsername(A);
		
		assertEquals(A, saved.getUsername());
		assertNotSame(0, saved.getId());
		assertFalse(saved.isSupervisor());
		assertTrue(saved.isEnabled());
		assertTrue(saved.isAccountNonExpired());
		
		List<User> listPost = userDAO.getAllUsers();
		
		assertEquals(listPost.size(), list.size() +1);
		log.debug("User list size "+list.size());
		
		
		//now do some edits
		//
		saved.setUsername(B);
		saved.setSupervisor(true);
		
		userDAO.save(saved);
		User editted = userDAO.getUserByUsername(B);
		
		assertNotNull(editted);
		assertEquals(B, editted.getUsername());
		assertSame(saved.getId(), editted.getId());
		assertTrue(saved.isSupervisor());
		assertTrue(saved.isEnabled());
		assertTrue(saved.isAccountNonExpired());
		
		
		
		
	}

}
