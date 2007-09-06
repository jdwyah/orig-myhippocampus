package com.aavu.server.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicOccurrenceConnector;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.User;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.dao.EditDAO;

public class EditDAOHibernateImpl extends HibernateDaoSupport implements EditDAO {

	private static final Logger log = Logger.getLogger(EditDAOHibernateImpl.class);


	/**
	 * PEND MED a bit fragile. Note the capitalization. Is there another way to do this?
	 * 
	 * @param t
	 * @param toIsland
	 */
	public void changeState(final Topic t, final boolean toIsland) {
		int res = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				String newType = "Topic";
				if (toIsland) {
					newType = "tag";
				}
				String hqlUpdate = "update Topic set discriminator = :newType where topic_id = :id";
				int updatedEntities = sess.createQuery(hqlUpdate).setString("newType", newType)
						.setLong("id", t.getId()).executeUpdate();
				return updatedEntities;
			}
		});
		log.debug("res: " + res + " Changed t:" + t.getId() + " Now an island " + toIsland);

		getHibernateTemplate().evict(t);


	}


	public List<Topic> getDeleteList(long topicID) {
		List<Topic> rtn = new ArrayList<Topic>();

		Topic toDelete = (Topic) getHibernateTemplate().get(Topic.class, topicID);

		recurseDelete(rtn, toDelete);

		return rtn;
	}

	private void recurseDelete(List<Topic> toDelete, Topic toCheck) {
		// prevent infinite loop
		if (toDelete.contains(toCheck)) {
			return;
		}
		// don't delete root or allow a root child with a child of root to end up looping back on
		// itself
		if (!toCheck.isDeletable()) {
			return;
		}

		for (Iterator iterator = toCheck.getInstances().iterator(); iterator.hasNext();) {
			TopicTypeConnector toc = (TopicTypeConnector) iterator.next();
			Topic child = toc.getTopic();

			if (child.getTypes().size() < 2) {
				recurseDelete(toDelete, child);
			}
		}
		for (Iterator iterator = toCheck.getOccurenceObjs().iterator(); iterator.hasNext();) {
			Occurrence occ = (Occurrence) iterator.next();

			if (occ.getTopics().size() < 2) {
				recurseDelete(toDelete, occ);
			}
		}

		toDelete.add(toCheck);
	}

	/**
	 * recursively delete a topic. Use getDeleteList if you want to know what's going to be deleted.
	 */
	public void delete(final Topic todelete) {

		List<Topic> toDeleteList = getDeleteList(todelete.getId());
		log.info(" " + todelete + " recurse size " + toDeleteList.size());
		for (Topic recursiveDelete : toDeleteList) {
			log.info("recurse: " + recursiveDelete + " " + recursiveDelete.getClass());
			delete(recursiveDelete.getId());
		}
	}


	/**
	 * Do not call delete() unless you know what you're doing, which is just to say, make sure to
	 * use getDeleteList first, so that you don't leave danglers. This is done by the public
	 * delete(Topic) method
	 * 
	 * 
	 */
	private void delete(final long topicID) {

		log.info("Deleting: " + topicID);
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {

				/*
				 * load-ing here prevents an error where todelete has unsaved occurences, which we
				 * then try to delete causing an "expected row 1" kind of error. If you want to
				 * delete todelete, there's no reason to save his associations first. NOTE: we could
				 * just change this method to take the long topic_id instead of a real Topic.class
				 */
				Topic topic = (Topic) sess.get(Topic.class, topicID);



				// log.info("Instances: "+topic.getInstances().size());

				// for (TopicTypeConnector conn : (Set<TopicTypeConnector>)topic.getInstances()) {
				// getHibernateTemplate().delete(conn);
				// }

				/*
				 * delete all type/instance references to this topic
				 */
				List<TopicTypeConnector> conns = sess.createCriteria(TopicTypeConnector.class,
						"conn").add(
						Expression.or((Expression.eq("conn.type", topic)), Expression.eq(
								"conn.topic", topic))).list();


				System.out.println("found " + conns.size() + " connections.");
				for (TopicTypeConnector connector : conns) {
					connector.getTopic().getTypes().remove(connector);
					System.out.println("Delete connection " + connector + " " + connector.getId()
							+ " " + connector.getTopic().getId() + " to "
							+ connector.getType().getId());
					sess.delete(connector);
				}
				topic.getTypes().clear();

				// System.out.println("after clear "+topic.toPrettyString());

				// for (TopicTypeConnector conn : (Set<TopicTypeConnector>)topic.getTypes()) {
				// log.info("Was a tag. Removing instances "+instance);
				// topic.getTypes().clear();
				// }
				// topic.getInstances().clear();

				// log.info("Types: "+topic.getTypes().size());
				// for (Topic type : (Set<Topic>)topic.getTypes()) {
				// log.info("Removing from type "+type);
				// type.getInstances().remove(topic);
				// }
				// topic.getTypes().clear();


				/**
				 * this will leave danglers, but we're taking care of that with the getDeleteList().
				 * Do not call delete() unless you know what you're doing
				 * 
				 * TODO delete S3Files from amazon
				 */
				for (TopicOccurrenceConnector owl : (Set<TopicOccurrenceConnector>) topic
						.getOccurences()) {

					// other side of delete
					owl.getOccurrence().getTopics().remove(owl);
					sess.delete(owl);
				}
				topic.getOccurences().clear();


				/*
				 * Associations. First delete my associations. Remember if Topic A has a seeAlso to
				 * Topic B A has an association with B as a member & B.associations.size == 0 So if
				 * we delete B, we need to go finding references in A.
				 */
				topic.getAssociations().clear();
				for (Association assoc : (Set<Association>) topic.getAssociations()) {
					sess.delete(assoc);
				}

				/*
				 * next delete any references to me in other's associations.
				 * 
				 */
				List<Association> associationsWithThisMember = sess.createCriteria(
						Association.class, "assoc").createAlias("assoc.members", "mem").add(
						Expression.eq("mem.id", topic.getId())).list();


				log.info("Associations With This Member: " + associationsWithThisMember.size());
				for (Association assoc : associationsWithThisMember) {
					log.debug("removing " + topic + " from " + assoc);
					assoc.getMembers().remove(topic);
				}


				if (topic instanceof Occurrence) {
					log.debug("Is an occ");
					Occurrence topicAsOcc = (Occurrence) topic;
					Set<TopicOccurrenceConnector> topics = topicAsOcc.getTopics();
					for (TopicOccurrenceConnector toccConnector : topics) {
						log.debug("Remove toc " + toccConnector);

						// System.out.println("s1 " +
						// toccConnector.getTopic().getOccurences().size());
						// System.out
						// .println("s2 " + toccConnector.getOccurrence().getTopics().size());
						//
						// System.out.println("i1 "
						// + toccConnector.getTopic().getOccurences().iterator().next());
						// System.out.println("i2 "
						// + toccConnector.getOccurrence().getTopics().iterator().next());
						//
						//
						// boolean b1 = CollectionUtils.removeFromCollectionById(toccConnector
						// .getTopic().getOccurences(), toccConnector.getId());
						// boolean b2 = CollectionUtils.removeFromCollectionById(toccConnector
						// .getOccurrence().getTopics(), toccConnector.getId());

						// System.out.println("b1 " + b1);
						// System.out.println("b2 " + b2);

						System.out.println("rem1: "
								+ toccConnector.getTopic().getOccurences().remove(toccConnector));
						// System.out.println("rem2: "
						// + toccConnector.getOccurrence().getTopics().remove(toccConnector));
						sess.delete(toccConnector);
					}
					topicAsOcc.getTopics().clear();
				}



				System.out.println("last gasp " + topic.toPrettyString());

				sess.delete(topic);
				return true;
			}
		});
	}


	// public void deleteOccurrence(final Occurrence toDelete) {
	//
	// getHibernateTemplate().execute(new HibernateCallback() {
	// public Object doInHibernate(Session sess) throws HibernateException, SQLException {
	//
	// Occurrence o = (Occurrence) sess.get(Occurrence.class, toDelete.getId());
	//
	// log.debug("going to delete " + o + " " + o.getId() + " " + o.getData());
	//
	// Set<TopicOccurrenceConnector> topics = o.getTopics();
	// for (TopicOccurrenceConnector toccConnector : topics) {
	// log.debug("remove " + toccConnector);
	//
	// toccConnector.getTopic().getOccurences().remove(toccConnector);
	// sess.delete(toccConnector);
	//
	// }
	// sess.delete(o);
	// return null;
	// }
	// });
	//
	// }

	public void evict(Serializable obj) {
		getHibernateTemplate().evict(obj);
	}



	public MindTree save(MindTree tree) {
		getHibernateTemplate().saveOrUpdate(tree);
		return tree;
	}

	public Occurrence save(Occurrence link) {
		getHibernateTemplate().saveOrUpdate(link);
		return link;
	}


	public Topic save(Topic t) throws HippoBusinessException {

		log.debug("SAVE " + t.getTitle() + " User:" + t.getUser());

		//
		// Save the subject. If they've just added the subject it will be unsaved,
		// even if it's already in the DB, do a lookup.
		//
		Subject curSubj = t.getSubject();
		if (curSubj != null && curSubj.getId() == 0) {
			DetachedCriteria crit = DetachedCriteria.forClass(curSubj.getClass()).add(
					Expression.eq("foreignID", curSubj.getForeignID()));
			Subject saved = (Subject) DataAccessUtils.uniqueResult(getHibernateTemplate()
					.findByCriteria(crit));
			if (saved == null) {
				getHibernateTemplate().save(curSubj);
			} else {
				t.setSubject(saved);
			}
		}

		// return (Topic) getHibernateTemplate().merge(t);
		// getHibernateTemplate().merge(t);

		// getHibernateTemplate().replicate(t, replicationMode)
		getHibernateTemplate().saveOrUpdate(t);

		return t;
	}

	public Long saveSimple(Topic t) {
		return (Long) getHibernateTemplate().save(t);
	}

	public void saveTopicsLocation(long tagID, long topicID, int latitude, int longitude,
			User currentUser) throws HippoBusinessException {

		log.debug("-------------tag " + tagID + " topic " + topicID + "---------------");

		Object[] pms = { new Long(tagID), new Long(topicID), currentUser };

		try {
			TopicTypeConnector ttc = (TopicTypeConnector) DataAccessUtils
					.requiredUniqueResult(getHibernateTemplate()
							.find(
									"from TopicTypeConnector ttc where ttc.type.id = ? and ttc.topic.id = ? and ttc.topic.user = ?",
									pms));

			ttc.setLatitude(latitude);
			ttc.setLongitude(longitude);
			getHibernateTemplate().save(ttc);

		} catch (IncorrectResultSizeDataAccessException e) {
			throw new HippoBusinessException("Error saving Topic Location. Connections: "
					+ e.getActualSize());
		}


	}


	public void saveOccurrenceLocation(long topicID, long occurrenceID, int latitude,
			int longitude, User currentUser) throws HippoBusinessException {

		Object[] pms = { new Long(occurrenceID), new Long(topicID), currentUser };

		try {
			TopicOccurrenceConnector toc = (TopicOccurrenceConnector) DataAccessUtils
					.requiredUniqueResult(getHibernateTemplate()
							.find(
									"from TopicOccurrenceConnector toc where toc.occurrence.id = ? and toc.topic.id = ? and toc.topic.user = ?",
									pms));

			toc.setLatitude(latitude);
			toc.setLongitude(longitude);
			getHibernateTemplate().save(toc);

		} catch (IncorrectResultSizeDataAccessException e) {
			throw new HippoBusinessException("Missing Connection to save Occurrence Location "
					+ topicID + " " + occurrenceID);
		}


	}


	public Topic merge(Topic t) {
		return (Topic) getHibernateTemplate().merge(t);
	}


}
