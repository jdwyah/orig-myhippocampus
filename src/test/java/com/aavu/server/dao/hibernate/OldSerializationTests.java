package com.aavu.server.dao.hibernate;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;
import com.aavu.server.service.gwt.Converter;
import com.aavu.server.service.gwt.NewConverter;

public class OldSerializationTests extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(OldSerializationTests.class);


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

}
