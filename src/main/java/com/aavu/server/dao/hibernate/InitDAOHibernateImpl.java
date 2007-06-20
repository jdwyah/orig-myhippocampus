package com.aavu.server.dao.hibernate;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Root;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.InitDAO;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.service.UserService;

public class InitDAOHibernateImpl extends HibernateDaoSupport implements InitDAO {
	private static final Logger log = Logger.getLogger(InitDAOHibernateImpl.class);

	private UserService userService;
	private SelectDAO selectDAO;
	private EditDAO editDAO;
	

	public void doInit() {
		try {
			log.debug("doInit");

//			userService.createUser("test","test",false);
//			userService.createUser("jdwyah","jdwyah",true);
//			userService.createUser("vpech","vpech",true);

		} catch (Exception e) {
			log.error("Failed doInit "+e);
		}
	}

	@Required
	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}
	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Required
	public void setEditDAO(EditDAO editDAO) {
		this.editDAO = editDAO;
	}


	public void displayRootInfo() throws HippoBusinessException {

		List<User> users = userService.getAllUsers();
		
		for (User user : users) {
		
			Root root = selectDAO.getRoot(user,user);
			
		
			List<TopicTypeConnector> rootConn = selectDAO.getTopicIdsWithTag(root.getId(), user);
			
			System.out.println(user +" Root "+root);
			System.out.println(root.toPrettyString());
			System.out.println(user+" size "+rootConn.size()+" count ");
		}

	}
	
	public void upgradeRemoveTags() throws HippoBusinessException {

		List<User> users = userService.getAllUsers();
		
		for (User user : users) {
		
			log.info("User: "+user);
			Root root = selectDAO.getRoot(user,user);
			
			log.info("Root: "+root);
			
			List<DatedTopicIdentifier> topics = selectDAO.getAllTopicIdentifiers(user, false);
			
			int count = 0;
			
			for (DatedTopicIdentifier dti : topics) {
				
				Topic topic = selectDAO.getForID(user,dti.getTopicID());
				
				log.info("TI: "+topic+" Empty "+topic.getTypes().isEmpty());
				
				if(topic.getTypes().isEmpty()){
					topic.tagTopic(root);
					topic = editDAO.save(topic);
					log.info("saved");
					count++;
				}
				
			}
			
			List<TopicTypeConnector> rootConn = selectDAO.getTopicIdsWithTag(root.getId(), user);
			
			System.out.println(user+" topics "+topics.size()+" size "+rootConn.size()+" count "+count);
		}

	}
	
	
	
	
	
	
	
	
	
	
//	ll = (List<Object>) getHibernateTemplate().execute(new HibernateCallback(){
//		
//
//		public Object doInHibernate(Session sess)
//				throws HibernateException, SQLException {
//			
//			
//			return sess.createQuery("from Topics t "+
//					"join topics meta on topic_meta_values.topic_id = meta.topic_id "+
//					"join topics top on topic_meta_values.topic_meta_value_id = top.topic_id "+
//					"join topics metavalue on topic_meta_values.metaValue = metavalue.topic_id "+
//			"where t.discriminator = 'tag'")				
//			.list();
//
//		}});
	
}
