package com.aavu.server.service.impl;



import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;

import com.aavu.client.domain.User;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.domain.MessageServiceReturn;
import com.aavu.server.service.MessageService;
import com.aavu.server.service.TopicService;

public class MessageServiceImpl implements MessageService {
	private static final Logger log = Logger.getLogger(MessageServiceImpl.class);

	private UserDAO userDAO;
	private TopicService topicService;



	public MessageServiceReturn processMail(String username, String subject, String text) {

		log.debug("Process Message for: "+username);

		try {
			User u = userDAO.getUserByUsername(username);


			log.debug("Subject: "+subject);
			log.debug("Text: "+text);

			return new MessageServiceReturn(true,"Success!");


		} catch (UsernameNotFoundException e) {
			return new MessageServiceReturn(false,"Couldn't find user: "+username);
		}

	}

	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}






}
