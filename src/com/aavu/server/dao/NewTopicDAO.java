package com.aavu.server.dao;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;

public interface NewTopicDAO {

	void test(User user);
	
	Topic save(Topic topic);
	Topic get(long id);
}