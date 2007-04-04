package com.aavu.server.web.domain.validation;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;

import com.aavu.client.domain.User;
import com.aavu.client.exception.DuplicateUserException;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.CreateUserRequestCommand;

/**
 * always returns unique == true
 * 
 * @author Jeff Dwyer
 *
 */
public class MockUserService implements UserService {

	public void createUser(CreateUserRequestCommand comm)
			throws DuplicateUserException {
		// TODO Auto-generated method stub

	}

	public void createUser(String user, String pass, boolean superV) {
		// TODO Auto-generated method stub

	}

	public void delete(Integer id) {
		// TODO Auto-generated method stub

	}

	public List<User> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	public User getCurrentUser() throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUnique(CreateUserRequestCommand comm) {
		return true;
	}

	public void toggleEnabled(Integer id) {
		// TODO Auto-generated method stub

	}

	public void toggleSupervisor(Integer id) {
		// TODO Auto-generated method stub

	}

}
