package com.aavu.server.service.impl;



import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;

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
import com.aavu.server.service.TagService;
import com.aavu.server.service.TopicService;

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

	private Tag getTagAddIfNew(String tagName,User user) throws HippoBusinessException {
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
			log.debug("existed "+rt.getId()+"\n"+rt.toPrettyString());

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
	 */
	public MessageServiceReturn processMail(String username, String subject, String text, List<PersistedFile> attachements) {
	
		log.debug("Process Message for: |"+username+"|");

		try {
			User u = userDAO.getUserByUsername(username);

			log.debug("Subject: "+subject);
			log.debug("Text: "+text);

			/*
			 * NOTE not using the TopicService means we need to be more careful about what we're trying to save,
			 * setting Users, publicly visible etc.
			 */
			Topic topic = new Topic(u,subject);
			topic.setLastUpdated(new Date());
			
			Entry entry = topic.getLatestEntry();
			entry.setUser(u);
			entry.setInnerHTML(text);
			
			Tag inbox = getTagAddIfNew(INBOX,u);
			
			topic.tagTopic(inbox);
			
			topic = topicDAO.save(topic);
			
			for (PersistedFile file : attachements) {	
				log.debug("Saving attachement "+file+" "+file.getFilename());
				fileService.saveFileToTopic(file, topic, u);				
			}
			
			return new MessageServiceReturn(true,"Success!",topic.getId());


		} catch (UsernameNotFoundException e) {
			log.warn("User Exception "+e.getMessage());
			return new MessageServiceReturn(false,"Couldn't find user: "+username,-1);
		} catch (HippoException e) {
			log.warn("HippoException "+e.getMessage());
			e.printStackTrace();
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
