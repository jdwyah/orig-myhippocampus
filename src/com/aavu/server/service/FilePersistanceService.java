package com.aavu.server.service;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoException;
import com.aavu.client.exception.HippoInfrastructureException;
import com.aavu.server.domain.PersistedFile;

public interface FilePersistanceService {
	String saveFile(String contentType, long topicID, byte[] file, String filename, User user) throws HippoInfrastructureException;
	PersistedFile getFile(User u, String key) throws HippoException;
	void deleteFile(User u, String key) throws HippoException;
	void saveFileToTopic(PersistedFile file, Topic topic, User u) throws HippoException;
}
