package com.aavu.server.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.test.AssertThrows;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.mapper.MindTreeElement;
import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.service.gwt.Converter;
import com.aavu.server.service.gwt.NewConverter;
import com.aavu.server.web.domain.UserPageBean;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.impl.ServerSerializableTypeOracle;
import com.google.gwt.user.server.rpc.impl.ServerSerializableTypeOracleImpl;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamReader;
import com.google.gwt.user.server.rpc.impl.ServerSerializationStreamWriter;

public class TopicDAOHibernateImplTest extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImplTest.class);

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

	private TopicDAO topicDAO;
	
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



	public void testSave() throws HippoBusinessException {

		
//		topicDAO.getForID(u, 0);
		
		
		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		//explicitly not setting user
		Tag tag = new Tag();		
		tag.setName(D);

		tag = (Tag) topicDAO.save(tag);

		t.tagTopic(tag);

		System.out.println("before: "+t.getId());

		System.out.println("TYPES_size "+t.getTypesAsTopics().size());
		
		topicDAO.save(t);
				
		System.out.println("TYPES_size "+t.getTypesAsTopics().size());
		
		
		System.out.println("TOPIC: "+((TopicTypeConnector)t.getTypes().iterator().next()).getTopic().getId());
		System.out.println("TYPE: "+((TopicTypeConnector)t.getTypes().iterator().next()).getType().getId());

		System.out.println("after: "+t.getId());

		List<TopicIdentifier> savedL = topicDAO.getAllTopicIdentifiers(u);

		//tag does not show up bc user not set
		assertEquals(1, savedL.size());		

		TopicIdentifier saved = savedL.get(0);

		Topic savedTopic = topicDAO.getForID(u, saved.getTopicID());

		assertEquals(C, savedTopic.getTitle());
		assertEquals(B, savedTopic.getLatestEntry().getData());
		assertEquals(u, savedTopic.getUser());

		assertEquals(1, savedTopic.getTypesAsTopics().size());

		Topic savedTag = (Topic) savedTopic.getTypesAsTopics().iterator().next();

		assertEquals(D,	savedTag.getTitle());

		//Tag's user will still not be initialized, this happens in service
		assertEquals(null, savedTag.getUser());


	}

	public void testSaveComplexMetas() throws HippoBusinessException {

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

		topicDAO.save(book);

		Topic tomClancy = new Topic();
		tomClancy.setTitle(E);
		topicDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: "+patriotGames.getId());

		topicDAO.save(patriotGames);

		System.out.println("after: "+patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<TopicIdentifier> savedL = topicDAO.getAllTopicIdentifiers(u);

		//NOTE: getAllTopics doesn't take a User right now. That functionality was only used here.
		//List<Topic> allTopics = topicDAO.getAllTopics();				
		//Book, Author, PatGames, PatGames->Author, Book->Author
		//assertEquals(5,allTopics.size());		
		//assertEquals(5, allTopics.size());

		assertEquals(3, savedL.size());


		assertNotSame(0,patriotGames.getId());		
		Topic savedPatriotGames = topicDAO.getForID(u, patriotGames.getId());
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
			Topic top = topicDAO.getForID(u, saved.getTopicID());

			for (Iterator iter = top.getMetaValuesFor(author).iterator(); iter.hasNext();) {
				Topic element = (Topic) iter.next();
				System.out.println("elem "+element.getClass()+" "+element);
			}

			System.out.println(top.toPrettyString());
		}
		//t.setTags(tags);


	}

	public void testGetTopicsWithTag() throws HippoBusinessException{


		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Topic t2 = new Topic();
		t2.getLatestEntry().setData(C);
		t2.setTitle(B);
		t2.setUser(u);

		Tag tag = new Tag();
		tag.setName("testtagAAA");					

		topicDAO.save(tag);

		t.addType(tag);		

		System.out.println("before: "+t.getId());

		topicDAO.save(t);
		topicDAO.save(t2);

		System.out.println("after: "+t.getId());

		System.out.println("getting w/ id "+tag.getId());
		List<TopicTypeConnector> savedL = topicDAO.getTopicIdsWithTag(tag.getId(),u);

		Date lastUp = null;
		for (TopicTypeConnector connector : savedL) {
			Date last =  connector.getTopic().getLastUpdated();
			System.out.println("last "+last);
			if(lastUp != null){
				assertTrue(lastUp.after(last));				
			}
			lastUp = last;
			
		}
		
		System.out.println(savedL.get(0));
		System.out.println("b: " +t.toPrettyString());

		System.out.println(((TopicTypeConnector)savedL.get(0)));


		assertEquals(1, savedL.size());

		TopicTypeConnector b = savedL.get(0);

		assertEquals(b.getTopic().getId(),t.getId());
		assertEquals(b.getTopic().getTitle(), t.getTitle());

		System.out.println("A");
	}

	/**
	 * test that they're returned in lastUpdated order
	 * 
	 * @throws HippoBusinessException
	 * @throws InterruptedException 
	 */
	public void testGetTopicsWithTag2() throws HippoBusinessException, InterruptedException{

		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Topic t2 = new Topic();
		t2.getLatestEntry().setData(C);
		t2.setTitle(B);
		t2.setUser(u);

		Tag tag = new Tag();
		tag.setName("testtagAAA");					

		topicDAO.save(tag);

		t.addType(tag);		
		t.setLastUpdated(new Date());
		topicDAO.save(t);
		t2 = topicDAO.save(t2);
		
		System.out.println("before: "+t.getId());

		//ensure that they get different times
		Thread.sleep(2000);
		
		t2.addType(tag);
		t2.setLastUpdated(new Date());
		t2 = topicDAO.save(t2);
		
		System.out.println("after: "+t.getId());

		System.out.println("getting w/ id "+tag.getId());
		List<TopicTypeConnector> savedL = topicDAO.getTopicIdsWithTag(tag.getId(),u);


		assertEquals(2, savedL.size());
		
		Date lastUp = null;
		for (TopicTypeConnector connector : savedL) {
			Date last =  connector.getTopic().getLastUpdated();
			System.out.println("last "+last);
			if(lastUp != null){		
				assertTrue(lastUp.after(last));				
			}
			lastUp = last;
			
		}
		



	}
	public void testGetAllTopicIdentifiers() throws HippoBusinessException{	

		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		topicDAO.save(t);

		List<TopicIdentifier> list = topicDAO.getAllTopicIdentifiers(u);

		assertEquals(1,list.size());

		for(TopicIdentifier tident : list){
			assertEquals(tident.getTopicTitle(),C);
		}

		Topic t2 = new Topic(u,D);
		t2.addSeeAlso(t.getIdentifier());
		topicDAO.save(t2);


		list = topicDAO.getAllTopicIdentifiers(u);

		for(TopicIdentifier tident : list){
			System.out.println("tident "+tident);
		}
		//not 3, even though there's and association
		assertEquals(2,list.size());


		assertEquals(list.get(0).getTopicTitle(),D);
		assertEquals(list.get(1).getTopicTitle(),C);


	}

	public void testSaveSeeAlsos() throws HippoBusinessException{

		Topic author = new Topic(u,B);


		author = topicDAO.save(author);

		Topic patGames = new Topic(u,C);		

		patGames = topicDAO.save(patGames);

		author.addSeeAlso(patGames.getIdentifier());

		System.out.println("_________________");

		System.out.println(author.toPrettyString());

		author = topicDAO.save(author);

		System.out.println("++++++++++++++++++");

		Topic savedST = author.getSeeAlsoAssociation();

		Association savedSee = (Association) savedST;

		System.out.println(author.toPrettyString());
		System.out.println("----------");
		System.out.println("member size "+savedSee.getMembers().size());
		Topic member = (Topic) savedSee.getMembers().iterator().next();
		System.out.println("member: "+member.toPrettyString());
		System.out.println("comp "+member.compare(patGames));

		assertTrue(savedSee.getMembers().contains(patGames));

		//
		//oy, what are we going to do with the cache? I guess just null out cache 
		//entries for see alsos on their way through to be saved. 
		//bunch of error prone stuff going on with these multi-directional associations
		//that need to be updated on both sides, eh?
		//		
		System.out.println("get id "+author.getId());

		Topic savedStringT = topicDAO.getForID(u, author.getId());

		assertNotNull(savedStringT);



		Topic savedFN = topicDAO.getForID(u, patGames.getId());
		assertNotNull(savedFN);


		//
		//check that the associations were created.
		//
		assertEquals(1, savedStringT.getAssociations().size());
		assertEquals(0, savedFN.getAssociations().size());

		System.out.println("get links to "+savedFN.toPrettyString());

		List<TopicIdentifier> linksTo = topicDAO.getLinksTo(savedFN, u);

		assertNotNull(linksTo);
		assertEquals(1, linksTo.size());

		TopicIdentifier linkTo = linksTo.iterator().next();
		assertEquals(B, linkTo.getTopicTitle());



		//
		//now add a see also and save
		//
		Topic bullcrap = new Topic(u,D);		
		bullcrap = topicDAO.save(bullcrap);		
		author.addSeeAlso(bullcrap.getIdentifier());		
		topicDAO.save(author);

//		topicDAO.evict(bullcrap);
//		topicDAO.evict(author);
//		topicDAO.evict(patGames);

		savedStringT = topicDAO.getForID(u, author.getId());		
		assertNotNull(savedStringT);

		savedFN = topicDAO.getForID(u, patGames.getId());
		assertNotNull(savedFN);

		Topic savedBull = topicDAO.getForID(u, bullcrap.getId());
		assertNotNull(savedBull); 

		//
		//check that the associations were created. There should only be 
		//1 see also for savedStringT, but it shoudl have 2 members.
		//
		assertEquals(1, savedStringT.getAssociations().size());
		assertEquals(0, savedFN.getAssociations().size());
		assertEquals(0, savedBull.getAssociations().size());

		Association secondSeeAlsoSave = savedStringT.getSeeAlsoAssociation();
		assertEquals(2, secondSeeAlsoSave.getMembers().size());

		//
		//now a link to Bull
		//
		List<TopicIdentifier> toBull = topicDAO.getLinksTo(savedBull, u);
		assertEquals(1, toBull.size());

		linkTo = toBull.iterator().next();
		assertEquals(B, linkTo.getTopicTitle());

		//
		//still linked to Feinman
		//
		List<TopicIdentifier> toFein = topicDAO.getLinksTo(savedFN, u);
		assertEquals(1, toFein.size());

		linkTo = toFein.iterator().next();
		assertEquals(B, linkTo.getTopicTitle());



	}

	/**
	 * Main test here is to make sure that the Singleton creating code for SeeAlsos
	 * works. This code is in TopicDaoHibernateImpl.save()   It allows us to just say
	 * "new MetaSeeAlso()" but not end up with multiple DB instances of this object
	 * that should really be a singleton of some sort. An alternative solution would 
	 * be some kind of "getAllSingletonTopics()" service that can be called on GWT 
	 * startup. It's basically a bootstrapping problem. We'll run into this again 
	 * when we want to creat a global 'Book', 'Movie', or 'Author'... those might
	 * be different though because they're already being loaded remotely. SeeAlsos 
	 * code need to do this itself.   
	 * Another alternative could be a topicService.addSeeAlso() call, but this starts 
	 * really taking things out of the Domain. Good and bad aspects to that I suppose.
	 * 
	 * Only functions properly with clean DB bc it uses getAllTopics to sweep for accidental
	 * null user topics.
	 * @throws HippoBusinessException 
	 */
	public void testToMakeSureWeDontCreateTooManyObjects() throws HippoBusinessException{

		//topicDAO.deleteAllTables();

		Topic patriotGames = new Topic(u,C);
		patriotGames.getLatestEntry().setData(B);

		Tag book = new Tag(u,D);

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);

		book.addMeta(author);

		topicDAO.save(book);

		Topic tomClancy = new Topic(u,E);
		topicDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: "+patriotGames.getId());

		topicDAO.save(patriotGames);

		Topic savedPat = topicDAO.getForName(u, C);
		assertEquals(1, savedPat.getAssociations().size());
		assertNotNull(savedPat.getMetaValuesFor(author));
		Topic savedClancy = (Topic) savedPat.getSingleMetaValueFor(author);

		//System.out.println(savedPat.toPrettyString());		
		//System.out.println(savedClancy.toPrettyString());

		List<Topic> allTopics = topicDAO.getAllTopics();		
		//should be six. PatGames, Book, Author, Book->Author, TomClancy, PatGames->TomClancy 
		assertEquals(6,allTopics.size());

		Topic recommender = new Topic(u,F);
		recommender = topicDAO.save(recommender);

		savedPat.addSeeAlso(recommender.getIdentifier());		
		Topic savedPat2 = topicDAO.save(savedPat);

		allTopics = topicDAO.getAllTopics();
		for (Topic topic : allTopics) {
			System.out.println("topic "+topic+" "+topic.getId()+" "+topic.getClass());
		}
		//should be nine. 6 from before plus:  Recommender, Patriot->Recommender, and SeeAlso, 
		assertEquals(9,allTopics.size());


		Topic anotherBook = new Topic(u,G);
		topicDAO.save(anotherBook);

		savedPat2.addSeeAlso(anotherBook.getIdentifier());

		Topic savedPat3 = topicDAO.save(savedPat2);

		assertEquals(2,savedPat3.getSeeAlsoAssociation().getMembers().size());

		allTopics = topicDAO.getAllTopics();
		for (Topic topic : allTopics) {
			System.out.println("topic "+topic+" "+topic.getId()+" "+topic.getClass()+" user "+topic.getUser());
		}
		//should be just 10. 9 from before plus: Another 
		assertEquals(10,allTopics.size());


		Topic anotherRecommender = new Topic(u,H);
		topicDAO.save(anotherRecommender);

		anotherRecommender.addSeeAlso(savedPat3.getIdentifier());
		topicDAO.save(anotherRecommender);


		allTopics = topicDAO.getAllTopics();
		for (Topic topic : allTopics) {
			System.out.println("topic "+topic+" "+topic.getId()+" "+topic.getClass()+" user "+topic.getUser());
		}
		//should be just 12. 10 from before plus: AnotherRecommender & AnotherRecomender's Association 
		assertEquals(12,allTopics.size());
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
	
	public void testSubjectSave() throws HippoBusinessException {
		Topic t = new Topic(u,B);

		Subject b_Subj = new AmazonBook();
		b_Subj.setForeignID(D);
		b_Subj.setName(E);

		t.setSubject(b_Subj);

		topicDAO.save(t);

		Topic savedT = topicDAO.getForName(u, B);

		assertEquals(D,savedT.getSubject().getForeignID());


		//
		//Test that a second saved topic with the same subject
		//get's the same subject object
		//
		Subject c_Subj = new AmazonBook();
		c_Subj.setForeignID(D);
		c_Subj.setName(E);

		assertEquals(0, c_Subj.getId());

		//hmm. interesting. I guess that's right, although maybe they should be .eq logically. dunno.
		assertNotSame(b_Subj, c_Subj);

		Topic t2 = new Topic(u,C);
		t2.setSubject(c_Subj);		
		topicDAO.save(t2);

		Topic s1 = topicDAO.getForName(u, B);
		Topic s2 = topicDAO.getForName(u, C);

		Subject ss1 = s1.getSubject();
		Subject ss2 = s2.getSubject();

		assertTrue(ss1.equals(ss2));
		assertEquals(ss1.getId(), ss2.getId());
		assertEquals(ss1.getForeignID(), ss2.getForeignID());
		assertEquals(ss1,ss2);




	}
	/**
	 * This test starts working when we add Occurences cascade="save-update"
	 * 
	 * @throws HippoBusinessException
	 */
	public void testSubjectSaveTransientProblem() throws HippoBusinessException {
		Topic t = new Topic(u,B);

		topicDAO.save(t);

		Topic savedT = topicDAO.getForName(u, B);

		Subject b_Subj = new AmazonBook();
		b_Subj.setForeignID(D);
		b_Subj.setName(E);

		t.setSubject(b_Subj);

		topicDAO.save(t);

		Topic savedTAgain = topicDAO.getForName(u, B);

		assertEquals(D,savedTAgain.getSubject().getForeignID());

	}

	public void testSaveLink() throws HippoBusinessException {
		WebLink link = 	new WebLink(u,B,C,D);

		link = (WebLink) topicDAO.save(link);

		String string = "";


		log.debug("str: "+string);			
		if(string.equals("")){				
			string = C;
			log.debug("blank tags, setting topic to; "+string);
		}
		Topic t = topicDAO.getForName(u, string);			

		if(null == t){
			log.debug("was null, creating as Tag ");
			t = new Tag();
			t.setTitle(string);				
			t.setUser(u);							
		}			

		assertEquals(0, t.getOccurences().size());

		t.getOccurences().add(link);
		assertEquals(1, t.getOccurences().size());

		System.out.println("-----t-----"+t.toPrettyString());
		Topic st = topicDAO.save(t);
		System.out.println("-----st-----"+st.toPrettyString());

		assertEquals(1, st.getOccurences().size());

	}

	/**
	 * Should let us link occurrence the topic that uses it..
	 * 
	 * A bit confused.. shoudl this really be a one-way many-to-many like
	 * it is now? Can an occurrence exist for many topics? Ahh. crap, yes it can.
	 * Hmm. 
	 * 
	 * 
	 * @throws HippoBusinessException
	 */
	public void testGetTopicForOccurrence() throws HippoBusinessException{
		WebLink link = 	new WebLink(u,B,C,D);
		link = (WebLink) topicDAO.save(link);
		String string = "";
		log.debug("str: "+string);			
		if(string.equals("")){				
			string = C;
			log.debug("blank tags, setting topic to; "+string);
		}
		Topic t = topicDAO.getForName(u, string);			
		if(null == t){
			log.debug("was null, creating as Tag ");
			t = new Tag();
			t.setTitle(string);				
			t.setUser(u);							
		}			
		assertEquals(0, t.getOccurences().size());
		t.getOccurences().add(link);
		assertEquals(1, t.getOccurences().size());
		Topic st = topicDAO.save(t);

		System.out.println("ling "+link.getId());
		List<TopicIdentifier> ident = topicDAO.getTopicForOccurrence(link.getId());
		assertEquals(t.getId(),ident.get(0).getTopicID());
		
	}


	public void testGetTree() throws HippoBusinessException{
		
		Topic t = new Topic(u,B);
		t = topicDAO.save(t);
		
		MindTreeOcc occ = new MindTreeOcc(t);		
		MindTree tree = occ.getMindTree();
		tree.getRightSide().add(new MindTreeElement("Foo",null,0,3));
		tree.getRightSide().add(new MindTreeElement("Foo2",null,1,2));
		tree.getLeftSide().add(new MindTreeElement("FooL",null,0,1));
		
		
		//save tree explicitly
		topicDAO.save(tree);
		
//		//save occ explicitly?
//		topicDAO.save(occ);		
		
		t.getOccurences().add(occ);
		
		Topic saved = topicDAO.save(t);
		
		assertEquals(1, saved.getOccurences().size());
		
		MindTreeOcc socc = (MindTreeOcc) saved.getOccurences().iterator().next();
		
		MindTree savedTree = topicDAO.getTree(socc);
				
		assertNotNull(savedTree);
		
		assertEquals(2, savedTree.getRightSide().size());
		assertEquals(1, savedTree.getLeftSide().size());
	}


	public void testDelete() throws HippoBusinessException{
		Topic patriotGames = new Topic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Tag book = new Tag(u,D);		

		topicDAO.save(book);


		patriotGames.tagTopic(book);

		System.out.println("before: "+patriotGames.getId());

		topicDAO.save(patriotGames);

		System.out.println("after: "+patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<TopicIdentifier> savedL = topicDAO.getAllTopicIdentifiers(u);

		assertEquals(2, savedL.size());
		
		
		book = (Tag) topicDAO.getForName(u, D);
		
		System.out.println("patgames "+patriotGames.toPrettyString());
		System.out.println("book "+book.toPrettyString());
		
		//assertEquals(1,book.getInstances().size());
		
		topicDAO.delete(patriotGames);
		
		savedL = topicDAO.getAllTopicIdentifiers(u);
		assertEquals(1, savedL.size());
		
		book = (Tag) topicDAO.getForName(u, D);
		//assertEquals(0,book.getInstances().size());
		
		
	}
	public void testDeleteAdvanced() throws HippoBusinessException{
		Topic patriotGames = new Topic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Tag book = new Tag(u,D);		

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);
		book.addMeta(author);

		topicDAO.save(book);

		Topic tomClancy = new Topic();
		tomClancy.setTitle(E);
		topicDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: "+patriotGames.getId());

				
		topicDAO.save(patriotGames);

		Entry lastEntry = patriotGames.getLatestEntry();
		
		System.out.println("after: "+patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<TopicIdentifier> savedL = topicDAO.getAllTopicIdentifiers(u);

		assertEquals(3, savedL.size());
		
		
		book = (Tag) topicDAO.getForName(u, D);
		//assertEquals(1,book.getInstances().size());
		
		topicDAO.delete(patriotGames);
		
		savedL = topicDAO.getAllTopicIdentifiers(u);
		assertEquals(2, savedL.size());
		
		book = (Tag) topicDAO.getForName(u, D);
		//assertEquals(0,book.getInstances().size());
		
		//TODO assert that lastEntry has been deleted
	}
	public void testDeleteAdvanced2() throws HippoBusinessException{
		Topic patriotGames = new Topic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Topic forWhomTheBellTolls = new Topic();
		forWhomTheBellTolls.getLatestEntry().setData(B);
		forWhomTheBellTolls.setTitle(F);
		forWhomTheBellTolls.setUser(u);
		
		Tag book = new Tag(u,D);		

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);
		book.addMeta(author);

		topicDAO.save(book);

		Topic tomClancy = new Topic();
		tomClancy.setTitle(E);
		topicDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: "+patriotGames.getId());
				
		topicDAO.save(patriotGames);

		
		forWhomTheBellTolls.tagTopic(book);
		topicDAO.save(forWhomTheBellTolls);
		
		Entry lastEntry = patriotGames.getLatestEntry();
		
		System.out.println("after: "+patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<TopicIdentifier> savedL = topicDAO.getAllTopicIdentifiers(u);

		assertEquals(4, savedL.size());
		
		
		book = (Tag) topicDAO.getForName(u, D);
		//assertEquals(2,book.getInstances().size());
		
		topicDAO.delete(patriotGames);
		
		savedL = topicDAO.getAllTopicIdentifiers(u);
		assertEquals(3, savedL.size());
		
		book = (Tag) topicDAO.getForName(u, D);
		//assertEquals(1,book.getInstances().size());
		
	
		
		
	}
	
	public void testSaveTopicsLocation() throws HippoBusinessException{
		
	
		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Topic t2 = new Topic();
		t2.getLatestEntry().setData(C);
		t2.setTitle(B);
		t2.setUser(u);

		Tag tag = new Tag();
		tag.setName("testtagAAA");					

		tag = (Tag) topicDAO.save(tag);

		t.addType(tag);

		System.out.println("before: "+t.getId());

		topicDAO.save(t);
		topicDAO.save(t2);
		
		System.out.println("after: "+t.getId());

		List<TopicTypeConnector> savedL = topicDAO.getTopicIdsWithTag(tag.getId(),u);

		System.out.println(savedL.get(0));
		System.out.println("b: " +t.toPrettyString());

		System.out.println(((TopicTypeConnector)savedL.get(0)));


		assertEquals(1, savedL.size());

		TopicTypeConnector b = savedL.get(0);

		assertEquals(b.getTopic().getId(),t.getId());
		assertEquals(b.getTopic().getTitle(), t.getTitle());

		System.out.println("A");
		
		
		
		List<TopicTypeConnector> topicsWithTag = topicDAO.getTopicIdsWithTag(tag.getId(), u);
		assertEquals(1, topicsWithTag.size());
		TopicTypeConnector fti = topicsWithTag.get(0);
		assertEquals(-1.0, fti.getLatitude());
		assertEquals(-1.0, fti.getLongitude());
		
		//sysout
		topicDAO.saveTopicsLocation(tag.getId(), t.getId(), .23, .47);
		
		topicsWithTag = topicDAO.getTopicIdsWithTag(tag.getId(), u);
		assertEquals(1, topicsWithTag.size());
		fti = topicsWithTag.get(0);
		assertEquals(.23, fti.getLongitude());
		assertEquals(.47, fti.getLatitude());
		
		
		
		
	}
	
	public void testPopualateUsageStats(){
		
		UserPageBean bean = new UserPageBean();
		topicDAO.populateUsageStats(bean);
		
		System.out.println(bean);
	}
	
	public void testLazyWithSeeAlso(){
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 707);
		
		System.out.println(t.toPrettyString());
		
		assertEquals(1,t.getAssociations().size());
		
		Association seeAlsoP1 = (Association) t.getAssociations().iterator().next();
		assertEquals(1, seeAlsoP1.getMembers().size());
		assertEquals(1, seeAlsoP1.getTypes().size());
				
		Association seeAlsoPRE = t.getSeeAlsoAssociation();
		
		assertEquals(1, seeAlsoPRE.getMembers().size());
		assertEquals(1, seeAlsoPRE.getTypes().size());
		
		System.out.println(t.toPrettyString());
		
		NewConverter.convertInPlace(t);
		assertFalse(Converter.scan(t));
		
		
		assertEquals(1,t.getAssociations().size());
		
		Association seeAlso = t.getSeeAlsoAssociation();
		
		assertEquals(1, seeAlso.getMembers().size());
		assertEquals(1, seeAlso.getTypes().size());
		
		Topic seeAlsoUber = seeAlso.getFirstType();
		
		
	}

	public void testLazyWithSubject(){
				
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 208);
		
		log.debug(t.toPrettyString());
		
		assertEquals(1,t.getAssociations().size());
		
		assertTrue(Converter.scan(t));

		NewConverter.convertInPlace(t);
		
		assertFalse(Converter.scan(t));	

	}
	
	public void testLazyWithMetas(){
		
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 970);
		
		log.debug(t.toPrettyString());
		
		assertEquals(1,t.getAssociations().size());
		
		assertTrue(Converter.scan(t));

		NewConverter.convertInPlace(t);
		
		assertFalse(Converter.scan(t));	

	}
	
	
	public void testSerialization(){
		
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 208);
		
		String str = Converter.serialize(t);
		
		assertTrue(str.contains("CGLIB"));
		assertTrue(str.contains("Persistent"));
		assertTrue(str.contains("java.sql.Timestamp"));
				
		NewConverter.convertInPlace(t);		
		
		str = Converter.serialize(t);
				
		assertFalse(str.contains("CGLIB"));
		
		assertFalse(str.contains("Persistent"));
		
		assertFalse(str.contains("java.sql.Timestamp"));
				
	}
	
	public void testSerializationOfBigComplex(){
		
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 715);
		
		String str = Converter.serialize(t);
		
		assertTrue(str.contains("CGLIB"));
		assertTrue(str.contains("Persistent"));
		assertTrue(str.contains("java.sql.Timestamp"));
				
		NewConverter.convertInPlace(t);		
		
		str = Converter.serialize(t);
				
		assertFalse(str.contains("CGLIB"));
		
		assertFalse(str.contains("Persistent"));
		
		assertFalse(str.contains("java.sql.Timestamp"));
				
	}

	
	
	public void testSerializationWHibernateSupport(){
		
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 208);
		
		String str = Converter.serializeWithHibernateSupport(t);
		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		System.out.println("pass 1");
		
		assertFalse(str.contains("Persistent"));
		
		System.out.println("pass 2");
		
		assertFalse(str.contains("CGLIB"));
		
		System.out.println("pass 3!!!");
						
	}
	
	public void testSerializationOfBigComplexWHibernate(){
		
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 715);
		
		String str = Converter.serializeWithHibernateSupport(t);
				
		assertFalse(str.contains("CGLIB"));
		
		assertFalse(str.contains("Persistent"));
		
		assertFalse(str.contains("java.sql.Timestamp"));
				
	}
	public void testSerializationOfMany(){
		
		User uu = new User();
		uu.setId(1);
		Topic t = topicDAO.getForID(uu, 515);
		
		String str = Converter.serializeWithHibernateSupport(t);
				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		t = topicDAO.getForID(uu, 707);		
		str = Converter.serializeWithHibernateSupport(t);				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		
		t = topicDAO.getForID(uu, 208);		
		str = Converter.serializeWithHibernateSupport(t);				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
		
		t = topicDAO.getForID(uu, 970);		
		str = Converter.serializeWithHibernateSupport(t);				
		assertFalse(str.contains("CGLIB"));		
		assertFalse(str.contains("Persistent"));		
		assertFalse(str.contains("java.sql.Timestamp"));
	}
	
}
