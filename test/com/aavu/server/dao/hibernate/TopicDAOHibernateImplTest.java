package com.aavu.server.dao.hibernate;

import java.util.List;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;

public class TopicDAOHibernateImplTest extends HibernateTransactionalTest {

	private TopicDAO topicDAO;

	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}
	
	private UserDAO userDAO;



	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void testGetAllTopics() {
		
		String B = "Ssds45t";
		String C = "ASR#35rf";
		
		User u = userDAO.getUserByUsername("vpech");

		Topic t = new Topic();
		t.setText(B);
		t.setTitle(C);
		t.setUser(u);

		topicDAO.save(t);

		List<Topic> list = topicDAO.getAllTopics(u);
		
		assertEquals(1,list.size());
		
		Topic saved = list.get(0);
		
		assertEquals(B, saved.getText());
		assertEquals(C, saved.getTitle());
		
		assertEquals(u, saved.getUser());
		
		
		
		System.out.println("META: "+saved.getMetaValues().getClass());
		System.out.println("TAGS: "+saved.getTags().getClass());
		
		
		
	}

	public void testGetBlogTopics() {
		fail("Not yet implemented");
	}

	public void testGetForName() {
		fail("Not yet implemented");
	}

	public void testGetTopicsStarting() {
		fail("Not yet implemented");
	}

	public void testSave() {
		fail("Not yet implemented");
	}

}
