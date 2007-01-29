package com.aavu.server.service.impl;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.MetaTopic;
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
import com.aavu.server.service.gwt.BaseTestNoTransaction;
import com.aavu.server.service.gwt.BaseTestWithTransaction;

import junit.framework.TestCase;

public class TopicServiceImplTest extends BaseTestNoTransaction {
	

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
	protected void onSetUp() throws Exception {
		super.onSetUp();
		u = userService.getCurrentUser();		
	}
//
//	@Override
//	protected void onSetUpBeforeTransaction() throws Exception {		
//		super.onSetUpBeforeTransaction();
//		setUsername("junit");
//	}
//	@Override
//	protected void onSetUpInTransaction() throws Exception {
//
//		super.onSetUpInTransaction();
//
//		
//	}
	

	/**
	 * NOTE; same code as DAO test, but with service.
	 * Different result, since we set the tag's user in the service layer.
	 * 
	 * @throws HippoBusinessException
	 */
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

		assertEquals(2, savedL.size());		

		TopicIdentifier saved = savedL.get(1);

		Topic savedTopic = topicService.getForID(saved.getTopicID());

		assertEquals(C, savedTopic.getTitle());
		assertEquals(B, savedTopic.getLatestEntry().getData());
		assertEquals(u, savedTopic.getUser());

		assertEquals(1, savedTopic.getTypesAsTopics().size());

		Topic savedTag = (Topic) savedTopic.getTypesAsTopics().iterator().next();

		assertEquals(D,	savedTag.getTitle());

		//Tag's user will not be initialized
		assertEquals(u, savedTag.getUser());
		
		topicService.delete(savedTopic);
		topicService.delete(savedTag);
		
				
	}

	public void __testSaveComplexMetas() throws HippoBusinessException {

		Topic patriotGames = new Topic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Tag book = new Tag(u,D);		

		MetaText ss = new MetaText();
		MetaTopic author = new MetaTopic();
		
		author.setTitle(B);
		author.setUser(u);
		book.addMeta(author);

		topicService.save(book);

		Topic tomClancy = new Topic();
		tomClancy.setTitle(E);
		topicService.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: "+patriotGames.getId());

		topicService.save(patriotGames);

		System.out.println("after: "+patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<TopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		//NOTE: getAllTopics doesn't take a User right now. That functionality was only used here.
		//List<Topic> allTopics = topicDAO.getAllTopics();				
		//Book, Author, PatGames, PatGames->Author, Book->Author
		//assertEquals(5,allTopics.size());		
		//assertEquals(5, allTopics.size());

		assertEquals(4, savedL.size());


		assertNotSame(0,patriotGames.getId());		
		Topic savedPatriotGames = topicService.getForID(patriotGames.getId());
		assertNotNull(savedPatriotGames);

		assertEquals(1, savedPatriotGames.getTypesAsTopics().size());		
		Topic savedBookTag = (Topic) savedPatriotGames.getTypesAsTopics().iterator().next();
		assertEquals(D, savedBookTag.getTitle());
		assertEquals(1, savedBookTag.getMetas().size());

		assertEquals(1, savedPatriotGames.getMetaValuesFor(author).size());
		assertEquals(0, savedPatriotGames.getMetas().size());



		Topic savedTomClancy = (Topic) savedPatriotGames.getSingleMetaValueFor(author);
		assertNotNull(savedTomClancy);


		assertEquals(E,savedTomClancy.getTitle());


		//assertEquals(savedBookTag., false)


		for(TopicIdentifier saved : savedL){
			//Topic saved = savedL.get(0);

			System.out.println("C");
			Topic top = topicService.getForID(saved.getTopicID());

			for (Iterator iter = top.getMetaValuesFor(author).iterator(); iter.hasNext();) {
				Topic element = (Topic) iter.next();
				System.out.println("elem "+element.getClass()+" "+element);
			}

			System.out.println(top.toPrettyString());
		}
		//t.setTags(tags);
	

	}
	public void testUpdatingTopic() throws HippoBusinessException {
	
		clean();
		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Tag tag = new Tag();
		tag.setName(D);

		tag = (Tag) topicService.save(tag);

		t.tagTopic(tag);

		System.out.println("before: "+t.getId());

		topicService.save(t);

		System.out.println("after: "+t.getId());

		List<TopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		assertEquals(2, savedL.size());		

		TopicIdentifier saved = savedL.get(1);
		
		
		Topic savedTopic = topicService.getForID(saved.getTopicID());
		
		savedTopic.setTitle(E);
		
		Topic secondSave = topicService.save(savedTopic);
		
		assertEquals(E, secondSave.getTitle());
		
		
		//order matters. tag is not current.
		topicService.delete(secondSave);
		topicService.delete(tag);
		
	}
	
	
	private void clean() throws HippoBusinessException {
		List<TopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		for (TopicIdentifier identifier : savedL) {
			log.debug("Clean: "+identifier.getTopicTitle());
			Topic t = topicService.getForID(identifier.getTopicID());
//			t.getTypes().clear();
//			t.getInstances().clear();
//			t = topicService.save(t);
			topicService.delete(t);
		}
		
	}


	/*
	public void testGetForName() {
		fail("Not yet implemented");
	}

	public void testGetTopicsStarting() {
		fail("Not yet implemented");
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
*/
}
