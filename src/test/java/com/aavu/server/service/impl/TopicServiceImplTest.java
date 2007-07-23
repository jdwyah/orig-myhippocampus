package com.aavu.server.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.test.AssertThrows;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaLocation;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.commands.AbstractCommand;
import com.aavu.client.domain.commands.RemoveTagFromTopicCommand;
import com.aavu.client.domain.commands.SaveMetaDateCommand;
import com.aavu.client.domain.commands.SaveMetaLocationCommand;
import com.aavu.client.domain.commands.SaveSeeAlsoCommand;
import com.aavu.client.domain.commands.SaveTagPropertiesCommand;
import com.aavu.client.domain.commands.SaveTagtoTopicCommand;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.HippoException;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestNoTransaction;

public class TopicServiceImplTest extends BaseTestNoTransaction {


	private static final Logger log = Logger.getLogger(TopicServiceImplTest.class);

	// private static final String B = "Ssds45t";
	// private static final String C = "ASR#35rf";
	// private static final String D = "234234123";
	// private static final String E = "^#*(DNS03";

	private static final String B = "Author";
	private static final String C = "PatriotGames";
	private static final String D = "Book";
	private static final String E = "TomClancy";
	private static final String F = "Recommender";
	private static final String G = "AnotherBook";
	private static final String H = "AnotherRecommender";

	private static final String J = "DateRead";
	private static final String K = "LocationMeta";

	private static final int LAT1 = 213123;
	private static final int LONG1 = 12332;
	private static final int LAT2 = 223;
	private static final int LONG2 = 2334;

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
	// @Override
	// protected void onSetUpBeforeTransaction() throws Exception {
	// super.onSetUpBeforeTransaction();
	// setUsername("junit");
	// }
	// @Override
	// protected void onSetUpInTransaction() throws Exception {
	//
	// super.onSetUpInTransaction();
	//
	//		
	// }


	/**
	 * NOTE; same code as DAO test, but with service. Different result, since we set the tag's user
	 * in the service layer.
	 * 
	 * @throws HippoBusinessException
	 */
	public void testSaveTopic() throws HippoBusinessException {
		clean();

		Topic t = new RealTopic();

		// Entry e = new Entry();
		// e.setData(B);
		// t.addOccurence(e);

		t.setTitle(C);
		t.setUser(u);

		Topic tag = new RealTopic();
		tag.setTitle(D);

		topicService.save(tag);

		t.tagTopic(tag);

		System.out.println("before: " + t.getId());

		topicService.save(t);

		System.out.println("after: " + t.getId());

		List<DatedTopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		assertEquals(2, savedL.size());

		TopicIdentifier saved = savedL.get(1);

		Topic savedTopic = topicService.getForID(saved.getTopicID());

		assertEquals(C, savedTopic.getTitle());
		// assertEquals(B, savedTopic.getLatestEntry().getData());
		assertEquals(u, savedTopic.getUser());

		assertEquals(1, savedTopic.getTypesAsTopics().size());

		Topic savedTag = (Topic) savedTopic.getTypesAsTopics().iterator().next();

		assertEquals(D, savedTag.getTitle());

		// Tag's user will not be initialized
		assertEquals(u, savedTag.getUser());


	}

	/**
	 * NOTE; same code as DAO test, but with service. Different result, since we set the tag's user
	 * in the service layer.
	 * 
	 * @throws HippoBusinessException
	 */
	public void testSaveAndCompleteLoad() throws HippoBusinessException {
		clean();

		Topic t = new RealTopic();

		Entry e = new Entry();
		e.setData(B);
		t.addOccurence(e);

		t.setTitle(C);
		t.setUser(u);

		Topic tag = new RealTopic();
		tag.setTitle(D);

		topicService.save(tag);

		t.tagTopic(tag);

		assertEquals(1, t.getOccurences().size());

		System.out.println("before: " + t.getId());

		t = topicService.save(t);

		topicService.save(e);

		System.out.println("after: " + t.getId());

		List<DatedTopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		assertEquals(3, savedL.size());

		Topic savedTopic = topicService.getForID(t.getId());

		assertEquals(C, savedTopic.getTitle());

		assertEquals(1, savedTopic.getOccurences().size());

		for (Iterator iterator = savedTopic.getOccurences().iterator(); iterator.hasNext();) {
			TopicOccurrenceConnector owl = (TopicOccurrenceConnector) iterator.next();
			System.out.println("occ " + owl.getOccurrence().getData() + " "
					+ owl.getOccurrence().getTitle() + " " + owl.getOccurrence().getId());

		}
		TopicOccurrenceConnector owl = (TopicOccurrenceConnector) savedTopic.getOccurences()
				.iterator().next();

		Occurrence occ = (Occurrence) owl.getOccurrence();

		TopicOccurrenceConnector toc = (TopicOccurrenceConnector) occ.getTopics().iterator().next();
		assertEquals(savedTopic, toc.getTopic());

		System.out.println();

		// not working because of CGLIB enhanced, 'instanceof' not working.
		// assertEquals(B, savedTopic.getLatestEntry().getDataWithoutBodyTags());

		assertEquals(u, savedTopic.getUser());

		assertEquals(1, savedTopic.getTypesAsTopics().size());

		Topic savedTag = (Topic) savedTopic.getTypesAsTopics().iterator().next();

		assertEquals(D, savedTag.getTitle());

		// Tag's user will not be initialized
		assertEquals(u, savedTag.getUser());



		Topic links = topicService.getForID(2081);
		assertEquals(3, links.getOccurences().size());

		for (Iterator iterator = links.getOccurenceObjs().iterator(); iterator.hasNext();) {
			Occurrence link = (Occurrence) iterator.next();
			assertEquals(1, link.getTopics().size());
		}

	}

	public void __testSaveComplexMetas() throws HippoBusinessException {

		Topic patriotGames = new RealTopic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Topic book = new RealTopic(u, D);

		MetaText ss = new MetaText();
		MetaTopic author = new MetaTopic();

		author.setTitle(B);
		author.setUser(u);
		book.addTagProperty(author);

		topicService.save(book);

		Topic tomClancy = new RealTopic();
		tomClancy.setTitle(E);
		topicService.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: " + patriotGames.getId());

		topicService.save(patriotGames);

		System.out.println("after: " + patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<DatedTopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		// NOTE: getAllTopics doesn't take a User right now. That functionality was only used here.
		// List<Topic> allTopics = topicDAO.getAllTopics();
		// Book, Author, PatGames, PatGames->Author, Book->Author
		// assertEquals(5,allTopics.size());
		// assertEquals(5, allTopics.size());

		assertEquals(4, savedL.size());


		assertNotSame(0, patriotGames.getId());
		Topic savedPatriotGames = topicService.getForID(patriotGames.getId());
		assertNotNull(savedPatriotGames);

		assertEquals(1, savedPatriotGames.getTypesAsTopics().size());
		Topic savedBookTag = (Topic) savedPatriotGames.getTypesAsTopics().iterator().next();
		assertEquals(D, savedBookTag.getTitle());
		assertEquals(1, savedBookTag.getTagProperties().size());

		assertEquals(1, savedPatriotGames.getMetaValuesFor(author).size());
		assertEquals(0, savedPatriotGames.getTagProperties().size());



		Topic savedTomClancy = (Topic) savedPatriotGames.getSingleMetaValueFor(author);
		assertNotNull(savedTomClancy);


		assertEquals(E, savedTomClancy.getTitle());


		// assertEquals(savedBookTag., false)


		for (TopicIdentifier saved : savedL) {
			// Topic saved = savedL.get(0);

			System.out.println("C");
			Topic top = topicService.getForID(saved.getTopicID());

			for (Iterator iter = top.getMetaValuesFor(author).iterator(); iter.hasNext();) {
				Topic element = (Topic) iter.next();
				System.out.println("elem " + element.getClass() + " " + element);
			}

			System.out.println(top.toPrettyString());
		}
		// t.setTags(tags);


	}

	public void testUpdatingTopic() throws HippoBusinessException {

		clean();
		Topic t = new RealTopic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Topic tag = new RealTopic();
		tag.setTitle(D);

		tag = topicService.save(tag);

		t.tagTopic(tag);

		System.out.println("before: " + t.getId());

		topicService.save(t);

		System.out.println("after: " + t.getId());

		List<DatedTopicIdentifier> savedL = topicService.getAllTopicIdentifiers();

		assertEquals(3, savedL.size());

		TopicIdentifier saved = savedL.get(1);


		Topic savedTopic = topicService.getForID(saved.getTopicID());

		savedTopic.setTitle(E);

		Topic secondSave = topicService.save(savedTopic);

		assertEquals(E, secondSave.getTitle());

	}


	public void testCommands() throws HippoException {

		clean();

		Topic tag = new RealTopic(u, D);
		tag = topicService.save(tag);

		System.out.println("SAVED TAG " + B);


		Topic topic = new RealTopic(u, C);
		topic = (Topic) topicService.save(topic);

		System.out.println("SAVED TOPIC " + C);
		topicService.executeAndSaveCommand(new SaveTagtoTopicCommand(topic, tag));

		Topic topicS = topicService.getForID(topic.getId());
		assertEquals(1, topicS.getTypes().size());
		Topic tagRef = (Topic) topicS.getTypesAsTopics().iterator().next();
		assertEquals(tag.getId(), tagRef.getId());


		Topic t3 = new RealTopic(u, G);
		t3 = (Topic) topicService.save(t3);
		topicService.executeAndSaveCommand(new SaveSeeAlsoCommand(topic, t3));

		Topic t3s = topicService.getForID(t3.getId());
		assertEquals(0, t3s.getAssociations().size());

		topicS = topicService.getForID(topic.getId());
		assertEquals(1, topicS.getAssociations().size());

		Association a = (Association) topicS.getAssociations().iterator().next();
		assertEquals(1, a.getMembers().size());

		Topic s = (Topic) a.getMembers().iterator().next();
		assertEquals(t3.getId(), s.getId());


	}

	public void testLocationCommand() throws HippoException {

		clean();

		Topic tag = new RealTopic(u, B);
		tag = topicService.save(tag);

		System.out.println("SAVED TAG " + B);


		Topic topic = new RealTopic(u, C);
		topic = (Topic) topicService.save(topic);

		MetaLocation metaL = new MetaLocation();
		metaL.setTitle(K);
		metaL = (MetaLocation) topicService.save(metaL);

		MetaLocation savedMeta1 = (MetaLocation) topicService.getForID(metaL.getId());
		assertNotSame(0, savedMeta1.getId());
		System.out.println("saved meta1 " + savedMeta1);
		assertTrue(savedMeta1 instanceof MetaLocation);


		HippoLocation loc1 = new HippoLocation();
		loc1.setLatitude(LAT1);
		loc1.setLongitude(LONG1);

		Set locs = new HashSet();
		locs.add(loc1);

		AbstractCommand comm = new SaveMetaLocationCommand(topic, savedMeta1, locs);


		topicService.executeAndSaveCommand(comm);

		System.out.println("FINISHED SAVE");

		Topic topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());

		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());
		Meta savedMeta = (Meta) topicS.getMetas().iterator().next();
		assertNotNull(savedMeta);
		assertEquals(K, savedMeta.getTitle());

		HippoLocation savedLoc = (HippoLocation) topicS.getSingleMetaValueFor(savedMeta);
		assertNotNull(savedLoc);

		assertEquals(LAT1, savedLoc.getLatitude());
		assertEquals(LONG1, savedLoc.getLongitude());


	}

	public void testMultipleLocationCommand() throws HippoException {

		clean();

		Topic tag = new RealTopic(u, B);
		tag = topicService.save(tag);

		System.out.println("SAVED TAG " + B);


		Topic topic = new RealTopic(u, C);
		topic = (Topic) topicService.save(topic);

		MetaLocation metaL = new MetaLocation();
		metaL.setTitle(K);
		metaL = (MetaLocation) topicService.save(metaL);

		MetaLocation savedMeta1 = (MetaLocation) topicService.getForID(metaL.getId());
		assertNotSame(0, savedMeta1.getId());
		System.out.println("saved meta1 " + savedMeta1);
		assertTrue(savedMeta1 instanceof MetaLocation);


		HippoLocation loc1 = new HippoLocation();
		loc1.setLatitude(LAT1);
		loc1.setLongitude(LONG1);
		loc1.setTitle(E);

		HippoLocation loc2 = new HippoLocation();
		loc2.setLatitude(LAT2);
		loc2.setLongitude(LONG2);
		loc2.setTitle(D);

		Set locs = new HashSet();
		locs.add(loc1);
		locs.add(loc2);

		AbstractCommand comm = new SaveMetaLocationCommand(topic, savedMeta1, locs);


		topicService.executeAndSaveCommand(comm);

		System.out.println("FINISHED SAVE");

		Topic topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());

		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());
		Meta savedMeta = (Meta) topicS.getMetas().iterator().next();
		assertNotNull(savedMeta);
		assertEquals(K, savedMeta.getTitle());


		Set members = topicS.getMetaValuesFor(savedMeta);
		assertEquals(2, members.size());

		for (Iterator iter = members.iterator(); iter.hasNext();) {
			HippoLocation l = (HippoLocation) iter.next();
			if (l.getTitle().equals(D)) {
				assertEquals(LAT2, l.getLatitude());
				assertEquals(LONG2, l.getLongitude());
			} else if (l.getTitle().equals(E)) {
				assertEquals(LAT1, l.getLatitude());
				assertEquals(LONG1, l.getLongitude());
			} else {
				fail("Wrong Title " + l.getTitle());
			}
		}

	}


	public void testMultipleLocationsInMultiplePassesCommand() throws HippoException {

		clean();

		Topic topic = new RealTopic(u, C);
		topic = (Topic) topicService.save(topic);

		MetaLocation metaL = new MetaLocation();
		metaL.setTitle(K);
		metaL = (MetaLocation) topicService.save(metaL);

		MetaLocation savedMeta1 = (MetaLocation) topicService.getForID(metaL.getId());
		assertNotSame(0, savedMeta1.getId());
		System.out.println("saved meta1 " + savedMeta1);
		assertTrue(savedMeta1 instanceof MetaLocation);


		HippoLocation loc1 = new HippoLocation();
		loc1.setLatitude(LAT1);
		loc1.setLongitude(LONG1);
		loc1.setTitle(E);

		HippoLocation loc2 = new HippoLocation();
		loc2.setLatitude(LAT2);
		loc2.setLongitude(LONG2);
		loc2.setTitle(D);

		Set locs = new HashSet();
		locs.add(loc1);

		AbstractCommand comm = new SaveMetaLocationCommand(topic, savedMeta1, locs);


		topicService.executeAndSaveCommand(comm);

		System.out.println("FINISHED SAVE 1");

		Topic topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());

		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());
		Meta savedMeta = (Meta) topicS.getMetas().iterator().next();
		assertNotNull(savedMeta);
		assertEquals(K, savedMeta.getTitle());


		Set members = topicS.getMetaValuesFor(savedMeta);
		assertEquals(1, members.size());

		HippoLocation s1 = (HippoLocation) topicS.getSingleMetaValueFor(savedMeta);
		assertEquals(E, s1.getTitle());
		assertEquals(LAT1, s1.getLatitude());
		assertEquals(LONG1, s1.getLongitude());

		List<DatedTopicIdentifier> idents = topicService.getAllTopicIdentifiers(true);
		assertEquals(4, idents.size());



		//
		// Add the second Locations and verify that we now have 2
		//
		locs.add(loc2);
		AbstractCommand comm2 = new SaveMetaLocationCommand(topic, savedMeta1, locs);
		topicService.executeAndSaveCommand(comm2);

		topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());
		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());
		savedMeta = (Meta) topicS.getMetas().iterator().next();
		assertNotNull(savedMeta);
		assertEquals(K, savedMeta.getTitle());
		members = topicS.getMetaValuesFor(savedMeta);
		assertEquals(2, members.size());

		for (Iterator iter = members.iterator(); iter.hasNext();) {
			HippoLocation l = (HippoLocation) iter.next();
			if (l.getTitle().equals(D)) {
				assertEquals(LAT2, l.getLatitude());
				assertEquals(LONG2, l.getLongitude());
			} else if (l.getTitle().equals(E)) {
				assertEquals(LAT1, l.getLatitude());
				assertEquals(LONG1, l.getLongitude());
			} else {
				fail("Wrong Title " + l.getTitle());
			}
		}
		idents = topicService.getAllTopicIdentifiers(true);
		assertEquals(5, idents.size());

		//
		// Now do a command with only loc2.
		// assert that we've gone down to just 1 meta-value.
		//
		Set locs3 = new HashSet();
		locs3.add(loc2);
		AbstractCommand comm3 = new SaveMetaLocationCommand(topic, savedMeta1, locs3);
		topicService.executeAndSaveCommand(comm3);

		topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());
		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());
		savedMeta = (Meta) topicS.getMetas().iterator().next();
		assertNotNull(savedMeta);
		assertEquals(K, savedMeta.getTitle());
		members = topicS.getMetaValuesFor(savedMeta);
		assertEquals(1, members.size());


		HippoLocation s3 = (HippoLocation) topicS.getSingleMetaValueFor(savedMeta);
		assertEquals(D, s3.getTitle());
		assertEquals(LAT2, s3.getLatitude());
		assertEquals(LONG2, s3.getLongitude());



		//
		// Test that there was no DB leakage
		//
		idents = topicService.getAllTopicIdentifiers(true);
		assertEquals(4, idents.size());

	}



	public void testMetaDateCommand() throws HippoException {

		clean();



		Topic topic = new RealTopic(u, C);
		topic = (Topic) topicService.save(topic);

		MetaDate metaL = new MetaDate();
		metaL.setTitle(J);
		metaL = (MetaDate) topicService.save(metaL);

		Topic savedMeta1 = topicService.getForID(metaL.getId());
		assertNotSame(0, savedMeta1.getId());
		System.out.println("||| metaL " + metaL + " " + metaL.getClass());
		System.out.println("||| savedMeta1 " + savedMeta1 + " " + savedMeta1.getClass());
		assertTrue(savedMeta1 instanceof MetaDate);


		HippoDate date = new HippoDate();
		date.setStartDate(new Date());


		AbstractCommand comm = new SaveMetaDateCommand(topic, savedMeta1, date);


		topicService.executeAndSaveCommand(comm);

		System.out.println("FINISHED SAVE");


		Topic topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());

		System.out.println(topicS.toPrettyString());


		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());
		Meta savedMeta = (Meta) topicS.getMetas().iterator().next();
		assertNotNull(savedMeta);


		assertEquals(J, savedMeta.getTitle());

		HippoDate savedLoc = (HippoDate) topicS.getSingleMetaValueFor(savedMeta);
		assertNotNull(savedLoc);
		assertEquals(date.getTitle(), savedLoc.getTitle());


	}


	public void testConnectionsInMultiplePassesCommand() throws HippoException {

		clean();

		Topic topic = new RealTopic(u, C);
		topic = (Topic) topicService.save(topic);


		Topic topic2 = new RealTopic(u, D);
		topic2 = (Topic) topicService.save(topic2);

		Topic topic3 = new RealTopic(u, E);
		topic3 = (Topic) topicService.save(topic3);


		AbstractCommand comm = new SaveSeeAlsoCommand(topic, topic2);

		topicService.executeAndSaveCommand(comm);

		log.debug("FINISHED SAVE 1");

		Topic topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());
		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());

		Association see = topicS.getSeeAlsoAssociation();
		assertNotNull(see);
		assertNotNull(see.getMembers());
		assertEquals(1, see.getMembers().size());
		assertEquals(topic2, see.getMembers().iterator().next());


		comm = new SaveSeeAlsoCommand(topic, topic3);
		topicService.executeAndSaveCommand(comm);

		log.debug("FINISHED SAVE 2");


		topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());
		assertEquals(1, topicS.getAssociations().size());
		assertEquals(1, topicS.getMetas().size());

		see = topicS.getSeeAlsoAssociation();
		assertNotNull(see);
		assertNotNull(see.getMembers());
		assertEquals(2, see.getMembers().size());

		for (Iterator iter = see.getMembers().iterator(); iter.hasNext();) {
			Topic element = (Topic) iter.next();
			assertTrue(element.equals(topic2) || element.equals(topic3));
		}

	}


	/**
	 * Test duplicate entry and "" title checks.
	 * 
	 * @throws HippoBusinessException
	 */
	public void testSaveChecks() throws HippoBusinessException {
		clean();

		final Topic t = new RealTopic(u, "");

		new AssertThrows(HippoBusinessException.class) {
			public void test() throws HippoBusinessException {
				topicService.save(t);
			}
		}.runTest();

		final Topic t2 = new RealTopic(u, C);
		final Topic t3 = new RealTopic(u, C);
		topicService.save(t2);

		new AssertThrows(HippoBusinessException.class) {
			public void test() throws HippoBusinessException {
				topicService.save(t3);
			}
		}.runTest();


		//
		// Dates are a different case. They should be able to have the same title
		//
		final HippoDate d2 = new HippoDate();
		d2.setUser(u);
		d2.setStartDate(new Date());
		final HippoDate d3 = new HippoDate();
		d3.setUser(u);
		d3.setStartDate(new Date());

		assertEquals(d2.getTitle(), d3.getTitle());

		topicService.save(d2);
		topicService.save(d3);


	}


	public void testTagPropertyCommand() throws HippoException {

		clean();

		Topic tag = new RealTopic(u, C);
		tag = topicService.save(tag);


		MetaDate metaDate = new MetaDate();
		metaDate.setTitle(J);
		metaDate = (MetaDate) topicService.save(metaDate);

		MetaDate savedMeta1 = (MetaDate) topicService.getForID(metaDate.getId());
		assertNotSame(0, savedMeta1.getId());
		System.out.println("||| metaL " + metaDate + " " + metaDate.getClass());
		System.out.println("||| savedMeta1 " + savedMeta1 + " " + savedMeta1.getClass());
		assertTrue(savedMeta1 instanceof MetaDate);



		Meta[] metas = new Meta[] { savedMeta1 };

		AbstractCommand comm = new SaveTagPropertiesCommand(tag, metas);

		topicService.executeAndSaveCommand(comm);

		System.out.println("FINISHED SAVE");


		Topic topicS = topicService.getForID(tag.getId());
		assertEquals(0, topicS.getTypes().size());

		System.out.println(topicS.toPrettyString());
		assertEquals(1, topicS.getTagProperties().size());


		//
		// add a second meta
		//
		MetaDate metaDate2 = new MetaDate();
		metaDate2.setTitle(D);
		metaDate2 = (MetaDate) topicService.save(metaDate2);
		MetaDate savedMeta2 = (MetaDate) topicService.getForID(metaDate2.getId());
		assertNotSame(0, savedMeta2.getId());
		assertTrue(savedMeta2 instanceof MetaDate);

		metas = new Meta[] { savedMeta1, savedMeta2 };
		comm = new SaveTagPropertiesCommand(tag, metas);
		topicService.executeAndSaveCommand(comm);
		topicS = topicService.getForID(tag.getId());

		assertEquals(2, topicS.getTagProperties().size());



		//
		// remove a meta and ensure that we're back to 1
		//
		metas = new Meta[] { savedMeta2 };
		comm = new SaveTagPropertiesCommand(tag, metas);
		topicService.executeAndSaveCommand(comm);
		topicS = topicService.getForID(tag.getId());

		assertEquals(1, topicS.getTagProperties().size());
	}


	public void testRemoveTagComand() throws HippoException {

		clean();

		Topic tag = new RealTopic(u, C);
		tag = topicService.save(tag);


		Topic topic = new RealTopic(u, D);
		topic = topicService.save(topic);

		AbstractCommand comm = new SaveTagtoTopicCommand(topic, tag);

		topicService.executeAndSaveCommand(comm);

		System.out.println("FINISHED SAVE");


		Topic topicS = topicService.getForID(topic.getId());
		assertEquals(1, topicS.getTypes().size());

		assertEquals(tag, topicS.getTags().iterator().next());



		comm = new RemoveTagFromTopicCommand(topic, tag);
		topicService.executeAndSaveCommand(comm);


		topicS = topicService.getForID(topic.getId());
		assertEquals(0, topicS.getTypes().size());


		Topic tagS = (Topic) topicService.getForID(tag.getId());
		assertEquals(0, topicService.getTopicIdsWithTag(tagS.getId()).size());

	}


	private void clean() throws HippoBusinessException {
		List<DatedTopicIdentifier> savedL = topicService.getAllTopicIdentifiers(true);

		for (TopicIdentifier identifier : savedL) {
			log.debug("Clean: " + identifier.getTopicTitle());
			Topic t = topicService.getForID(identifier.getTopicID());
			// t.getTypes().clear();
			// t.getInstances().clear();
			// t = topicService.save(t);
			topicService.delete(t);
		}
		log.debug("\n-----CLEAN FIN--------");
	}



	/*
	 * public void testGetForName() { fail("Not yet implemented"); }
	 * 
	 * public void testGetTopicsStarting() { fail("Not yet implemented"); }
	 * 
	 * public void testGetTopicIdsWithTag() { fail("Not yet implemented"); }
	 * 
	 * public void testGetAllTopicIdentifiers() { fail("Not yet implemented"); }
	 * 
	 * public void testGetForID() { fail("Not yet implemented"); }
	 * 
	 * public void testGetTimelineObjs() { fail("Not yet implemented"); }
	 * 
	 * public void testSaveTopicArray() { fail("Not yet implemented"); }
	 * 
	 * public void testSaveOccurrence() { fail("Not yet implemented"); }
	 * 
	 * public void testGetLinksTo() { fail("Not yet implemented"); }
	 * 
	 * public void testAddLinkToTags() { fail("Not yet implemented"); }
	 * 
	 * public void testDelete() { fail("Not yet implemented"); }
	 */
}
