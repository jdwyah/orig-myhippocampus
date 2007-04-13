package com.aavu.server.dao;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.aavu.client.domain.Subscription;
import com.aavu.client.domain.User;
import com.aavu.server.domain.ServerSideUser;

public interface UserDAO {	
	void delete(User user);
	List<User> getAllUsers();
	User getForPaypalID(String paypalID);
	Subscription getSubscriptionByID(long subscriptionID);
	User getUserByUsername(String username) throws UsernameNotFoundException ;
	User getUserForId(long id);
	
	ServerSideUser loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException;
	void save(User user);
}
