package com.aavu.server.dao;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.aavu.client.domain.User;
import com.aavu.server.domain.ServerSideUser;

public interface UserDAO {	
	ServerSideUser loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException;
	void save(User user);
	List<User> getAllUsers();
	User getUserForId(long id);
	void delete(User user);
	User getUserByUsername(String username) throws UsernameNotFoundException ;
}
