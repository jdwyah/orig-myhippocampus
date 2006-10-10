package com.aavu.server.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.SeeAlso;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;

public class TopicDAOHibernateImplTest extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImplTest.class);

	private static final String B = "Ssds45t";
	private static final String C = "ASR#35rf";
	private static final String D = "234234123";
	
	private TopicDAO topicDAO;
	private TagDAO tagDAO;
	private UserDAO userDAO;
	
	private User u;
	
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	


	@Override
	protected void onSetUpInTransaction() throws Exception {		
		super.onSetUpInTransaction();

		
		u = userDAO.getUserByUsername("junit");

	}


	public void testGetForName() {
		fail("Not yet implemented");
	}

	public void testGetTopicsStarting() {
		fail("Not yet implemented");
	}

	public void testSave() {
	
		Topic t = new Topic();
		t.setData(B);
		t.setTitle(C);
		t.setUser(u);
				
		Tag tag = new Tag();
		tag.setName(D);
		
		topicDAO.save(tag);
		
		t.tagTopic(tag);
		
		System.out.println("before: "+t.getId());
		
		topicDAO.save(t);
		
		System.out.println("after: "+t.getId());
		
		List<TopicIdentifier> savedL = topicDAO.getAllTopicIdentifiers(u);
				
		assertEquals(1, savedL.size());		
		
		TopicIdentifier saved = savedL.get(0);
		
		Topic savedTopic = topicDAO.getForID(u, saved.getTopicID());
		
		assertEquals(C, savedTopic.getTitle());
		assertEquals(B, savedTopic.getData());
		assertEquals(u, savedTopic.getUser());
		
		assertEquals(1, savedTopic.getTypes().size());
		
		Topic savedTag = (Topic) savedTopic.getTypes().iterator().next();
		
		assertEquals(D,	savedTag.getTitle());
		
		//Tag's user will not be initialized
		assertEquals(null, savedTag.getUser());
		
		
	}
	
	public void testSaveComplexMetas() {
						
		Topic patriotGames = new Topic();
		patriotGames.setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);
				
		Tag book = new Tag();
		book.setName(D);
						
		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		book.addMeta(author);
				
		topicDAO.save(book);
		
		Topic tomClancy = new Topic();
		tomClancy.setTitle(C);
		
		
		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);
				
		System.out.println("before: "+patriotGames.getId());
		
		topicDAO.save(patriotGames);
		
		System.out.println("after: "+patriotGames.getId());
		
		List<TopicIdentifier> savedL = topicDAO.getAllTopicIdentifiers(u);
				
		//only 1 because the user isn't set on book or author. 
		//hmm.. good and bad.. 
		assertEquals(1, savedL.size());
		
		
		assertNotSame(0,patriotGames.getId());		
		Topic savedPatriotGames = topicDAO.getForID(u, patriotGames.getId());
		assertNotNull(savedPatriotGames);
		
		assertEquals(1, savedPatriotGames.getTypes().size());		
		Topic savedBookTag = (Topic) savedPatriotGames.getTypes().iterator().next();
		assertEquals(D, savedBookTag.getTitle());
		assertEquals(1, savedBookTag.getMetas().size());
				
		assertEquals(1, savedPatriotGames.getMetaValues().size());
		assertEquals(0, savedPatriotGames.getMetas().size());
		
		
			
		Topic savedTomClancy = (Topic) savedPatriotGames.getMetaValues().get(author);
		assertNotNull(savedTomClancy);
		
					
		assertEquals(C,savedTomClancy.getTitle());
		
	
		//assertEquals(savedBookTag., false)
		
		
		for(TopicIdentifier saved : savedL){
			//Topic saved = savedL.get(0);

			System.out.println("C");
			Topic top = topicDAO.getForID(u, saved.getTopicID());

			for (Iterator iter = top.getMetaValues().keySet().iterator(); iter.hasNext();) {
				Topic element = (Topic) iter.next();
				System.out.println("elem "+element.getClass()+" "+element);
			}

			System.out.println(top.toPrettyString());
		}
		//t.setTags(tags);

		
	}
	
	public void testGetTopicsWithTag(){
		
				
		Topic t = new Topic();
		t.setData(B);
		t.setTitle(C);
		t.setUser(u);
				
		Topic t2 = new Topic();
		t2.setData(C);
		t2.setTitle(B);
		t2.setUser(u);
		
		Tag tag = new Tag();
		tag.setName("testtagAAA");					
		
		topicDAO.save(tag);
		
		t.getTags().add(tag);
		
		System.out.println("before: "+t.getId());
		
		topicDAO.save(t);
		topicDAO.save(t2);
		
		System.out.println("after: "+t.getId());
		
		List<TopicIdentifier> savedL = topicDAO.getTopicIdsWithTag(tag,u);
		
		System.out.println(savedL.get(0));
		System.out.println("b: " +t.toPrettyString());
		
		System.out.println(((TopicIdentifier)savedL.get(0)));
		
		
		assertEquals(1, savedL.size());
				
		TopicIdentifier b = savedL.get(0);
		
		assertEquals(b.getTopicID(),t.getId());
		assertEquals(b.getTopicTitle(), t.getTitle());
		
		System.out.println("A");
	}
	public void testGetAllTopicIdentifiers(){	
		
		Topic t = new Topic();
		t.setData(B);
		t.setTitle(C);
		t.setUser(u);

		topicDAO.save(t);

		List<TopicIdentifier> list = topicDAO.getAllTopicIdentifiers(u);
		
		assertEquals(1,list.size());
		
		for(TopicIdentifier tident : list){
			assertEquals(tident.getTopicTitle(),C);
		}
		
		
	}
	
	public void testSaveSeeAlsos(){
		
		Topic stringTheory = new Topic(u,B);
		

		stringTheory = topicDAO.save(stringTheory);
		
		Topic feinman = new Topic(u,C);		
		
		feinman = topicDAO.save(feinman);
		
		
		SeeAlso see = new SeeAlso();
		see.add(stringTheory.getIdentifier());
		see.add(feinman.getIdentifier());
		
		
		Topic savedST = topicDAO.save(see);
				
		SeeAlso savedSee = (SeeAlso) savedST;
		
		assertTrue(savedSee.getMembers().values().contains(stringTheory));
		assertTrue(savedSee.getMembers().values().contains(feinman));
		
//		System.out.println(savedSee.getFrom().compare(stringTheory));
//		
//		System.out.println(savedSee.getTo().compare(feinman));
//		
		
		//TODO
		//data is not equal. why? because we didn't load it right? that's ok, right?
		//why is title loaded?
//		assertEquals(savedSee.getFrom().getTitle(),stringTheory.getTitle());		
//		assertEquals(savedSee.getTo().getTitle(),feinman.getTitle());		
//		assertEquals(savedSee.getFrom().getId(),stringTheory.getId());		
//		assertEquals(savedSee.getTo().getId(),feinman.getId());
//		
		
		// Associations Contract will update our associations set in the DAO/Service layer.
		// Don't need to take care of that here.  
		//
		//oy, what are we going to do with the cache? I guess just null out cache 
		//entries for see alsos on their way through to be saved. 
		//bunch of error prone stuff going on with these multi-directional associations
		//that need to be updated on both sides, eh?
		//
		
		
		System.out.println("get id "+stringTheory.getId());
		
		
		Topic savedStringT = topicDAO.getForID(u, stringTheory.getId());
				
		assertNotNull(savedStringT);
		
		
		
		Topic savedFN = topicDAO.getForID(u, feinman.getId());
		assertNotNull(savedFN);
		
		
		//
		//check that the associations were created.
		//
		assertEquals(1, savedStringT.getAssociations().size());
		assertEquals(1, savedFN.getAssociations().size());
		
		
	}
	

	public void testGetTimeline(){
		
		MetaDate md = new MetaDate();
		md.setId(9);
		
		Tag tag = new Tag();
		tag.setId(3);
		
		List<TimeLineObj> list = topicDAO.getTimeline(u);
				
		for (TimeLineObj timeLine : list) {
			System.out.println("timelineObj "+timeLine);
		}
		
		System.out.println("list "+list.size());
	}
	
	
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

}
