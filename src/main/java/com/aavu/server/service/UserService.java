package com.aavu.server.service;

import java.util.Date;
import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;

import com.aavu.client.domain.Subscription;
import com.aavu.client.domain.User;
import com.aavu.client.exception.DuplicateUserException;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.web.domain.CreateUserRequestCommand;

public interface UserService {

	User createUser(CreateUserRequestCommand comm) throws DuplicateUserException, HippoException;

	User createUser(String user, String pass, String email, boolean superV) throws HippoException;

	User createUser(String user, String pass, String email, boolean superV, Date dateCreated)
			throws HippoException;

	void delete(Integer id) throws PermissionDeniedException;

	List<Subscription> getAllUpgradeSubscriptions();

	List<User> getAllUsers();

	User getCurrentUser() throws UsernameNotFoundException;

	User getUserWithNormalization(String username);

	boolean exists(String username);

	void subscriptionCancel(String paypalID);

	void subscriptionNewSignup(long hippoUserID, String paypalID, long subscriptionID,
			String payerEmail) throws HippoBusinessException;

	void subscriptionRecordPayment(long hippoID, String paypalID);

	void addInvitationsTo(User inviter, int num);

	void toggleEnabled(Integer id) throws PermissionDeniedException;

	void toggleSupervisor(Integer id) throws PermissionDeniedException;

	boolean nowAcceptingSignups();

	boolean couldBeOpenID(String openIDusername);

	void setDeliciousUpdate();

	void setGoogleAppsUpdate();



}
