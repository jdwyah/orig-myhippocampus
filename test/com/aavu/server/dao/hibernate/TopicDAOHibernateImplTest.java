package com.aavu.server.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;

public class TopicDAOHibernateImplTest extends HibernateTransactionalTest {

	private TopicDAO topicDAO;
	private TagDAO tagDAO;
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
		
		
		
		System.out.println("META: "+saved.getMetaValueStrs().getClass());
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
		String B = "Ssds45t";
		String C = "ASR#35rf";
		
		User u = userDAO.getUserByUsername("vpech");

				
		Topic t = new Topic();
		t.setText(B);
		t.setTitle(C);
		t.setUser(u);
		
		
		
		
		Tag tag = new Tag();
		tag.setName("testtagAAA");
		
		tagDAO.save(tag);
		
		t.getTags().add(tag);
		
		System.out.println("before: "+t.getId());
		
		topicDAO.save(t);
		
		System.out.println("after: "+t.getId());
		
		List<Topic> savedL = topicDAO.getAllTopics(u);
		
		System.out.println("A");
		
		assertEquals(1, savedL.size());
		
		System.out.println("B");
		
		Topic saved = savedL.get(0);
		
		System.out.println("C");
		
		System.out.println(saved.toPrettyString());
		
		//t.setTags(tags);
		
		
	}
	
	public void testSaveComplexMetas() {
		String B = "Ssds45t";
		String C = "ASR#35rf";
		
		User u = userDAO.getUserByUsername("test");

				
		Topic t = new Topic();
		t.setText(B);
		t.setTitle(C);
		t.setUser(u);
		
		
		
		
		Tag tag = new Tag();
		tag.setName("testtagAAA");
				
		
		MetaText mt = new MetaText();
		mt.setName(B);
		tag.getMetas().add(mt);
		
		
		tagDAO.save(tag);
		
		
		t.getMetaValueStrs().put("3", "C");
		
		
		t.getTags().add(tag);
		
		System.out.println("before: "+t.getId());
		
		topicDAO.save(t);
		
		System.out.println("after: "+t.getId());
		
		List<Topic> savedL = topicDAO.getAllTopics(u);
		
		System.out.println("A");
		
		//assertEquals(1, savedL.size());
		
		System.out.println("B");
		
		
		for(Topic saved : savedL){
		//Topic saved = savedL.get(0);
		
		System.out.println("C");
		
		for (Iterator iter = saved.getMetaValueStrs().keySet().iterator(); iter.hasNext();) {
			String element = (String) iter.next();
			System.out.println("elem "+element.getClass()+" "+element);
		}
		
		System.out.println(saved.toPrettyString());
		}
		//t.setTags(tags);
		
		
	}
	
	public void testGetTopicsWithTag(){
		
		String B = "Ssds45t";
		String C = "ASR#35rf";
		
		User u = userDAO.getUserByUsername("test");
				
		Topic t = new Topic();
		t.setText(B);
		t.setTitle(C);
		t.setUser(u);
				
		Topic t2 = new Topic();
		t2.setText(C);
		t2.setTitle(B);
		t2.setUser(u);
		
		Tag tag = new Tag();
		tag.setName("testtagAAA");					
		
		tagDAO.save(tag);
		
		t.getTags().add(tag);
		
		System.out.println("before: "+t.getId());
		
		topicDAO.save(t);
		topicDAO.save(t2);
		
		System.out.println("after: "+t.getId());
		
		List<Topic> savedL = topicDAO.getTopicsWithTag(tag);
		
		System.out.println(savedL.get(0));
		System.out.println("b: " +t.toPrettyString());
		
		System.out.println(((Topic)savedL.get(0)).toPrettyString());
		
		
		assertEquals(1, savedL.size());
		assertEquals(t, savedL.get(0));
		
		Topic b = savedL.get(0);
		assertNotNull(b.getUser());
		
		System.out.println("A");
	}

	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

}
