package com.aavu.server.service;

import java.util.List;

import com.aavu.server.domain.MessageServiceReturn;
import com.aavu.server.domain.PersistedFile;

public interface MessageService {

	public MessageServiceReturn processMail(String username, String subject, String text, List<PersistedFile> attachements);
	
}
