package com.aavu.server.dao.db4o;

import java.util.List;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.db4ospring.support.Db4oDaoSupport;
import org.springframework.dao.DataAccessException;

import com.aavu.client.domain.Topic;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class UserDAOImpl extends Db4oDaoSupport implements UserDAO, UserDetailsService {
	private static final Logger log = Logger.getLogger(UserDAOImpl.class);

	public ServerSideUser loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {

//		Query q1 = getDb4oTemplate().query();
//		q1.constrain(ServerSideUser.class);
//		ObjectSet s2 = q1.execute();
//		System.out.println("total serversideusers: "+s2.size());
//		for(int i=0;i<s2.size();i++){
//			Object o = s2.get(i);
//			System.out.println("FOUND "+o);
//			ServerSideUser u = (ServerSideUser) o;
//			System.out.println("name "+u.getUsername());
//			u.setSupervisor(true);
//			save(u);
//		}

	
		
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
			
//			ServerSideUser user = (ServerSideUser) users.get(0);
//			
//			Query q1 = getDb4oTemplate().query();
//			q1.constrain(Topic.class);
//			ObjectSet s2 = q1.execute();
//			System.out.println("total topics: "+s2.size());
//			for(int i=0;i<s2.size();i++){
//				Object o = s2.get(i);
//				System.out.println("Setting user for "+o);
//				Topic u = (Topic) o;				
//				u.setUser(user);								
//				getDb4oTemplate().set(u);								
//			}
			
			return (ServerSideUser) users.get(0);
		}

	}

	public void save(ServerSideUser user) {
		if(user.getId() == 0){
			getDb4oTemplate().set(user);
			long id = getDb4oTemplate().getID(user);
			System.out.println("was 0, now "+id);				
			user.setId(id);							
		}else{
			getDb4oTemplate().bind(user, user.getId());			
		}
		
		getDb4oTemplate().set(user);	
	}

	public List<ServerSideUser> getAllUsers() {
		return getDb4oTemplate().query(new Predicate<ServerSideUser>() {
			public boolean match(ServerSideUser user) {
				return true;
			}
		});
	}

	public ServerSideUser getUserForId(final Integer id) {
		
		return (ServerSideUser) Db4oUtil.getUniqueRes(getDb4oTemplate().query(new Predicate<ServerSideUser>() {
			public boolean match(ServerSideUser user) {
				return user.getId() == id;
			}
		}));
	}

	public void delete(ServerSideUser user) {
		
		if(user.getId() != 0){						
			getDb4oTemplate().bind(user, user.getId());			
		}else{
			log.warn("tried to delete user ID 0 ");
		}
		
		getDb4oTemplate().delete(user);	
	}




}
