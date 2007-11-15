package com.aavu.server.dao.hibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.server.dao.BrowseDAO;

/**
 * 
 * @author Jeff Dwyer
 * 
 */
public class BrowseDAOHibernateImpl extends HibernateDaoSupport implements BrowseDAO {

	private static final Logger log = Logger.getLogger(BrowseDAOHibernateImpl.class);

	public static final int MAX_TOPICS = 7;
	public static final int MAX_WEBLINKS = 7;

	protected static final int PAGE_SIZE = 20;

	private static ArrayList<WebLink> topWeblinks;


	/**
	 * simple caching w/ timer
	 */
	public BrowseDAOHibernateImpl() {
		Timer timer = new Timer();
		long twoHours = 1000 * 60 * 60 * 2;
		// delay 2 hours bc we can't run before our setters are called
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				createTopWeblinks();
			}
		}, twoHours, twoHours);

	}

	/**
	 * PEND take another look at the efficiency of these guys.
	 * 
	 * setMaxResults w/ fetch -> warnings about doing things in memory.. not that we're not doing it
	 * in memory this way too...
	 * 
	 * http://www.hibernate.org/314.html
	 * http://www.hibernate.org/hib_docs/reference/en/html/queryhql.html
	 * 
	 * @param hql
	 * @param maxResults
	 * @return
	 */
	private Object getLimittedResults(final String hql, final int maxResults) {
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(hql);


				// // WARN : org.hibernate.hql.ast.QueryTranslatorImpl#list : firstResult/maxResults
				// // specified with collection fetch; applying in memory!
				// queryObject.setMaxResults(maxResults);

				List<Object> rtnList = new ArrayList<Object>(maxResults);

				List<Topic> resultList = queryObject.list();

				Set<Long> ids = new HashSet<Long>();

				for (Topic topic : resultList) {
					if (rtnList.size() >= maxResults) {
						break;
					}
					if (ids.contains(topic.getId())) {
						continue;
					} else {
						ids.add(topic.getId());
						rtnList.add(topic);
					}
				}
				return rtnList;

				// for (int i = 0; i < maxResults; i++) {
				//
				// Object[] r = resultList.get(i);
				//
				// System.out.println("r[0] " + r[0] + " r[1] " + r[1]);
				//
				// rtnList.add(resultList.get(i)[paramIdx]);
				// }
				// return rtnList;

				// if (maxResults < resultList.size()) {
				// return resultList.subList(0, maxResults);
				// } else {
				// return resultList;
				// }



				// List returnList = new ArrayList(maxResults);
				// ScrollableResults results = queryObject.scroll(ScrollMode.FORWARD_ONLY);
				// if (results.first()) {
				//
				// do {
				// Object[] oa = results.get();
				// returnList.add(oa[0]);
				//
				// } while (results.scroll(PAGE_SIZE));
				//
				// // Now get the first page of cats
				// pageOfCats = new ArrayList();
				// cats.beforeFirst();
				// int i = 0;
				// while ((PAGE_SIZE > i++) && cats.next())
				// pageOfCats.add(cats.get(1));
				//
				// }
				// results.close();
				// return returnList;

			}
		});
	}

	public List<RealTopic> getTopTopics() {

		// DetachedCriteria crit = DetachedCriteria.forClass(RealTopic.class).setProjection(
		// Projections.projectionList().add(Projections.count("instances"), "numInstances"))
		// .add(Expression.eq("publicVisible", true)).addOrder(Order.desc("numInstances"));
		// List<RealTopic> ll = getHibernateTemplate().findByCriteria(
		// SelectDAOHibernateImpl.loadEmAll(crit), 0, MAX_TOPICS);

		final String hql = "from RealTopic top " + " join fetch top.user "
				+ " join fetch top.instances ttc " + " join fetch ttc.topic as child "
				+ " where top.publicVisible = true and child.publicVisible=true "
				+ " order by size(top.instances) desc ";


		List<RealTopic> ll = (List<RealTopic>) getLimittedResults(hql, MAX_TOPICS);

		return ll;
	}

	/**
	 * 
	 */
	public List<WebLink> getTopWeblinks() {
		if (topWeblinks != null) {
			return topWeblinks;
		} else {
			return createTopWeblinks();
		}
	}


	/**
	 * Only run when necessary as this is a little slow
	 * 
	 * PEND why does this need to be a left join to return results?
	 * 
	 * @return
	 */
	private List<WebLink> createTopWeblinks() {

		log.info("Updating Top Weblinks");

		final String hql = "from WebLink occ " + " join fetch occ.user "
				+ " join fetch occ.topics toc " + " join fetch toc.topic as top "
				+ " where top.publicVisible = true and occ.publicVisible = true "
				+ " order by occ.created desc ";


		List<WebLink> ll = (List<WebLink>) getHibernateTemplate().find(hql);

		topWeblinks = new ArrayList<WebLink>(MAX_WEBLINKS);

		// Limit results to 1 per user
		Set<User> userIDs = new HashSet<User>();
		int i = 0;
		for (WebLink link : ll) {
			if (!userIDs.contains(link.getUser())) {
				topWeblinks.add(link);
				userIDs.add(link.getUser());
				i++;
			}
			if (i > MAX_WEBLINKS) {
				break;
			}
		}

		return topWeblinks;
	}


}
