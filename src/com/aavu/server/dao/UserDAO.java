package com.aavu.server.dao;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.aavu.server.domain.ServerSideUser;

public interface UserDAO {	
	ServerSideUser loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException;
	void save(ServerSideUser user);
}
