package com.aavu.server.dao.db4o;

import java.util.List;

import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.db4ospring.support.Db4oDaoSupport;
import org.springframework.dao.DataAccessException;

import com.aavu.client.domain.User;
import com.aavu.server.dao.InitDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.ServerSideUser;
import com.db4o.query.Predicate;

public class UserDAOdb4oImpl extends Db4oDaoSupport implements UserDAO, UserDetailsService {
	private static final Logger log = Logger.getLogger(UserDAOdb4oImpl.class);

	private InitDAO initDAO;	
	public void setInitDAO(InitDAO initDAO) {
		this.initDAO = initDAO;
	}
	private boolean init;	
	public void setInit(boolean init) {
		this.init = init;
	}

	public ServerSideUser loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {

		List <User> users = getDb4oTemplate().query(new Predicate<User>() {
			public boolean match(User user) {
				return user.getUsername().equals(username);
			}
		});

		log.debug("Found "+users.size()+" users for username "+username);

		if(users.size() != 1){
			System.out.println("UsernameNotFoundException "+users.size()+" users.");
			throw new UsernameNotFoundException("Username not found or duplicate.");
		}else{			
			log.debug("load user success "+users.get(0));
			User u = (User) users.get(0);
			return new ServerSideUser(u);
		}

	}

	public void save(User user) {

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

	public List<User> getAllUsers() {
		return getDb4oTemplate().query(new Predicate<User>() {
			public boolean match(User user) {
				return true;
			}
		});
	}

	public User getUserForId(final long id) {
		
		return (User) Db4oUtil.getUniqueRes(getDb4oTemplate().query(new Predicate<User>() {
			public boolean match(User user) {
				return user.getId() == id;
			}
		}));
	}

	public void delete(User user) {
		
		if(user.getId() != 0){						
			getDb4oTemplate().bind(user, user.getId());			
		}else{
			log.warn("tried to delete user ID 0 ");
		}
		
		getDb4oTemplate().delete(user);	
	}

	public User getUserByUsername(final String username) {
		//Hack to run a 1-time initialization of the DB
		//
		if(init){			
			initDAO.doInit();
			init = false;
		}
		
		
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

	
		
		List <User> users = getDb4oTemplate().query(new Predicate<User>() {
			public boolean match(User user) {
				return user.getUsername().equals(username);
			}
		});

		log.debug("Found "+users.size()+" users for username "+username);

		if(users.size() != 1){
			System.out.println("UsernameNotFoundException "+users.size()+" users.");
			throw new UsernameNotFoundException("Username not found or duplicate.");
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
			
			log.debug("load user success "+users.get(0));
			User u = (User) users.get(0);
			return u;
		}
	}




}
