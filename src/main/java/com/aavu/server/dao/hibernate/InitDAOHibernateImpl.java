package com.aavu.server.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
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
			Root root= null;
			try{
				root = selectDAO.getRoot(user,user);
			}catch (Exception e) {
				root = new Root(user);
				log.warn("No Root for "+user+" creating.");
				getHibernateTemplate().save(root);								
			}
			
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
	
	
	
	public void convertToAllTopics() {
		List<User> users = userService.getAllUsers();
		
		for (User user : users) {
		
			log.info("User: "+user);
			Root root= null;
			try{
				root = selectDAO.getRoot(user,user);
			}catch (Exception e) {
				root = new Root(user);
				log.warn("No Root for "+user+" creating.");
				getHibernateTemplate().save(root);								
			}
			
			
			log.info("Root: "+root);
			
			List<DatedTopicIdentifier> topics = selectDAO.getAllTopicIdentifiers(user, false);
			
			int count = 0;
			log.info("Topics Size "+topics.size());
			
			for (DatedTopicIdentifier dti : topics) {
		
				Topic topic = selectDAO.getForID(user,dti.getTopicID());
				
				
				Set<TopicOccurrenceConnector> s = topic.getOccurences();
				
				log.info("Topic: "+topic.getId()+" Occs: "+s.size());
				
				for(TopicOccurrenceConnector toc : s){
					
					//toc.getOccurrence()
					
					Occurrence o = (Occurrence)toc.getOccurrence();
					
//					CopyOfOccurrence coo = getNewOcc(o);
//					
//					getHibernateTemplate().save(coo);
//				
//					System.out.println("post save "+coo.getId());
//					
//					toc.setOccurrence(coo);
					
					getHibernateTemplate().save(toc);
					
					
				}
				
				
			}
			
		}
		
	
	}

//	private CopyOfOccurrence getNewOcc(Occurrence o){
//		CopyOfOccurrence newOcc = null;
//		if (o instanceof Entry) {
//			Entry e = (Entry) o;
//			newOcc = new CopyOfEntry();
//		}		
//		else if(o instanceof S3File){
//			newOcc = new CopyOfS3File();	
//			CopyOfS3File c = (CopyOfS3File) newOcc;
//			c.setUri(((URI)o).getUri());
//			
//		}
//		else if(o instanceof WebLink){
//			newOcc = new CopyOfWebLink();
//			CopyOfWebLink c = (CopyOfWebLink) newOcc;
//			c.setUri(((URI)o).getUri());			
//		}
//		
//		newOcc.setCreated(o.getCreated());
//		newOcc.setData(o.getData());
//		newOcc.setTitle(o.getTitle());
//		newOcc.setUser(o.getUser());
//		newOcc.setLastUpdated(o.getLastUpdated());
//		
//		return newOcc;
//	}


	
	
	
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
