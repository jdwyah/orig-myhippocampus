package com.aavu.server.service;

import com.aavu.server.domain.MessageServiceReturn;

public interface MessageService {

	public MessageServiceReturn processMail(String username, String subject, String text);
	
}
