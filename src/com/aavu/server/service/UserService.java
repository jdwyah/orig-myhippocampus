package com.aavu.server.service;

import java.util.List;

import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.exception.DuplicateUserException;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public interface UserService {

	void createUser(CreateUserRequestCommand comm) throws DuplicateUserException;

	boolean isUnique(CreateUserRequestCommand comm);

	List<ServerSideUser> getAllUsers();

	void toggleEnabled(Integer id);

	void toggleSupervisor(Integer id);

	void delete(Integer id);

}
