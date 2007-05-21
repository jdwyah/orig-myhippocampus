package com.aavu.server.service.impl;



import java.util.Date;
import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.aavu.client.domain.Entry;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.MessageServiceReturn;
import com.aavu.server.domain.PersistedFile;
import com.aavu.server.service.FilePersistanceService;
import com.aavu.server.service.MessageService;

public class MessageServiceImpl implements MessageService {
	private static final Logger log = Logger.getLogger(MessageServiceImpl.class);

	private static final String INBOX = "Inbox";

	private UserDAO userDAO;
	private FilePersistanceService fileService;
	
	
	/**
	 * Doesn't use Topic Service bc that uses the SecurityContext getCurrentUser() and we're not really logged in
	 */
	private TopicDAO topicDAO;
	/**
	 * Doesn't use Topic Service bc that uses the SecurityContext getCurrentUser() and we're not really logged in
	 */
	private TagDAO tagDAO;

	private Tag createTagIfNeeded(String tagName,User user) throws HippoBusinessException {
		log.debug("load tag named: "+tagName);

		Tag rt = tagDAO.getTag(user,tagName);
		if(null == rt){
			log.debug("was null, creating ");
			Tag t = new Tag();
			t.setName(tagName);
			t.setPublicVisible(false);
			t.setUser(user);
			topicDAO.save(t);

			log.debug("created: "+t.getId());
			return t;
		}else{
			log.debug("existed "+rt.getId()+"\n"+rt);

			return rt;
		}
	}

	/**
	 * Currently we create an island called "Inbox", create a topic 
	 * with title = subject 
	 * &
	 * body = message
	 * 
	 * Tag it and that's it.
	 * 
	 * TODO Natural language parse the message
	 * TODO Avoid name conflicts gracefully since Title must be unique. "(2)"
	 * TODO I18N & Personalize
	 * TODO rollback transaction on failures
	 */
	public MessageServiceReturn processMail(String username, String subject, String text, List<PersistedFile> attachements) {
	
		log.debug("Process Message for: |"+username+"|");

		
		try {
			User u = userDAO.getUserForEmailAddress(username);

			log.debug("Subject: "+subject);
			log.debug("Text: "+text);

			/*
			 * NOTE not using the TopicService means we need to be more careful about what we're trying to save,
			 * setting Users, publicly visible etc.
			 */
			
			

			/*
			 * Avoid old topics with the same name by iterating, checking and adding to the title.
			 */
			Topic oldTopic = null;
			String newTitle = subject;
			int i = 0;
			do{
				oldTopic = topicDAO.getForName(u, newTitle);
				if(oldTopic != null){
					log.debug("Collision! "+newTitle);
					newTitle = subject+"("+i+")";
					i++;
				}
			}while(oldTopic != null);
				
			
			Topic topic = new Topic(u,newTitle);
			topic.setLastUpdated(new Date());
			
			Entry entry = topic.getLatestEntry();
			entry.setUser(u);
			entry.setInnerHTML(text);
			
			Tag inbox = createTagIfNeeded(INBOX,u);
			
			topic.tagTopic(inbox);
			
			topic = topicDAO.save(topic);
			
			for (PersistedFile file : attachements) {	
				log.debug("Saving attachement "+file+" "+file.getFilename());
				fileService.saveFileToTopic(file, topic, u);				
			}			
			
			
			return new MessageServiceReturn(true,"Success!",topic.getId());

		} catch (UsernameNotFoundException e) {
			log.warn("User Exception "+e.getMessage());
			
			//TODO replace with declarative based on exception type 
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return new MessageServiceReturn(false,"Couldn't find user: "+username,-1);
		} catch (HippoException e) {
			log.warn("HippoException "+e.getMessage());
			e.printStackTrace();
			
			//TODO replace with declarative based on exception type
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return new MessageServiceReturn(false,"HippoException "+e.getMessage(),-1);
		}

	}

	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}

	public void setFileService(FilePersistanceService fileService) {
		this.fileService = fileService;
	}

	

}
