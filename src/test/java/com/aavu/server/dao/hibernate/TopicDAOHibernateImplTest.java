package com.aavu.server.dao.hibernate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.HippoDate;
import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaLocation;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.service.gwt.Converter;
import com.aavu.server.web.domain.UserPageBean;

public class TopicDAOHibernateImplTest extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImplTest.class);

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

	private SelectDAO selectDAO;
	private EditDAO editDAO;

	private UserDAO userDAO;

	private User u;

	@Required
	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}

	@Required
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Required
	public void setEditDAO(EditDAO editDAO) {
		this.editDAO = editDAO;
	}

	@Override
	protected void onSetUpInTransaction() throws Exception {
		super.onSetUpInTransaction();


		u = userDAO.getUserByUsername("junit");

	}



	public void testSave() throws HippoBusinessException {


		// topicDAO.getForID(u, 0);


		Topic t = new RealTopic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		// explicitly not setting user, this happens in service layer
		Topic tag = new RealTopic();
		tag.setTitle(D);

		System.out.println("tag " + tag);

		tag = (Topic) editDAO.save(tag);

		t.tagTopic(tag);

		System.out.println("before: " + t.getId());

		System.out.println("TYPES_size " + t.getTypesAsTopics().size());

		editDAO.save(t);

		System.out.println("TYPES_size " + t.getTypesAsTopics().size());


		System.out.println("TOPIC: "
				+ ((TopicTypeConnector) t.getTypes().iterator().next()).getTopic().getId());
		System.out.println("TYPE: "
				+ ((TopicTypeConnector) t.getTypes().iterator().next()).getType().getId());

		System.out.println("after: " + t.getId());

		List<DatedTopicIdentifier> savedL = selectDAO.getAllTopicIdentifiers(u, false);

		// tag does not show up bc user not set
		assertEquals(1, savedL.size());

		TopicIdentifier saved = savedL.get(0);

		Topic savedTopic = selectDAO.getForID(u, saved.getTopicID());

		assertEquals(C, savedTopic.getTitle());
		assertEquals(B, savedTopic.getLatestEntry().getData());
		assertEquals(u, savedTopic.getUser());

		assertEquals(1, savedTopic.getTypesAsTopics().size());

		Topic savedTag = (Topic) savedTopic.getTypesAsTopics().iterator().next();

		assertEquals(D, savedTag.getTitle());

		// Topic's user will still not be initialized, this happens in service
		assertEquals(null, savedTag.getUser());


	}

	public void testSaveComplexMetas() throws HippoBusinessException {

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

		editDAO.save(book);

		Topic tomClancy = new RealTopic();
		tomClancy.setTitle(E);
		editDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: " + patriotGames.getId());

		editDAO.save(patriotGames);

		System.out.println("after: " + patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<DatedTopicIdentifier> savedL = selectDAO.getAllTopicIdentifiers(u, false);

		// NOTE: getAllTopics doesn't take a User right now. That functionality was only used here.
		// List<Topic> allTopics = topicDAO.getAllTopics();
		// Book, Author, PatGames, PatGames->Author, Book->Author
		// assertEquals(5,allTopics.size());
		// assertEquals(5, allTopics.size());

		assertEquals(3, savedL.size());


		assertNotSame(0, patriotGames.getId());
		Topic savedPatriotGames = selectDAO.getForID(u, patriotGames.getId());
		assertNotNull(savedPatriotGames);

		assertEquals(1, savedPatriotGames.getTypesAsTopics().size());
		Topic savedBookTopic = (Topic) savedPatriotGames.getTypesAsTopics().iterator().next();
		assertEquals(D, savedBookTopic.getTitle());
		assertEquals(1, savedBookTopic.getTagProperties().size());

		assertEquals(1, savedPatriotGames.getMetaValuesFor(author).size());
		assertEquals(0, savedPatriotGames.getTagProperties().size());



		Topic savedTomClancy = (Topic) savedPatriotGames.getSingleMetaValueFor(author);
		assertNotNull(savedTomClancy);


		assertEquals(E, savedTomClancy.getTitle());


		// assertEquals(savedBookTopic., false)


		for (TopicIdentifier saved : savedL) {
			// Topic saved = savedL.get(0);

			System.out.println("C");
			Topic top = selectDAO.getForID(u, saved.getTopicID());

			for (Iterator iter = top.getMetaValuesFor(author).iterator(); iter.hasNext();) {
				Topic element = (Topic) iter.next();
				System.out.println("elem " + element.getClass() + " " + element);
			}

			System.out.println(top.toPrettyString());
		}
		// t.setTopics(tags);


	}

	public void testGetTopicsWithTopic() throws HippoBusinessException {


		Topic t = new RealTopic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Topic t2 = new RealTopic();
		t2.getLatestEntry().setData(C);
		t2.setTitle(B);
		t2.setUser(u);

		Topic tag = new RealTopic();
		tag.setTitle("testtagAAA");

		editDAO.save(tag);

		t.addType(tag);

		System.out.println("before: " + t.getId());

		editDAO.save(t);
		editDAO.save(t2);

		System.out.println("after: " + t.getId());

		System.out.println("getting w/ id " + tag.getId());
		List<TopicTypeConnector> savedL = selectDAO.getTopicIdsWithTag(tag.getId(), u);

		Date lastUp = null;
		for (TopicTypeConnector connector : savedL) {
			Date last = connector.getTopic().getLastUpdated();
			System.out.println("last " + last);
			if (lastUp != null) {
				assertTrue(lastUp.after(last));
			}
			lastUp = last;

		}

		System.out.println(savedL.get(0));
		System.out.println("b: " + t.toPrettyString());

		System.out.println(((TopicTypeConnector) savedL.get(0)));


		assertEquals(1, savedL.size());

		TopicTypeConnector b = savedL.get(0);

		assertEquals(b.getTopic().getId(), t.getId());
		assertEquals(b.getTopic().getTitle(), t.getTitle());

		System.out.println("A");
	}

	/**
	 * test that they're returned in lastUpdated order
	 * 
	 * @throws HippoBusinessException
	 * @throws InterruptedException
	 */
	public void testGetTopicsWithTopic2() throws HippoBusinessException, InterruptedException {

		Topic t = new RealTopic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Topic t2 = new RealTopic();
		t2.getLatestEntry().setData(C);
		t2.setTitle(B);
		t2.setUser(u);

		Topic tag = new RealTopic();
		tag.setTitle("testtagAAA");

		editDAO.save(tag);

		t.addType(tag);
		t.setLastUpdated(new Date());
		editDAO.save(t);
		t2 = editDAO.save(t2);

		System.out.println("before: " + t.getId());

		// ensure that they get different times
		Thread.sleep(2000);

		t2.addType(tag);
		t2.setLastUpdated(new Date());
		t2 = editDAO.save(t2);

		System.out.println("after: " + t.getId());

		System.out.println("getting w/ id " + tag.getId());
		List<TopicTypeConnector> savedL = selectDAO.getTopicIdsWithTag(tag.getId(), u);


		assertEquals(2, savedL.size());

		Date lastUp = null;
		for (TopicTypeConnector connector : savedL) {
			Date last = connector.getTopic().getLastUpdated();
			System.out.println("last " + last);
			if (lastUp != null) {
				assertTrue(lastUp.after(last));
			}
			lastUp = last;

		}



	}

	public void testGetAllTopicIdentifiers() throws HippoBusinessException {

		Topic t = new RealTopic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		t = editDAO.save(t);

		List<DatedTopicIdentifier> list = selectDAO.getAllTopicIdentifiers(u, false);

		assertEquals(1, list.size());

		for (TopicIdentifier tident : list) {
			assertEquals(tident.getTopicTitle(), C);
		}


		Topic t2 = new RealTopic(u, D);
		t2.addSeeAlso(t, new MetaSeeAlso());
		t2 = editDAO.save(t2);


		list = selectDAO.getAllTopicIdentifiers(u, false);

		for (TopicIdentifier tident : list) {
			System.out.println("tident " + tident);
		}
		// not 3, even though there's and association
		assertEquals(2, list.size());


		assertEquals(list.get(1).getTopicTitle(), D);
		assertEquals(list.get(0).getTopicTitle(), C);


	}

	public void testSaveSeeAlsos() throws HippoBusinessException {

		Topic author = new RealTopic(u, B);

		MetaSeeAlso metaseealso = new MetaSeeAlso();

		author = editDAO.save(author);

		Topic patGames = new RealTopic(u, C);

		patGames = editDAO.save(patGames);

		author.addSeeAlso(patGames, metaseealso);

		System.out.println("_________________");

		System.out.println(author.toPrettyString());

		author = editDAO.save(author);

		System.out.println("++++++++++++++++++");

		Topic savedST = author.getSeeAlsoAssociation();

		Association savedSee = (Association) savedST;

		System.out.println(author.toPrettyString());
		System.out.println("----------");
		System.out.println("member size " + savedSee.getMembers().size());
		Topic member = (Topic) savedSee.getMembers().iterator().next();
		System.out.println("member: " + member.toPrettyString());
		System.out.println("comp " + member.compare(patGames));

		assertTrue(savedSee.getMembers().contains(patGames));

		//
		// oy, what are we going to do with the cache? I guess just null out cache
		// entries for see alsos on their way through to be saved.
		// bunch of error prone stuff going on with these multi-directional associations
		// that need to be updated on both sides, eh?
		//		
		System.out.println("get id " + author.getId());

		Topic savedStringT = selectDAO.getForID(u, author.getId());

		assertNotNull(savedStringT);



		Topic savedFN = selectDAO.getForID(u, patGames.getId());
		assertNotNull(savedFN);


		//
		// check that the associations were created.
		//
		assertEquals(1, savedStringT.getAssociations().size());
		assertEquals(0, savedFN.getAssociations().size());

		System.out.println("get links to " + savedFN.toPrettyString());

		List<TopicIdentifier> linksTo = selectDAO.getLinksTo(savedFN, u);

		assertNotNull(linksTo);
		assertEquals(1, linksTo.size());

		TopicIdentifier linkTo = linksTo.iterator().next();
		assertEquals(B, linkTo.getTopicTitle());



		//
		// now add a see also and save
		//
		Topic bullcrap = new RealTopic(u, D);
		bullcrap = editDAO.save(bullcrap);
		author.addSeeAlso(bullcrap, metaseealso);
		editDAO.save(author);

		// topicDAO.evict(bullcrap);
		// topicDAO.evict(author);
		// topicDAO.evict(patGames);

		savedStringT = selectDAO.getForID(u, author.getId());
		assertNotNull(savedStringT);

		savedFN = selectDAO.getForID(u, patGames.getId());
		assertNotNull(savedFN);

		Topic savedBull = selectDAO.getForID(u, bullcrap.getId());
		assertNotNull(savedBull);

		//
		// check that the associations were created. There should only be
		// 1 see also for savedStringT, but it shoudl have 2 members.
		//
		assertEquals(1, savedStringT.getAssociations().size());
		assertEquals(0, savedFN.getAssociations().size());
		assertEquals(0, savedBull.getAssociations().size());

		Association secondSeeAlsoSave = savedStringT.getSeeAlsoAssociation();
		assertEquals(2, secondSeeAlsoSave.getMembers().size());

		//
		// now a link to Bull
		//
		List<TopicIdentifier> toBull = selectDAO.getLinksTo(savedBull, u);
		assertEquals(1, toBull.size());

		linkTo = toBull.iterator().next();
		assertEquals(B, linkTo.getTopicTitle());

		//
		// still linked to Feinman
		//
		List<TopicIdentifier> toFein = selectDAO.getLinksTo(savedFN, u);
		assertEquals(1, toFein.size());

		linkTo = toFein.iterator().next();
		assertEquals(B, linkTo.getTopicTitle());



	}

	/**
	 * Main test here is to make sure that the Singleton creating code for SeeAlsos works. This code
	 * is in TopicDaoHibernateImpl.save() It allows us to just say "new MetaSeeAlso()" but not end
	 * up with multiple DB instances of this object that should really be a singleton of some sort.
	 * An alternative solution would be some kind of "getAllSingletonTopics()" service that can be
	 * called on GWT startup. It's basically a bootstrapping problem. We'll run into this again when
	 * we want to creat a global 'Book', 'Movie', or 'Author'... those might be different though
	 * because they're already being loaded remotely. SeeAlsos code need to do this itself. Another
	 * alternative could be a topicService.addSeeAlso() call, but this starts really taking things
	 * out of the Domain. Good and bad aspects to that I suppose.
	 * 
	 * 
	 * Only functions properly with clean DB bc it uses getAllTopics to sweep for accidental null
	 * user topics.
	 * 
	 * @throws HippoBusinessException
	 */
	public void testToMakeSureWeDontCreateTooManyObjects() throws HippoBusinessException {

		// topicDAO.deleteAllTables();

		Topic patriotGames = new RealTopic(u, C);
		patriotGames.getLatestEntry().setData(B);

		Topic book = new RealTopic(u, D);

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);

		book.addTagProperty(author);

		editDAO.save(book);

		Topic tomClancy = new RealTopic(u, E);
		editDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: " + patriotGames.getId());

		editDAO.save(patriotGames);

		Topic savedPat = selectDAO.getForName(u, C);
		assertEquals(1, savedPat.getAssociations().size());
		assertNotNull(savedPat.getMetaValuesFor(author));
		Topic savedClancy = (Topic) savedPat.getSingleMetaValueFor(author);

		// System.out.println(savedPat.toPrettyString());
		// System.out.println(savedClancy.toPrettyString());

		List<Topic> allTopics = selectDAO.getAllTopics(u);

		for (Topic topic2 : allTopics) {
			System.out.println("topic " + topic2);
		}
		// should be seven. Root, PatGames, Book, Author, TomClancy. and the two associations
		assertEquals(7, allTopics.size());

		Topic recommender = new RealTopic(u, F);
		recommender = editDAO.save(recommender);

		MetaSeeAlso metaSeeAlso = new MetaSeeAlso();

		savedPat.addSeeAlso(recommender, metaSeeAlso);
		Topic savedPat2 = editDAO.save(savedPat);

		allTopics = selectDAO.getAllTopics(u);
		for (Topic topic : allTopics) {
			System.out.println("topic " + topic + " " + topic.getId() + " " + topic.getClass());
		}
		// //should be nine. 7 from before plus: Recommender, Patriot->Recommender
		assertEquals(9, allTopics.size());


		Topic anotherBook = new RealTopic(u, G);
		editDAO.save(anotherBook);

		savedPat2.addSeeAlso(anotherBook, metaSeeAlso);

		Topic savedPat3 = editDAO.save(savedPat2);

		assertEquals(2, savedPat3.getSeeAlsoAssociation().getMembers().size());

		allTopics = selectDAO.getAllTopics(u);
		for (Topic topic : allTopics) {
			System.out.println("topic " + topic + " " + topic.getId() + " " + topic.getClass());
		}
		// //should be just 10. 9 from before plus: Another
		//
		assertEquals(10, allTopics.size());


		Topic anotherRecommender = new RealTopic(u, H);
		editDAO.save(anotherRecommender);

		anotherRecommender.addSeeAlso(savedPat3, metaSeeAlso);
		editDAO.save(anotherRecommender);


		allTopics = selectDAO.getAllTopics(u);
		for (Topic topic : allTopics) {
			System.out.println("topic " + topic + " " + topic.getId() + " " + topic.getClass());
		}
		// //should be just 12. 10 from before plus: AnotherRecommender & AnotherRecomender's
		// Association
		assertEquals(12, allTopics.size());
	}



	public void testGetTimeline() {

		MetaDate md = new MetaDate();
		md.setId(9);

		Topic tag = new RealTopic();
		tag.setId(3);

		List<TimeLineObj> list = selectDAO.getTimeline(0, u);

		for (TimeLineObj timeLine : list) {
			System.out.println("timelineObj " + timeLine);
		}


		System.out.println("list " + list.size());
	}

	public void testGetTimelineAll_1() throws HippoBusinessException {

		Topic t1 = new RealTopic(u, C);


		MetaDate md = new MetaDate();
		md.setTitle("Date Seen");

		HippoDate date = new HippoDate();
		date.setStartDate(new Date());

		t1.addMetaValue(md, date);

		t1 = editDAO.save(t1);

		List<TimeLineObj> list = selectDAO.getTimeline(u);

		assertEquals(1, list.size());

		for (TimeLineObj timeLine : list) {
			System.out.println("timelineObj " + timeLine);
		}

		System.out.println("list " + list.size());
	}

	public void testGetTimelineAll_2() throws HippoBusinessException {

		// Add a topic w/ 2 meta dates.
		// Make sure that 'limitToTheseMetas' works
		//
		Topic t1 = new RealTopic(u, C);

		MetaDate md = new MetaDate();
		md.setTitle("Date Seen");
		HippoDate date = new HippoDate();
		date.setStartDate(new Date());
		t1.addMetaValue(md, date);
		t1 = editDAO.save(t1);

		MetaDate md2 = new MetaDate();
		md2.setTitle("Date Read");
		HippoDate date2 = new HippoDate();
		date2.setStartDate(new Date());
		t1.addMetaValue(md2, date2);

		assertEquals(2, t1.getAllMetas(new MetaDate()).size());

		t1 = editDAO.save(t1);

		List<TimeLineObj> list = selectDAO.getTimeline(u);
		assertEquals(2, list.size());

		for (TimeLineObj timeLine : list) {
			System.out.println("timelineObj " + timeLine);
		}

		System.out.println("2");



		//
		// add a second topic, with a meta date for each meta
		//
		Topic t2 = new RealTopic(u, E);
		Topic tag = new RealTopic(u, D);
		t2.tagTopic(tag);

		for (Iterator iter = t1.getMetas().iterator(); iter.hasNext();) {
			Meta m = (Meta) iter.next();

			HippoDate adate = new HippoDate();
			adate.setStartDate(new Date());
			t2.addMetaValue(m, adate);
			t2 = editDAO.save(t2);
		}

		list = selectDAO.getTimeline(u);
		assertEquals(4, list.size());

		Topic tt = (Topic) t2.getTags().iterator().next();

		list = selectDAO.getTimeline(tt.getId(), u);
		assertEquals(2, list.size());



	}

	public void testTimelineWithOldDates() throws HippoBusinessException {

		// Add a topic w/ 2 meta dates.
		// Make sure that 'limitToTheseMetas' works
		//
		Topic t1 = new RealTopic(u, C);

		MetaDate md = new MetaDate();
		md.setTitle("Date Seen");
		HippoDate date = new HippoDate();

		Date oldDate = new Date();
		oldDate.setYear(-300);

		date.setStartDate(oldDate);
		t1.addMetaValue(md, date);
		t1 = editDAO.save(t1);

		MetaDate md2 = new MetaDate();
		md2.setTitle("Date Read");
		HippoDate date2 = new HippoDate();

		Date oldDate2 = new Date();
		oldDate2.setYear(-500);
		oldDate2.setMonth(-40);
		date2.setStartDate(oldDate2);
		t1.addMetaValue(md2, date2);

		assertEquals(2, t1.getAllMetas(new MetaDate()).size());

		t1 = editDAO.save(t1);

		List<TimeLineObj> list = selectDAO.getTimeline(u);
		assertEquals(2, list.size());

		// Use fuzzyEq, bc DB seems to truncate milliseconds
		for (TimeLineObj timeLine : list) {
			assertTrue(fuzzyDateEq(timeLine.getStart(), oldDate2)
					|| fuzzyDateEq(timeLine.getStart(), oldDate));
		}

		System.out.println("2");



		//
		// add a second topic, with a meta date for each meta
		//
		Topic t2 = new RealTopic(u, E);
		Topic tag = new RealTopic(u, D);
		t2.tagTopic(tag);

		for (Iterator iter = t1.getMetas().iterator(); iter.hasNext();) {
			Meta m = (Meta) iter.next();

			HippoDate adate = new HippoDate();

			Date oldDate3 = new Date();
			oldDate3.setYear(-800);// 1100AD
			adate.setStartDate(oldDate3);
			t2.addMetaValue(m, adate);
			t2 = editDAO.save(t2);
		}

		list = selectDAO.getTimeline(u);
		assertEquals(4, list.size());

		Topic tt = (Topic) t2.getTags().iterator().next();

		list = selectDAO.getTimeline(tt.getId(), u);
		assertEquals(2, list.size());

		// huh, not sure why this works
		// http://dev.mysql.com/doc/refman/5.0/en/datetime.html
		// The supported range is '1000-01-01 00:00:00' to '9999-12-31 23:59:59'.
		Topic tooOld = new RealTopic(u, G);
		Date tooOldDate = new Date();
		tooOldDate.setYear(-1850);// 50AD
		tooOld.setCreated(tooOldDate);

		editDAO.save(tooOld);

		Topic saved = selectDAO.getForName(u, G);

		System.out.println("Too Old  " + saved.getCreated() + " " + tooOldDate);

		assertTrue(fuzzyDateEq(tooOldDate, saved.getCreated()));

		// finally we get something that's really too old, but this is a JAVA too old,
		// not a DB too old.
		//
		Topic waytooOld = new RealTopic(u, F);
		Date waytooOldDate = new Date();
		waytooOldDate.setYear(-2100);// 200BC
		waytooOld.setCreated(waytooOldDate);

		editDAO.save(waytooOld);

		Topic nsaved = selectDAO.getForName(u, F);

		System.out.println("old " + oldDate2.getYear() + " " + oldDate2);
		System.out.println("Way Too Old " + nsaved.getCreated() + " " + waytooOldDate + " "
				+ nsaved.getCreated().getYear() + " " + waytooOldDate.getYear());

		assertTrue(fuzzyDateEq(waytooOldDate, nsaved.getCreated()));

		// BUT! we've wrapped at year 0, so 200BC --> 200AD
		assertTrue(nsaved.getCreated().getYear() > -2100);


	}

	private boolean fuzzyDateEq(Date d1, Date d2) {
		System.out.println("d1 " + d1);
		System.out.println("d2 " + d2);
		System.out.println("diff " + Math.abs(d1.getTime() - d2.getTime()));
		return Math.abs(d1.getTime() - d2.getTime()) < 2000;
	}


	public void testGetMapAll_1() throws HippoBusinessException {

		Topic t1 = new RealTopic(u, C);


		MetaLocation md = new MetaLocation();
		md.setTitle("Where");

		HippoLocation loc = new HippoLocation();
		loc.setLatitude(400);
		loc.setLongitude(350);

		t1.addMetaValue(md, loc);

		t1 = editDAO.save(t1);

		List<LocationDTO> list = selectDAO.getLocations(u);

		assertEquals(1, list.size());

		for (LocationDTO sloc : list) {
			System.out.println("LocationDTO " + sloc);
		}

		System.out.println("list " + list.size());
	}

	/**
	 * NOTE: if HippoLocation titles are not set, the association will get the same name and this
	 * may trigger a .eq where we don't really want it, leading to association #2 not getting added.
	 * 
	 * TODO, change association .eq() to not .eq() based on title??
	 * 
	 * @throws HippoBusinessException
	 */
	public void testGetMapAll_2() throws HippoBusinessException {

		// Add a topic w/ 2 meta locations.
		// Make sure that 'limitToTheseMetas' works
		//
		Topic t1 = new RealTopic(u, C);

		MetaLocation md = new MetaLocation();
		md.setTitle("Where");
		HippoLocation loc = new HippoLocation();
		loc.setTitle("555, 2323");
		loc.setLatitude(555);
		loc.setLongitude(2323);
		t1.addMetaValue(md, loc, false);
		t1 = editDAO.save(t1);

		System.out.println("FIRST " + t1.toPrettyString());

		MetaLocation md2 = new MetaLocation();
		md2.setTitle("Birthplace");
		HippoLocation loc2 = new HippoLocation();
		loc2.setTitle("999, 111");
		loc2.setLatitude(999);
		loc2.setLongitude(111);
		t1.addMetaValue(md2, loc2, false);

		System.out.println("SAVING " + t1.toPrettyString());

		t1 = editDAO.save(t1);

		Topic saved = selectDAO.getForName(u, C);
		System.out.println("SAVED " + saved.toPrettyString());


		List<LocationDTO> list = selectDAO.getLocations(u);
		assertEquals(2, list.size());

		for (LocationDTO sloc : list) {
			System.out.println("Location " + sloc);
		}

		System.out.println("2");



		//
		// add a second topic, with a meta date for each meta
		//
		Topic t2 = new RealTopic(u, E);
		Topic tag = new RealTopic(u, D);
		t2.tagTopic(tag);


		int i = 0;
		for (Iterator iter = t1.getMetas().iterator(); iter.hasNext();) {
			Meta m = (Meta) iter.next();

			HippoLocation aloc = new HippoLocation();
			aloc.setTitle(i + ", 3344");
			aloc.setLatitude(i);
			aloc.setLongitude(3344);
			t2.addMetaValue(m, aloc);
			t2 = editDAO.save(t2);
			i++;
		}

		list = selectDAO.getLocations(u);
		assertEquals(4, list.size());

		Topic tt = (Topic) t2.getTags().iterator().next();

		list = selectDAO.getLocations(tt.getId(), u);
		assertEquals(2, list.size());



	}


	public void testSubjectSave() throws HippoBusinessException {
		Topic t = new RealTopic(u, B);

		Subject b_Subj = new AmazonBook();
		b_Subj.setForeignID(D);
		b_Subj.setName(E);

		t.setSubject(b_Subj);

		editDAO.save(t);

		Topic savedT = selectDAO.getForName(u, B);

		assertEquals(D, savedT.getSubject().getForeignID());


		//
		// Test that a second saved topic with the same subject
		// get's the same subject object
		//
		Subject c_Subj = new AmazonBook();
		c_Subj.setForeignID(D);
		c_Subj.setName(E);

		assertEquals(0, c_Subj.getId());

		// hmm. interesting. I guess that's right, although maybe they should be .eq logically.
		// dunno.
		assertNotSame(b_Subj, c_Subj);

		Topic t2 = new RealTopic(u, C);
		t2.setSubject(c_Subj);
		editDAO.save(t2);

		Topic s1 = selectDAO.getForName(u, B);
		Topic s2 = selectDAO.getForName(u, C);

		Subject ss1 = s1.getSubject();
		Subject ss2 = s2.getSubject();

		assertTrue(ss1.equals(ss2));
		assertEquals(ss1.getId(), ss2.getId());
		assertEquals(ss1.getForeignID(), ss2.getForeignID());
		assertEquals(ss1, ss2);



	}

	/**
	 * This test starts working when we add Occurences cascade="save-update"
	 * 
	 * @throws HippoBusinessException
	 */
	public void testSubjectSaveTransientProblem() throws HippoBusinessException {
		Topic t = new RealTopic(u, B);

		editDAO.save(t);

		Topic savedT = selectDAO.getForName(u, B);

		Subject b_Subj = new AmazonBook();
		b_Subj.setForeignID(D);
		b_Subj.setName(E);

		t.setSubject(b_Subj);

		editDAO.save(t);

		Topic savedTAgain = selectDAO.getForName(u, B);

		assertEquals(D, savedTAgain.getSubject().getForeignID());

	}

	public void testSaveLink() throws HippoBusinessException {
		WebLink link = new WebLink(u, B, C, D);

		link = (WebLink) editDAO.save(link);

		String string = "";


		log.debug("str: " + string);
		if (string.equals("")) {
			string = C;
			log.debug("blank tags, setting topic to; " + string);
		}
		Topic t = selectDAO.getForName(u, string);

		if (null == t) {
			log.debug("was null, creating as Topic ");
			t = new RealTopic();
			t.setTitle(string);
			t.setUser(u);
		}

		assertEquals(0, t.getOccurenceObjs().size());

		t.addOccurence(link);
		assertEquals(1, t.getOccurenceObjs().size());

		System.out.println("-----t-----" + t.toPrettyString());
		Topic st = editDAO.save(t);
		System.out.println("-----st-----" + st.toPrettyString());

		assertEquals(1, st.getOccurenceObjs().size());

	}

	/**
	 * Should let us link occurrence the topic that uses it..
	 * 
	 * A bit confused.. shoudl this really be a one-way many-to-many like it is now? Can an
	 * occurrence exist for many topics? Ahh. crap, yes it can. Hmm.
	 * 
	 * 
	 * @throws HippoBusinessException
	 */
	public void testGetTopicForOccurrence() throws HippoBusinessException {
		WebLink link = new WebLink(u, B, C, D);
		link = (WebLink) editDAO.save(link);
		String string = "";
		log.debug("str: " + string);
		if (string.equals("")) {
			string = C;
			log.debug("blank tags, setting topic to; " + string);
		}
		Topic t = selectDAO.getForName(u, string);
		if (null == t) {
			log.debug("was null, creating as Topic ");
			t = new RealTopic();
			t.setTitle(string);
			t.setUser(u);
		}
		assertEquals(0, t.getOccurenceObjs().size());
		t.addOccurence(link);
		assertEquals(1, t.getOccurenceObjs().size());
		Topic st = editDAO.save(t);

		System.out.println("ling " + link.getId());
		List<TopicIdentifier> ident = selectDAO.getTopicForOccurrence(link.getId(), u);
		assertEquals(t.getId(), ident.get(0).getTopicID());

	}


	// public void testGetTree() throws HippoBusinessException{

	// Topic t = new RealTopic(u,B);
	// t = topicDAO.save(t);

	// MindTreeOcc occ = new MindTreeOcc(t);
	// MindTree tree = occ.getMindTree();
	// tree.getRightSide().add(new MindTreeElement("Foo",null,0,3));
	// tree.getRightSide().add(new MindTreeElement("Foo2",null,1,2));
	// tree.getLeftSide().add(new MindTreeElement("FooL",null,0,1));


	// //save tree explicitly
	// topicDAO.save(tree);

	// // //save occ explicitly?
	// // topicDAO.save(occ);

	// t.addOccurrence(occ);

	// Topic saved = topicDAO.save(t);

	// assertEquals(1, saved.getOccurences().size());

	// MindTreeOcc socc = (MindTreeOcc) saved.getOccurences().iterator().next();

	// MindTree savedTree = topicDAO.getTree(socc);

	// assertNotNull(savedTree);

	// assertEquals(2, savedTree.getRightSide().size());
	// assertEquals(1, savedTree.getLeftSide().size());
	// }


	public void testDelete() throws HippoBusinessException {
		Topic patriotGames = new RealTopic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Topic book = new RealTopic(u, D);

		editDAO.save(book);


		patriotGames.tagTopic(book);

		System.out.println("before: " + patriotGames.getId());

		editDAO.save(patriotGames);

		System.out.println("after: " + patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<DatedTopicIdentifier> savedL = selectDAO.getAllTopicIdentifiers(u, false);

		assertEquals(2, savedL.size());


		book = (Topic) selectDAO.getForName(u, D);

		System.out.println("patgames " + patriotGames.toPrettyString());
		System.out.println("book " + book.toPrettyString());

		// assertEquals(1,book.getInstances().size());

		editDAO.delete(patriotGames);

		savedL = selectDAO.getAllTopicIdentifiers(u, false);
		assertEquals(1, savedL.size());

		book = (Topic) selectDAO.getForName(u, D);
		// assertEquals(0,book.getInstances().size());


	}

	public void testDeleteAdvanced() throws HippoBusinessException {
		Topic patriotGames = new RealTopic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Topic book = new RealTopic(u, D);

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);
		book.addTagProperty(author);

		editDAO.save(book);

		Topic tomClancy = new RealTopic();
		tomClancy.setTitle(E);
		editDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: " + patriotGames.getId());


		editDAO.save(patriotGames);

		Entry lastEntry = patriotGames.getLatestEntry();

		System.out.println("after: " + patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<DatedTopicIdentifier> savedL = selectDAO.getAllTopicIdentifiers(u, false);

		assertEquals(3, savedL.size());


		book = (Topic) selectDAO.getForName(u, D);
		// assertEquals(1,book.getInstances().size());

		editDAO.delete(patriotGames);

		savedL = selectDAO.getAllTopicIdentifiers(u, false);
		assertEquals(2, savedL.size());

		book = (Topic) selectDAO.getForName(u, D);
		// assertEquals(0,book.getInstances().size());

		// TODO assert that lastEntry has been deleted
	}

	public void testDeleteAdvanced2() throws HippoBusinessException {
		Topic patriotGames = new RealTopic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Topic forWhomTheBellTolls = new RealTopic();
		forWhomTheBellTolls.getLatestEntry().setData(B);
		forWhomTheBellTolls.setTitle(F);
		forWhomTheBellTolls.setUser(u);

		Topic book = new RealTopic(u, D);

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);
		book.addTagProperty(author);

		editDAO.save(book);

		Topic tomClancy = new RealTopic();
		tomClancy.setTitle(E);
		editDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: " + patriotGames.getId());

		editDAO.save(patriotGames);


		forWhomTheBellTolls.tagTopic(book);
		editDAO.save(forWhomTheBellTolls);

		Entry lastEntry = patriotGames.getLatestEntry();

		System.out.println("after: " + patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<DatedTopicIdentifier> savedL = selectDAO.getAllTopicIdentifiers(u, false);

		assertEquals(4, savedL.size());


		book = (Topic) selectDAO.getForName(u, D);
		// assertEquals(2,book.getInstances().size());

		editDAO.delete(patriotGames);

		savedL = selectDAO.getAllTopicIdentifiers(u, false);
		assertEquals(3, savedL.size());

		book = (Topic) selectDAO.getForName(u, D);
		// assertEquals(1,book.getInstances().size());



	}

	public void testDeleteTopic() throws HippoBusinessException {
		Topic patriotGames = new RealTopic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Topic book = new RealTopic(u, D);

		MetaTopic author = new MetaTopic();
		author.setTitle(B);
		author.setUser(u);
		book.addTagProperty(author);

		editDAO.save(book);

		Topic tomClancy = new RealTopic();
		tomClancy.setTitle(E);
		editDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: " + patriotGames.getId());


		editDAO.save(patriotGames);

		Entry lastEntry = patriotGames.getLatestEntry();

		System.out.println("after: " + patriotGames.getId());

		System.out.println(patriotGames.toPrettyString());

		List<DatedTopicIdentifier> savedL = selectDAO.getAllTopicIdentifiers(u, false);

		assertEquals(3, savedL.size());


		book = (Topic) selectDAO.getForName(u, D);
		// assertEquals(1,book.getInstances().size());

		editDAO.delete(book);

		savedL = selectDAO.getAllTopicIdentifiers(u, false);
		assertEquals(2, savedL.size());

		// assert that Book is no longer there
		for (TopicIdentifier identifier : savedL) {
			assertNotSame(D, identifier.getTopicTitle());
		}

		patriotGames = (Topic) selectDAO.getForName(u, C);
		assertEquals(0, patriotGames.getTypes().size());

	}


	public void testSaveTopicsLocation() throws HippoBusinessException {


		Topic t = new RealTopic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		Topic t2 = new RealTopic();
		t2.getLatestEntry().setData(C);
		t2.setTitle(B);
		t2.setUser(u);

		Topic tag = new RealTopic();
		tag.setTitle("testtagAAA");

		tag = (Topic) editDAO.save(tag);

		t.addType(tag);

		System.out.println("before: " + t.getId());

		editDAO.save(t);
		editDAO.save(t2);

		System.out.println("after: " + t.getId());

		List<TopicTypeConnector> savedL = selectDAO.getTopicIdsWithTag(tag.getId(), u);

		System.out.println(savedL.get(0));
		System.out.println("b: " + t.toPrettyString());

		System.out.println(((TopicTypeConnector) savedL.get(0)));


		assertEquals(1, savedL.size());

		TopicTypeConnector b = savedL.get(0);

		assertEquals(b.getTopic().getId(), t.getId());
		assertEquals(b.getTopic().getTitle(), t.getTitle());

		System.out.println("A");



		List<TopicTypeConnector> topicsWithTopic = selectDAO.getTopicIdsWithTag(tag.getId(), u);
		assertEquals(1, topicsWithTopic.size());
		TopicTypeConnector fti = topicsWithTopic.get(0);
		assertEquals(-1, fti.getLatitude());
		assertEquals(-1, fti.getLongitude());

		// sysout
		editDAO.saveTopicsLocation(tag.getId(), t.getId(), 23, 47, u);

		topicsWithTopic = selectDAO.getTopicIdsWithTag(tag.getId(), u);
		assertEquals(1, topicsWithTopic.size());
		fti = topicsWithTopic.get(0);
		assertEquals(23, fti.getLatitude());
		assertEquals(47, fti.getLongitude());



	}

	public void testSaveOccLocation() throws HippoBusinessException {

		Topic t = new RealTopic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);

		t = editDAO.save(t);

		Entry e = t.getLatestEntry();

		int Y = 4545;
		int X = 232;
		editDAO.saveOccurrenceLocation(t.getId(), e.getId(), Y, X, u);

		Topic saved = selectDAO.getForID(u, t.getId());

		TopicOccurrenceConnector owl = (TopicOccurrenceConnector) saved.getOccurences().iterator()
				.next();

		assertEquals(Y, owl.getLatitude());
		assertEquals(X, owl.getLongitude());
	}

	public void testPopualateUsageStats() {

		UserPageBean bean = selectDAO.getUsageStats(u);

		System.out.println(bean);
	}



	public void testSerializationWHibernateSupport() {

		User uu = new User();
		uu.setId(1);
		Topic t = selectDAO.getForID(uu, 208);

		String str = Converter.serializeWithHibernateSupport(t);

		assertFalse(str.contains("java.sql.Timestamp"));

		System.out.println("pass 1");

		assertFalse(str.contains("Persistent"));

		System.out.println("pass 2");

		assertFalse(str.contains("CGLIB"));

		System.out.println("pass 3!!!");

	}

	public void testSerializationOfBigComplexWHibernate() {

		User uu = new User();
		uu.setId(1);
		Topic t = selectDAO.getForID(uu, 715);

		String str = Converter.serializeWithHibernateSupport(t);

		assertFalse(str.contains("CGLIB"));

		assertFalse(str.contains("Persistent"));

		assertFalse(str.contains("java.sql.Timestamp"));

	}

	public void testSerializationOfMany() {

		User uu = new User();
		uu.setId(1);
		Topic t = selectDAO.getForID(uu, 515);

		String str = Converter.serializeWithHibernateSupport(t);

		assertFalse(str.contains("CGLIB"));
		assertFalse(str.contains("Persistent"));
		assertFalse(str.contains("java.sql.Timestamp"));

		t = selectDAO.getForID(uu, 707);
		str = Converter.serializeWithHibernateSupport(t);
		assertFalse(str.contains("CGLIB"));
		assertFalse(str.contains("Persistent"));
		assertFalse(str.contains("java.sql.Timestamp"));


		t = selectDAO.getForID(uu, 208);
		str = Converter.serializeWithHibernateSupport(t);
		assertFalse(str.contains("CGLIB"));
		assertFalse(str.contains("Persistent"));
		assertFalse(str.contains("java.sql.Timestamp"));

		t = selectDAO.getForID(uu, 970);
		str = Converter.serializeWithHibernateSupport(t);
		assertFalse(str.contains("CGLIB"));
		assertFalse(str.contains("Persistent"));
		assertFalse(str.contains("java.sql.Timestamp"));
	}

	public void testGetWebLinkForURI() {

		// System.out.println("u "+u.getUsername());
		WebLink w = selectDAO.getWebLinkForURI("http://www.google.com/", u);
		// assertNotNull(w);

	}

	public void testDeleteOccurrence() throws HippoBusinessException {

		Topic t = new RealTopic();
		t.setTitle(C);
		t.setUser(u);

		t = editDAO.save(t);

		Topic savedTopic = selectDAO.getForID(u, t.getId());

		assertEquals(C, savedTopic.getTitle());
		assertEquals(u, savedTopic.getUser());

		savedTopic.getLatestEntry().setData(B);
		savedTopic = editDAO.save(savedTopic);

		Topic savedTopic2 = selectDAO.getForID(u, t.getId());
		assertEquals(B, savedTopic2.getLatestEntry().getData());
		System.out.println("SAVED 2: " + savedTopic2.getLatestEntry().getData());
		System.out.println("SAVED 2: " + savedTopic2.getLatestEntry().getId());
		System.out.println("SAVED 2: " + savedTopic2.getLatestEntry().getTopics());
		System.out.println("SAVED 2: " + savedTopic2.getLatestEntry().getTopics().size());
		System.out.println("saved " + savedTopic2.getOccurenceObjs().size());

		editDAO.deleteOccurrence(savedTopic2.getLatestEntry());


		Topic savedTopic3 = selectDAO.getForID(u, t.getId());
		assertTrue(savedTopic3.getLatestEntry().isEmpty());

	}

	public void testGetAllMetas() throws HippoBusinessException {

		Topic patriotGames = new RealTopic();
		patriotGames.getLatestEntry().setData(B);
		patriotGames.setTitle(C);
		patriotGames.setUser(u);

		Topic book = new RealTopic(u, D);

		MetaText ss = new MetaText();
		MetaTopic author = new MetaTopic();
		MetaDate read = new MetaDate();

		author.setTitle(B);
		author.setUser(u);
		book.addTagProperty(author);

		ss.setTitle("F");
		ss.setUser(u);
		book.addTagProperty(ss);

		read.setTitle("Read");
		read.setUser(u);
		book.addTagProperty(read);

		editDAO.save(book);

		Topic tomClancy = new RealTopic();
		tomClancy.setTitle(E);
		editDAO.save(tomClancy);

		patriotGames.tagTopic(book);
		patriotGames.addMetaValue(author, tomClancy);

		System.out.println("before: " + patriotGames.getId());

		editDAO.save(patriotGames);



		List<Meta> allMetas = selectDAO.getAllMetas(u);
		assertEquals(3, allMetas.size());
		for (Meta meta : allMetas) {

		}

	}

	public void testGetPublic() throws HippoBusinessException {
		Topic t = new RealTopic();
		t.setPublicVisible(true);
		t.setTitle(C);
		t.setUser(u);

		t = editDAO.save(t);

		Topic saved = selectDAO.getPublicForName(u.getUsername(), C);

		assertNotNull(saved);
		assertEquals(saved.getTitle(), t.getTitle());



		saved.setPublicVisible(false);
		editDAO.save(saved);
		Topic saved2 = selectDAO.getPublicForName(u.getUsername(), C);
		assertNull(saved2);


	}

	public void testGetPublicConnections() throws HippoBusinessException {
		Topic t = new RealTopic();
		t.setPublicVisible(true);
		t.setTitle(C);
		t.setUser(u);

		t = editDAO.save(t);


		Topic tag = new RealTopic(u, D);
		tag.setPublicVisible(true);
		t.tagTopic(tag);

		tag = (Topic) editDAO.save(tag);

		List<TopicTypeConnector> saved = selectDAO.getTopicIdsWithTag(tag.getId());

		assertNotNull(saved);
		assertEquals(1, saved.size());


		t.setPublicVisible(false);
		editDAO.save(t);

		saved = selectDAO.getTopicIdsWithTag(tag.getId());

		assertNotNull(saved);
		assertEquals(0, saved.size());


	}

	public void testT() {
		Topic t = selectDAO.get(2457);

		assertTrue(t instanceof MetaLocation);
	}



}
