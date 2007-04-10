package com.aavu.server.service;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;

import com.aavu.client.domain.User;
import com.aavu.client.exception.DuplicateUserException;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public interface UserService {

	void createUser(CreateUserRequestCommand comm) throws DuplicateUserException;

	boolean isUnique(CreateUserRequestCommand comm);

	List<User> getAllUsers();

	void toggleEnabled(Integer id);

	void toggleSupervisor(Integer id);

	User getCurrentUser() throws UsernameNotFoundException;
	
	void delete(Integer id);

	void createUser(String user, String pass, boolean superV);

}
