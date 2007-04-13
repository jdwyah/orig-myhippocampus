package com.aavu.server.service;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;

import com.aavu.client.domain.Subscription;
import com.aavu.client.domain.User;
import com.aavu.client.exception.DuplicateUserException;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.domain.ServerSideUser;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public interface UserService {

	void createUser(CreateUserRequestCommand comm) throws DuplicateUserException;

	boolean isUnique(CreateUserRequestCommand comm);

	List<User> getAllUsers();

	void toggleEnabled(Integer id) throws PermissionDeniedException;

	void toggleSupervisor(Integer id) throws PermissionDeniedException;

	User getCurrentUser() throws UsernameNotFoundException;
	
	void delete(Integer id) throws PermissionDeniedException;

	void createUser(String user, String pass, boolean superV);
	 	

	void subscriptionNewSignup(long hippoUserID, String paypalID, long subscriptionID, String payerEmail) throws HippoBusinessException;

	void subscriptionRecordPayment(String paypalID);

	void subscriptionCancel(String paypalID);

}
