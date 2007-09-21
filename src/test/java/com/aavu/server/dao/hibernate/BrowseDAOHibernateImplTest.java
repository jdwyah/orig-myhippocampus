package com.aavu.server.dao.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.aavu.client.domain.RealTopic;
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
	}

	public void testGetTopWeblinks() throws HippoBusinessException {

		List<WebLink> links = browseDAO.getTopWeblinks();

		Set<Long> ids = new HashSet<Long>();

		for (WebLink link : links) {
			System.out.println("Weblink " + link + " ");

			assertFalse(ids.contains(link.getId()));
			ids.add(link.getId());
		}

		assertEquals(BrowseDAOHibernateImpl.MAX_WEBLINKS, links.size());


	}
}
