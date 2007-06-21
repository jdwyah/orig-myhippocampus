package com.aavu.server.dao.hibernate;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Entry;
import com.aavu.client.domain.MindTreeOcc;
import com.aavu.client.domain.Occurrence;
import com.aavu.client.domain.OccurrenceWithLocation;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicTypeConnector;
import com.aavu.client.domain.mapper.MindTree;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.util.SetUtils;
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
	public void changeState(final Topic t,final boolean toIsland) {
		int res = (Integer) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				String newType = "Topic";
				if(toIsland){
					newType = "tag";
				}								
				String hqlUpdate = "update Topic set discriminator = :newType where topic_id = :id";				
				int updatedEntities = sess.createQuery( hqlUpdate )
				.setString("newType", newType)
				.setLong( "id", t.getId() )	                            
				.executeUpdate();				
				return updatedEntities;				
			}});
		log.debug("res: "+res+" Changed t:"+t.getId()+" Now an island "+toIsland);

		getHibernateTemplate().evict(t);


	}

	/**
	 * 
	 */
	public void delete(final Topic todelete) {

		log.info("Deleting: "+todelete +" "+todelete.getId());
		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {

				/*
				 * load-ing here prevents an error where todelete has unsaved occurences, which we then try to delete causing an "expected row 1"
				 * kind of error. If you want to delete todelete, there's no reason to save his associations first. 
				 * NOTE: we could just change this method to take the long topic_id instead of a real Topic.class
				 */				
				Topic topic  = (Topic) sess.get(Topic.class,todelete.getId());



//				log.info("Instances: "+topic.getInstances().size());

//				for (TopicTypeConnector conn : (Set<TopicTypeConnector>)topic.getInstances()) {
//				getHibernateTemplate().delete(conn);
//				}

				/*
				 * delete all type/instance references to this topic
				 */
				List<TopicTypeConnector> conns = sess.createCriteria(TopicTypeConnector.class, "conn")
				.add(Expression.or((Expression.eq("conn.type", topic)),
						Expression.eq("conn.topic", topic))).list();


				System.out.println("found "+conns.size()+" connections.");
				for (TopicTypeConnector connector : conns) {
					connector.getTopic().getTypes().remove(connector);					
					System.out.println("Delete connection "+connector+" "+connector.getId()+" "+connector.getTopic().getId()+" to "+connector.getType().getId());
					sess.delete(connector);
				}				
				topic.getTypes().clear();

				//System.out.println("after clear "+topic.toPrettyString());

//				for (TopicTypeConnector conn : (Set<TopicTypeConnector>)topic.getTypes()) {
//				log.info("Was a tag. Removing instances "+instance);
//				topic.getTypes().clear();					
//				}
				//topic.getInstances().clear();

//				log.info("Types: "+topic.getTypes().size());
//				for (Topic type : (Set<Topic>)topic.getTypes()) {
//				log.info("Removing from type "+type);
//				type.getInstances().remove(topic);
//				}
//				topic.getTypes().clear();



				for (OccurrenceWithLocation owl : (Set<OccurrenceWithLocation>)topic.getOccurences()) {
					
					Occurrence occurence = owl.getOccurrence();
					
					//TODO delete S3Files 
					//TODO delete Weblinks that were only referenced by us					

					log.debug("remove occurrence: "+occurence.getId()+" "+occurence.getTitle()+" "+occurence.getData());

					if(occurence instanceof Entry){
						sess.delete(occurence);
					}
					if(occurence instanceof MindTreeOcc){
						sess.delete(occurence);
					}
					
					
				}
				topic.getOccurences().clear();


				/*
				 * Associations. 
				 * First delete my associations.
				 * Remember if Topic A has a seeAlso to Topic B
				 * A has an association with B as a member & B.associations.size == 0 
				 * So if we delete B, we need to go finding references in A. 
				 */
				topic.getAssociations().clear();
				for (Association assoc : (Set<Association>)topic.getAssociations()) {					
					sess.delete(assoc);					
				}

				/*
				 * next delete any references to me in other's associations. 
				 *  
				 */
				List<Association> associationsWithThisMember = sess.createCriteria(Association.class, "assoc")
				.createAlias("assoc.members", "mem")
				.add(Expression.eq("mem.id", topic.getId())).list();


				log.info("Associations With This Member: "+associationsWithThisMember.size());
				for(Association assoc : associationsWithThisMember){
					log.debug("removing "+topic+" from "+assoc);
					assoc.getMembers().remove(topic);
				}


				System.out.println("last gasp "+topic.toPrettyString());

				sess.delete(topic);				
				return true;
			}});
	}


	public void deleteOccurrence(final Occurrence toDelete) {

		getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {

				Occurrence o  = (Occurrence) sess.get(Occurrence.class,toDelete.getId());

				log.debug("going to delete "+o+" "+o.getId()+" "+o.getData());

				Set<Topic> topics = o.getTopics();				
				for (Topic t : topics) {
					log.debug("remove "+t);

					boolean fnd = SetUtils.removeFromSetById(t.getOccurences(), o.getId());	

					if(!fnd){
						log.error("Problem removing occ "+o.getId()+" from "+t.getId());
					}

				}
				sess.delete(o);
				return null;
			}});

	}

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
		
		log.info("SAVE "+t.getTitle()+" "+t.getUser());

		//
		//Save the subject. If they've just added the subject it will be unsaved,
		//even if it's already in the DB, do a lookup.
		//
		Subject curSubj = t.getSubject();		
		if(curSubj != null && curSubj.getId() == 0){
			DetachedCriteria crit = DetachedCriteria.forClass(curSubj.getClass())
			.add(Expression.eq("foreignID", curSubj.getForeignID()));
			Subject saved = (Subject) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));
			if(saved == null){
				getHibernateTemplate().save(curSubj);
			}else{
				t.setSubject(saved);
			}
		}

		getHibernateTemplate().saveOrUpdate(t);		

		return t;		
	}
	public Long saveSimple(Topic t){
		return (Long) getHibernateTemplate().save(t);
	}

	public void saveTopicsLocation(long tagID, long topicID, int latitude, int longitude){

		log.debug("-------------SAVE TOPICS LOCATION---------------");
		log.debug("-------------tag "+tagID+" topic "+topicID+"---------------");	
		Topic t = (Topic) getHibernateTemplate().get(Topic.class, topicID);



		Set<TopicTypeConnector> types = t.getTypes();

		//System.out.println(t.toPrettyString());

		log.debug("Types size "+t.getTypesAsTopics().size());
		log.debug("TypesWith loc size "+types.size());

		for(TopicTypeConnector twl : types){

			//System.out.println("Found "+twl.getTopic().getTitle()+" lat "+twl.getLatitude()+" long "+twl.getLongitude());

			if(twl.getTopic().getId() == topicID){

				//System.out.println("updating "+topicID);

				twl.setLatitude(latitude);
				twl.setLongitude(longitude);

				getHibernateTemplate().save(twl);
				break;
			}
		}



//		List<Object[]> list = getHibernateTemplate().find(""+
//		"select title, id, lastUpdated, top.types.latitude, top.types.longitude from Topic top "+
//		"where top.types.id is ? "+
//		"and user is ? "
//		,params);

//		Topic t = (Topic) getHibernateTemplate().get(Topic.class, topicID);
//		t.getTypes().

	}

	
}
