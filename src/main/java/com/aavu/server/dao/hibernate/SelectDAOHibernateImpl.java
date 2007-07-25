package com.aavu.server.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Subqueries;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.HippoLocation;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaLocation;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.RealTopic;
import com.aavu.client.domain.Root;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.WebLink;
import com.aavu.client.domain.dto.DatedTopicIdentifier;
import com.aavu.client.domain.dto.LocationDTO;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TimeLineObj;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.web.domain.UserPageBean;

/**
 * 
 * @author Jeff Dwyer
 * 
 */
public class SelectDAOHibernateImpl extends HibernateDaoSupport implements SelectDAO {
	private static final int DEFAULT_AUTOCOMPLET_MAX = 7;
	private static final int DEFAULT_TAG_AUTOCOMPLETE_MAX = 7;
	private static final Logger log = Logger.getLogger(SelectDAOHibernateImpl.class);

	/**
	 * This is what will be fetched when you ask for a topic. If you follow the object graph beyond
	 * these bounds you'll be in lazy territory.
	 * 
	 * @param crit
	 * @return
	 */
	public static DetachedCriteria loadEmAll(DetachedCriteria crit) {
		return crit
				.setFetchMode("user", FetchMode.JOIN)
				// .setFetchMode("instances.topic", FetchMode.JOIN)
				.setFetchMode("subject", FetchMode.JOIN).setFetchMode("types", FetchMode.JOIN)
				.setFetchMode("types.type", FetchMode.JOIN).setFetchMode("types.type.associations",
						FetchMode.JOIN).setFetchMode("types.type.associations.types",
						FetchMode.JOIN).setFetchMode("types.type.associations.members",
						FetchMode.JOIN).setFetchMode("instances", FetchMode.JOIN).setFetchMode(
						"instances.topic", FetchMode.JOIN).setFetchMode("occurences",
						FetchMode.JOIN).setFetchMode("occurences.occurrence", FetchMode.JOIN)
				.setFetchMode("occurences.occurrence.topics", FetchMode.JOIN).setFetchMode(
						"associations", FetchMode.JOIN).setFetchMode("associations.members",
						FetchMode.JOIN).setFetchMode("associations.types", FetchMode.JOIN);
	}



	public Topic get(long topicID) {
		return (Topic) getHibernateTemplate().get(Topic.class, topicID);
	}


	public List<Meta> getAllMetas(User user) {
		List<Meta> ll2 = getHibernateTemplate().find("from Meta meta " + "where meta.user = ?",
				user);
		return ll2;
	}


	private List<DatedTopicIdentifier> getAllTopicIdentifiers(DetachedCriteria crit, int start,
			int max) {

		List<Object[]> list = getHibernateTemplate().findByCriteria(crit, start, max);

		List<DatedTopicIdentifier> rtn = new ArrayList<DatedTopicIdentifier>(list.size());

		// TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list) {
			// rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));
			rtn.add(new DatedTopicIdentifier((Long) o[1], (String) o[0], (Date) o[2], (Date) o[3]));
		}

		return rtn;
	}

	/**
	 * 
	 */
	public List<DatedTopicIdentifier> getAllTopicIdentifiers(User user, int start, int max,
			String startStr) {
		return getAllTopicIdentifiers(user, start, max, startStr, false, false);
	}

	/**
	 * all param is used by some unit tests to help wipe a user's account.
	 * 
	 */
	public List<DatedTopicIdentifier> getAllTopicIdentifiers(User user, boolean all) {
		return getAllTopicIdentifiers(user, 0, 9999, null, all, false);
	}

	public List<DatedTopicIdentifier> getAllPublicTopicIdentifiers(User user, int start, int max,
			String startStr) {
		return getAllTopicIdentifiers(user, start, max, startStr, false, true);
	}

	/**
	 * TODO replace hardcoded class discriminators with .class.
	 * 
	 * @param user
	 * @param start
	 * @param max
	 * @param startStr
	 * @param all
	 * @return
	 */
	private List<DatedTopicIdentifier> getAllTopicIdentifiers(User user, int start, int max,
			String startStr, boolean all, boolean publicOnly) {

		DetachedCriteria crit;
		if (all) {
			crit = DetachedCriteria.forClass(Topic.class);
		} else {
			crit = DetachedCriteria.forClass(RealTopic.class);
		}

		crit.add(Expression.eq("user", user));



		if (startStr != null) {
			crit.add(Expression.ilike("title", startStr, MatchMode.START));
		}
		crit.addOrder(Order.desc("lastUpdated"))

		.setProjection(getTopicIdentifier());


		return getAllTopicIdentifiers(crit, start, max);

	}

	/**
	 * TESTING ONLY Should not be exposed to Service layer.
	 * 
	 * for when you really want the whole DB
	 * 
	 */
	public List<Topic> getAllTopics(User u) {
		DetachedCriteria crit = loadEmAll(DetachedCriteria.forClass(Topic.class).add(
				Expression.eq("user", u)));
		return getHibernateTemplate().findByCriteria(
				crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY));
	}


	public Topic getForID(User currentUser, long topicID) {

		DetachedCriteria crit = loadEmAll(DetachedCriteria.forClass(Topic.class)
		// .add(Expression.eq("user", currentUser))
				.add(Expression.eq("id", topicID)));

		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));
	}

	public Topic getForName(User currentUser, String string) {

		log.debug("user " + currentUser.getUsername() + " string " + string);

		DetachedCriteria crit = loadEmAll(DetachedCriteria.forClass(RealTopic.class).add(
				Expression.eq("user", currentUser)).add(Expression.eq("title", string)));


		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));

		// return getHibernateTemplate().findByNamedParam("from Topic where user = :user and title =
		// :title", "user", user);
	}

	public Topic getPublicForName(String username, String string) {

		log.debug("Public user " + username + " string " + string);

		DetachedCriteria crit = loadEmAll(DetachedCriteria.forClass(RealTopic.class).createAlias(
				"user", "u").add(Expression.eq("u.username", username)).add(
				Expression.eq("publicVisible", true)).add(Expression.eq("title", string)));

		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));

		// return getHibernateTemplate().findByNamedParam("from Topic where user = :user and title =
		// :title", "user", user);
	}

	public List<TopicIdentifier> getLinksTo(Topic topic, User user) {
		Object[] params = { topic.getId(), user };
		log.debug("----------getLinksTo-----------");
		log.debug("------------" + topic + "-------");

		/*
		 * Get Associations that mention this Topic
		 */
		List<Object[]> associationsToThis = getHibernateTemplate()
				.find("" + "select title, id from Topic top " +
				// "join top.associations "+
						"where ? in elements(top.associations.members) " + "and user is ? ", params);


		// associationsToThis.addAll(instancesOfThisTopicSlashTag);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(associationsToThis.size());
		for (Object[] o : associationsToThis) {
			rtn.add(new TopicIdentifier((Long) o[1], (String) o[0]));
		}
		return rtn;
	}

	/**
	 * Pretty much the same code as getTimelines. Could refactor out common functionality.
	 * 
	 */
	public List<LocationDTO> getLocations(long tagID, User user) {
		List<LocationDTO> rtn = new ArrayList<LocationDTO>();

		// "where typeConn.type.class = MetaDate

		// NOTE, aliases are INNER_JOINS so don't accidentally limt ourselves to only topics w/ tags
		DetachedCriteria crit = DetachedCriteria.forClass(Topic.class).add(
				Expression.eq("user", user)).createAlias("associations", "assoc").createAlias(
				"assoc.members", "metaValue").createAlias("assoc.types", "assocTypeConn")
				.createAlias("assocTypeConn.type", "assocType").add(
						Expression.eq("assocType.class", "metalocation"));

		if (tagID != -1) {
			DetachedCriteria tags = DetachedCriteria.forClass(TopicTypeConnector.class).add(
					Expression.eq("type.id", tagID)).setProjection(Property.forName("topic.id"));

			log.debug("Using tag subquery");
			crit.add(Subqueries.propertyIn("id", tags));
		}

		crit.setProjection(Projections.projectionList().add(Property.forName("id")).add(
				Property.forName("title")).add(Property.forName("metaValue.id")).add(
				Property.forName("metaValue.title")).add(Property.forName("metaValue.latitude"))
				.add(Property.forName("metaValue.longitude")).add(Property.forName("assocType.id"))
				.add(Property.forName("assocType.title")));

		List<Object[]> ll = getHibernateTemplate().findByCriteria(crit);

		for (Object result : ll) {
			Object[] oa = (Object[]) result;

			// if(log.isDebugEnabled()){
			// for (int i = 0; i < oa.length; i++) {
			// Object object = oa[i];
			// if(object != null){
			// log.debug(" "+i+" "+object+" "+object.getClass());
			// }else{
			// log.debug(" "+i+" "+object+" ");
			// }
			// }
			// }

			TopicIdentifier topic = new TopicIdentifier((Long) oa[0], (String) oa[1]);

			HippoLocation location = new HippoLocation();
			location.setId((Long) oa[2]);
			location.setTitle((String) oa[3]);
			location.setLatitude((Integer) oa[4]);
			location.setLongitude((Integer) oa[5]);

			MetaLocation meta = new MetaLocation();
			meta.setId((Long) oa[6]);
			meta.setTitle((String) oa[7]);

			LocationDTO locDTO = new LocationDTO(topic, location, meta);

			rtn.add(locDTO);
		}

		return rtn;
	}

	public List<LocationDTO> getLocations(User user) {
		return getLocations(-1, user);
	}


	public Occurrence getOccurrrence(long id) {
		DetachedCriteria crit = loadEmAll(DetachedCriteria.forClass(Occurrence.class).add(
				Expression.eq("id", id)));

		return (Occurrence) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByCriteria(crit));
	}

	/**
	 * 
	 * 
	 * @param currentUser
	 * @return
	 */
	private DetachedCriteria getSecurityCriteriaForUser(User currentUser) {
		return DetachedCriteria.forClass(Topic.class).createAlias("user", "u").add(
				Expression.or(Expression.eq("u.username", currentUser), Expression.eq(
						"publicVisible", true)));

	}


	public MetaSeeAlso getSeeAlsoSingleton() {
		return (MetaSeeAlso) DataAccessUtils.uniqueResult(getHibernateTemplate().find(
				"from MetaSeeAlso"));
	}


	/**
	 * 
	 * If you'd like the timeline for multiple tags, use the helper method in TopicService, which
	 * will just call this many times. I'm not sure if it's possible to do better than that, since
	 * topics may have many tags and I haven't found a great way to query whether something like
	 * topic.hasAnyOfItsTagsIn(Set tags) without iterating.
	 * 
	 * 
	 */
	public List<TimeLineObj> getTimeline(long tagID, User user) {

		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();

		// "where typeConn.type.class = MetaDate

		// NOTE, aliases are INNER_JOINS so don't accidentally limt ourselves to only topics w/ tags
		DetachedCriteria crit = DetachedCriteria.forClass(Topic.class).add(
				Expression.eq("user", user)).createAlias("associations", "assoc").createAlias(
				"assoc.members", "metaValue").createAlias("assoc.types", "assocTypeConn")
				.createAlias("assocTypeConn.type", "assocType").add(
						Expression.eq("assocType.class", "metadate"));

		if (tagID != -1) {
			DetachedCriteria tags = DetachedCriteria.forClass(TopicTypeConnector.class).add(
					Expression.eq("type.id", tagID)).setProjection(Property.forName("topic.id"));

			log.debug("Using tag subquery");
			crit.add(Subqueries.propertyIn("id", tags));
		}


		// This code leads to a SQL issue. See
		// http://forum.hibernate.org/viewtopic.php?t=941669
		//
		// crit.setProjection(Projections.projectionList()
		// .add(Property.forName("id"))
		// .add(Property.forName("title"))
		// .add(Property.forName("metaValue.created"))
		// .add(Projections.distinct(Property.forName("metaValue.id"))));

		crit.setProjection(Projections.distinct(Projections.projectionList().add(
				Property.forName("id")).add(Property.forName("title")).add(
				Property.forName("metaValue.created")).add(Property.forName("metaValue.id"))));


		List<Object[]> ll = getHibernateTemplate().findByCriteria(crit);

		for (Object topic : ll) {
			Object[] oa = (Object[]) topic;

			if (log.isDebugEnabled()) {
				for (int i = 0; i < oa.length; i++) {
					Object object = oa[i];
					if (object != null) {
						log.debug(" " + i + " " + object + " " + object.getClass());
					} else {
						log.debug(" " + i + " " + object + " ");
					}
				}
			}
			// ?BigInteger topic_id = (BigInteger) oa[0];
			Long topicId = (Long) oa[0];

			Date date = (Date) oa[2];

			// add metaDate
			rtn.add(new TimeLineObj(new TopicIdentifier(topicId.longValue(), (String) oa[1]), date,
					null));
		}

		return rtn;
	}

	/**
	 * 
	 * 
	 */
	public List<TimeLineObj> getTimeline(User user) {
		return getTimeline(-1, user);
	}

	public int getTopicCount(User user) {
		DetachedCriteria crit = DetachedCriteria.forClass(RealTopic.class).add(
				Expression.eq("user", user)).setProjection(Projections.rowCount());
		return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(crit));
	}

	/**
	 * 
	 */
	public List<TopicIdentifier> getTopicForOccurrence(long id, User currentUser) {

		Object[] pm = { new Long(id), currentUser };

		List<TopicOccurrenceConnector> conns = getHibernateTemplate().find(
				"from TopicOccurrenceConnector conn "
						+ "where conn.occurrence.id = ? and conn.topic.user = ?", pm);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(conns.size());

		for (TopicOccurrenceConnector conn : conns) {
			rtn.add(new DatedTopicIdentifier(conn.getTopic().getId(), conn.getTopic().getTitle(),
					conn.getTopic().getCreated(), conn.getTopic().getLastUpdated()));
		}
		// TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//


		return rtn;

	}

	/**
	 * Utility to set the projection properties for TopicIdentifier.
	 * 
	 * @return
	 */
	private Projection getTopicIdentifier() {
		return Projections.projectionList().add(Property.forName("title")).add(
				Property.forName("id")).add(Property.forName("created")).add(
				Property.forName("lastUpdated"));
	}

	public List<TopicTypeConnector> getTopicIdsWithTag(long tagid, User user) {

		List<TopicTypeConnector> rtn = getHibernateTemplate().find(
				"from TopicTypeConnector conn "
						+ "where conn.type.id = ? order by conn.topic.lastUpdated DESC",
				new Long(tagid));

		// if(log.isDebugEnabled()){
		// for (TopicTypeConnector connector : rtn) {
		// log.debug("DAO says "+connector.getId()+" "+connector.getLongitude()+"
		// "+connector.getLatitude());
		// }
		// }

		return rtn;
	}

	/**
	 * No user, so only return public topics
	 */
	public List<TopicTypeConnector> getTopicIdsWithTag(long tagid) {

		List<TopicTypeConnector> rtn = getHibernateTemplate()
				.find(
						"from TopicTypeConnector conn "
								+ "where conn.type.id = ? and conn.topic.publicVisible = true order by conn.topic.lastUpdated DESC",
						new Long(tagid));

		return rtn;
	}

	/*
	 * TODO push this responsibility over to Compass, it should get better performance.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.aavu.server.dao.TopicDAO#getTopicsStarting(com.aavu.client.domain.User,
	 *      java.lang.String)
	 */
	public List<TopicIdentifier> getTopicsStarting(User user, String match) {
		return getTopicsStarting(user, match, DEFAULT_AUTOCOMPLET_MAX);
	}

	public List<TopicIdentifier> getTopicsStarting(User user, String match, int max) {
		DetachedCriteria crit = DetachedCriteria.forClass(RealTopic.class).add(
				Expression.eq("user", user)).add(
				Expression.ilike("title", match, MatchMode.ANYWHERE)).addOrder(Order.asc("title"))
				.setProjection(
						Projections.projectionList().add(Property.forName("title")).add(
								Property.forName("id")));

		List<Object[]> list = getHibernateTemplate().findByCriteria(crit, 0, max);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		// TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list) {
			rtn.add(new TopicIdentifier((Long) o[1], (String) o[0]));
		}
		return rtn;
	}

	/**
	 * Note don't sysout fullocc since it's User is not loaded and that LazyLEx's.
	 * 
	 */
	public MindTree getTree(MindTreeOcc occ) {

		DetachedCriteria crit = DetachedCriteria.forClass(MindTreeOcc.class).add(
				Expression.eq("id", occ.getId())).setFetchMode("MindTree", FetchMode.JOIN)
				.setFetchMode("MindTree.leftSide", FetchMode.JOIN).setFetchMode(
						"MindTree.rightSide", FetchMode.JOIN);

		MindTreeOcc fullocc = (MindTreeOcc) DataAccessUtils.uniqueResult(getHibernateTemplate()
				.findByCriteria(crit));
		return fullocc.getMindTree();
	}

	/**
	 * 
	 * 
	 */
	public UserPageBean getUsageStats(final User user) {

		UserPageBean rtn = new UserPageBean(user);

		rtn.setNumberOfTopics(getTopicCount(user));

		rtn.setNumberOfIslands(DataAccessUtils.intResult(getHibernateTemplate().find(
				"" + "select count(id) from Tag tag " + "where user is ? ", rtn.getUser())));
		rtn.setNumberOfLinks(DataAccessUtils.intResult(getHibernateTemplate().find(
				"" + "select count(id) from WebLink link " + "where user is ? ", rtn.getUser())));
		return rtn;
	}

	public WebLink getWebLinkForURI(String url, User user) {
		log.debug("user " + user.getUsername() + " url " + url);

		DetachedCriteria crit = loadEmAll(
				DetachedCriteria.forClass(WebLink.class).add(Expression.eq("user", user)).add(
						Expression.eq("uri", url))).setFetchMode("topics", FetchMode.JOIN);

		List<WebLink> res = getHibernateTemplate().findByCriteria(crit);

		try {
			return (WebLink) DataAccessUtils.uniqueResult(res);
		} catch (IncorrectResultSizeDataAccessException e) {
			log.warn(e.getMessage() + " URL " + url);
			return res.get(0);
		}

	}

	public List<TopicTypeConnector> getRootTopics(User forUser, User currentUser) {
		return getTopicIdsWithTag(getRoot(forUser, currentUser).getId(), forUser);
	}

	public void tester() {
		// TODO Auto-generated method stub

	}

	public List<TagStat> getTagStats(User user) {

		List<Object[]> list = getHibernateTemplate()
				.find(
						""
								+ "select tag.id, tag.instances.size, tag.title, tag.latitude, tag.longitude from Tag tag "
								+ "where  user is ? order by tag.title", user);

		// This is the query if we decide to get rid of the instances mapping again.
		//
		// List<Object[]> list = getHibernateTemplate().find(""+
		// "select conn.type.id, count(conn.type), conn.type.title, conn.type.latitude,
		// conn.type.longitude from TopicTypeConnector conn "+
		// //"left join topic "+
		// "where conn.topic.user is ? and conn.type.class = Tag "+
		// "group by conn.type"
		// ,user);



		// List<Object[]> subjectList = getHibernateTemplate().find(""+
		// "select top.subject.class.id, top.subject.class, count(top.subject.class) from Topic top
		// "+
		// "where top.user is ? "+
		// "group by top.subject.class"
		// ,user);

		log.debug("tagstats size " + list.size());
		// log.debug("subject list: "+subjectList.size());

		// List<TagStat> rtn = new ArrayList<TagStat>(subjectList.size() + list.size());
		List<TagStat> rtn = new ArrayList<TagStat>(list.size());

		// for (Object[] o : subjectList){
		// if(log.isDebugEnabled()){
		// log.debug("SUBJECT "+o[0]+" "+o[1]+" "+o[2]);
		// }
		// rtn.add(new TagStat((Long)o[0],(String)o[1],(Integer)o[2]));
		// }


		// TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list) {
			// if(log.isDebugEnabled()){
			// log.debug("TagStat "+o[0]+" "+o[1]+" "+o[2]+" "+o[3]+" "+o[4]);
			// //log.debug("TagStat "+o[0].getClass()+" "+o[1].getClass()+" "+o[2].getClass()+"
			// "+o[3].getClass()+" "+o[4].getClass());
			// }

			rtn.add(new TagStat((Long) o[0], (Integer) o[1], (String) o[2], (Integer) o[3],
					(Integer) o[4]));
		}

		return rtn;
	}


	public List<TopicIdentifier> getTagsStarting(User user, String match) {
		return getTagsStarting(user, match, DEFAULT_TAG_AUTOCOMPLETE_MAX);
	}

	public List<TopicIdentifier> getTagsStarting(User user, String match, int max) {
		DetachedCriteria crit = DetachedCriteria.forClass(Topic.class).add(
				Expression.and(Expression.ilike("title", match, MatchMode.START), Expression.eq(
						"user", user))).setProjection(
				Projections.projectionList().add(Property.forName("title")).add(
						Property.forName("id")));
		log.debug("USER: " + user + " USER ID " + user.getId() + " NAME " + user.getUsername()
				+ " MATCH|" + match + "|");

		List<Object[]> list = getHibernateTemplate().findByCriteria(crit, 0, max);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		// TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list) {
			rtn.add(new TopicIdentifier((Long) o[1], (String) o[0]));
		}
		return rtn;
	}


	public Root getRoot(User user, User currentUser) {
		DetachedCriteria crit = DetachedCriteria.forClass(Root.class).add(
				Expression.eq("user", user));
		Root userRoot = (Root) DataAccessUtils.requiredSingleResult(getHibernateTemplate()
				.findByCriteria(crit));
		return userRoot;
	}



}
