package com.aavu.server.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.dao.hibernate.HibernateTransactionalTest;
import com.aavu.server.dao.hibernate.TopicDAOHibernateImplTest;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;

import junit.framework.TestCase;

public class TopicServiceImplTest extends BaseTestWithTransaction {
	private static final Logger log = Logger.getLogger(TopicServiceImplTest.class);
	
//	private static final String B = "Ssds45t";
//	private static final String C = "ASR#35rf";
//	private static final String D = "234234123";
//	private static final String E = "^#*(DNS03";

	private static final String B = "Author";
	private static final String C = "PatriotGames";
	private static final String D = "Book";
	private static final String E = "TomClancy";
	private static final String F = "Recommender";
	private static final String G = "AnotherBook";
	private static final String H = "AnotherRecommender";

	private TopicService topicService;
	
	private UserService userService;

	private User u;

	

	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}




	@Override
	protected void onSetUpBeforeTransaction() throws Exception {		
		super.onSetUpBeforeTransaction();
		setUsername("test");
	}
	@Override
	protected void onSetUpInTransaction() throws Exception {

		super.onSetUpInTransaction();

		u = userService.getCurrentUser();		
	}
	
	
	public void testGetForName() {
		fail("Not yet implemented");
	}

	public void testGetTopicsStarting() {
		fail("Not yet implemented");
	}

	public void testSaveTopic() throws HippoBusinessException {
		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Tag tag = new Tag();
		tag.setName(D);

		topicService.save(tag);

		t.tagTopic(tag);

		System.out.println("before: "+t.getId());

		topicService.save(t);

		System.out.println("after: "+t.getId());

		List<TopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		assertEquals(1, savedL.size());		

		TopicIdentifier saved = savedL.get(0);

		Topic savedTopic = topicService.getForID(saved.getTopicID());

		assertEquals(C, savedTopic.getTitle());
		assertEquals(B, savedTopic.getLatestEntry().getData());
		assertEquals(u, savedTopic.getUser());

		assertEquals(1, savedTopic.getTypes().size());

		Topic savedTag = (Topic) savedTopic.getTypes().iterator().next();

		assertEquals(D,	savedTag.getTitle());

		//Tag's user will not be initialized
		assertEquals(null, savedTag.getUser());
		
	}

	public void testGetTopicIdsWithTag() {
		fail("Not yet implemented");
	}

	public void testGetAllTopicIdentifiers() {
		fail("Not yet implemented");
	}

	public void testGetForID() {
		fail("Not yet implemented");
	}

	public void testGetTimelineObjs() {
		fail("Not yet implemented");
	}

	public void testSaveTopicArray() {
		fail("Not yet implemented");
	}

	public void testSaveOccurrence() {
		fail("Not yet implemented");
	}

	public void testGetLinksTo() {
		fail("Not yet implemented");
	}

	public void testAddLinkToTags() {
		fail("Not yet implemented");
	}

	public void testDelete() {
		fail("Not yet implemented");
	}

}
