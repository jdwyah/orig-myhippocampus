package com.aavu.server.dao.db4o;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.db4ospring.support.Db4oDaoSupport;
import org.springframework.dao.DataAccessException;

import com.aavu.client.domain.User;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class UserDAOImpl extends Db4oDaoSupport implements UserDAO, UserDetailsService {


	public ServerSideUser loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {
		

//		createUser("test", "test");
//		createUser("demo", "demo");	
		
		Query q1 = getDb4oTemplate().query();
		q1.constrain(ServerSideUser.class);
		ObjectSet s2 = q1.execute();
		System.out.println("total serversideusers: "+s2.size());
		for(int i=0;i<s2.size();i++){
			Object o = s2.get(i);
			System.out.println("FOUND "+o);
			ServerSideUser u = (ServerSideUser) o;
			System.out.println("name "+u.getUsername());
			u.setEnabled(true);
			u.setCredentialsNonExpired(true);
		}
		
		 List <ServerSideUser> users = getDb4oTemplate().query(new Predicate<ServerSideUser>() {
			   public boolean match(ServerSideUser user) {
			      return user.getUsername().equals(username);
			   }
			});
		
		 System.out.println("new query size "+users.size());
		 
		if(users.size() != 1){
			System.out.println("found "+users.size()+" users.");
			throw new UsernameNotFoundException("Username not found.");
		}else{
			return (ServerSideUser) users.get(0);
		}
		
	}

	public void save(ServerSideUser user) {
		getDb4oTemplate().set(user);	
	}
	
	private  void createUser(String username,String userpass){
		
		System.out.println("u: "+username+" p "+userpass);
		System.out.println("pp: "+hashPassword(userpass));
		
		ServerSideUser user = new ServerSideUser();
		user.setUsername(username);
		user.setPassword(hashPassword(userpass));		
		
		save(user);
	}

	private String hashPassword(String password) {
		String hashword = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes());
			BigInteger hash = new BigInteger(1, md5.digest());
			hashword = hash.toString(16);			
		} catch (NoSuchAlgorithmException nsae) {
			
		}
		return hashword;
	}



}
