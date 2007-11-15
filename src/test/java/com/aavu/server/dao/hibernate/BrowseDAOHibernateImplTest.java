package com.aavu.server.dao.hibernate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.BrowseDAO;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.dao.UserDAO;

public class BrowseDAOHibernateImplTest extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(BrowseDAOHibernateImplTest.class);

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

	private BrowseDAO browseDAO;

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
	public void setBrowseDAO(BrowseDAO browseDAO) {
		this.browseDAO = browseDAO;
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



	public void testGetTopTopics() throws HippoBusinessException {

		List<RealTopic> topics = browseDAO.getTopTopics();

		Set<Long> ids = new HashSet<Long>();

		for (RealTopic topic : topics) {
			System.out.println("Topic " + topic + " " + topic.getId());

			assertFalse(ids.contains(topic.getId()));
			ids.add(topic.getId());
		}

		assertEquals(BrowseDAOHibernateImpl.MAX_TOPICS, topics.size());

		for (RealTopic topic : topics) {
			assertTrue(topic.getInstances().size() > 2);
			for (Iterator iterator = topic.getInstances().iterator(); iterator.hasNext();) {
				TopicTypeConnector ttc = (TopicTypeConnector) iterator.next();
				assertNotNull(ttc.getTopic());
				assertNotNull(ttc.getTopic().getTitle());
				assertTrue(ttc.getTopic().isPublicVisible());
				System.out.println(ttc.getTopic().getTitle());
			}
		}

	}

	public void testGetTopWeblinks() throws HippoBusinessException {

		List<WebLink> links = browseDAO.getTopWeblinks();

		Set<Long> ids = new HashSet<Long>();

		Set<Long> userids = new HashSet<Long>();

		for (WebLink link : links) {
			System.out.println("Weblink " + link + " " + " Topics:" + link.getTopics().size());

			assertFalse(ids.contains(link.getId()));

			assertTrue(link.isPublicVisible());

			assertFalse(userids.contains(link.getUser().getId()));

			ids.add(link.getId());
			userids.add(link.getUser().getId());

			for (Iterator iterator = link.getTopics().iterator(); iterator.hasNext();) {
				TopicOccurrenceConnector toc = (TopicOccurrenceConnector) iterator.next();
				assertTrue(toc.getTopic().isPublicVisible());
			}

		}

		assertTrue(BrowseDAOHibernateImpl.MAX_WEBLINKS >= links.size());


	}
}
