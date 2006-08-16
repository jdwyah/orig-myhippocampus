package com.aavu.server.dao;

import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.aavu.server.domain.ServerSideUser;

public interface UserDAO {	
	ServerSideUser loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException;
	void save(ServerSideUser user);
	List<ServerSideUser> getAllUsers();
	ServerSideUser getUserForId(Integer id);
	void delete(ServerSideUser user);
}
