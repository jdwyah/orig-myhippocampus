package com.aavu.server.dao.hibernate;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaDate;
import com.aavu.client.domain.MetaSeeAlso;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TopicDAO;

public class TopicDAOHibernateImpl extends HibernateDaoSupport implements TopicDAO{
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImpl.class);

	public static DetachedCriteria loadEmAll(DetachedCriteria crit){
		return crit.setFetchMode("user", FetchMode.JOIN)		
		.setFetchMode("instances", FetchMode.JOIN)
		.setFetchMode("types", FetchMode.JOIN)
		.setFetchMode("types.associations", FetchMode.JOIN)
		.setFetchMode("types.associations.types", FetchMode.JOIN)
		.setFetchMode("types.associations.members", FetchMode.JOIN)
		.setFetchMode("occurences", FetchMode.JOIN)
		.setFetchMode("associations", FetchMode.JOIN)
		.setFetchMode("associations.members", FetchMode.JOIN)	
		.setFetchMode("associations.types", FetchMode.JOIN);
	}
	
	/**
	 * remember, MetaDate objects only exist for one Tag ie a Book's ReadDate is not a 
	 * play's ReadDate.  We search on Tag, because we could be timeline-ing a Tag:
	 * RomanceBook extends Book, with Book's ReadDate
	 * 
	 */
	public List<TimeLineObj> getTimeline(User user) {
		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();
		
//		List<Object> ll= null;
//		(List<Object>) getHibernateTemplate().execute(new HibernateCallback(){
//			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
//				
//				return sess.createSQLQuery("select top.topic_id, top.title, metavalue.data from topic_meta_values "+
//						"join topics meta on topic_meta_values.topic_id = meta.topic_id "+
//						"join topics top on topic_meta_values.topic_meta_value_id = top.topic_id "+
//						"join topics metavalue on topic_meta_values.metaValue = metavalue.topic_id "+
//						"where meta.discriminator = 'metadate'")				
//			    .list();
//				
//			}});
		
		List<Object[]> ll = getHibernateTemplate().find("select top.id, top.title, metaValue.data from Topic top "+
				"join top.associations  ass "+
				"join ass.types  type "+
				"join ass.members metaValue "+
				 "where type.class = MetaDate");
		
		
		for (Object topic : ll) {
			Object[] oa = (Object[]) topic;
			for (int i = 0; i < oa.length; i++) {
				Object object = oa[i];
				System.out.println(" "+i+" "+object+" "+object.getClass());
			}
			BigInteger topic_id = (BigInteger) oa[0];
			String dateStr = (String) oa[2];
			Date date = new Date(Long.parseLong(dateStr));			
			
			rtn.add(new TimeLineObj(new TopicIdentifier(topic_id.longValue(),(String)oa[1]),date,null));						
		}
		if(log.isDebugEnabled())
			for (TimeLineObj obj : rtn) {
				log.debug("TIMELINE ");
				log.debug(obj);
			}
		
		return rtn;	
	}

	public Topic getForName(User user, String string) {

		log.debug("user "+user.getUsername()+" string "+string);
		
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.add(Expression.eq("title", string)));
		
		
		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));

		//return getHibernateTemplate().findByNamedParam("from Topic where user = :user and title = :title", "user", user);
	}

	public List<String> getTopicsStarting(User user, String match) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)		
		.add(Expression.eq("user", user))
		.add(Expression.ilike("title", match, MatchMode.ANYWHERE))
		.addOrder( Order.asc("title") )
		.setProjection(Property.forName("title"));	
		
		return getHibernateTemplate().findByCriteria(crit);
	}

	
	
	public Topic save(Topic t) {
		System.out.println("SAVE "+t.getTitle());

		
		
		
		//
		//Bit of a chicken & egg thing here with TransientReferences..
		//
		
		

//		System.out.println("METAS "+t.getMetas().size());
//		for (Iterator iter = t.getMetas().iterator(); iter.hasNext();) {
//			Meta meta = (Meta) iter.next();
//			log.debug("saving its metas ? "+meta.getTitle());		
//			if(meta.getId() == 0){			
//				log.debug("was unsaved. save"+meta.getTitle());
//				getHibernateTemplate().saveOrUpdate(meta);
//			}
//			log.debug("done");
//		}
//		
//		System.out.println("METAValues "+t.getMetaValues().entrySet().size());
//		for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
//			Topic metaValue = (Topic) t.getMetaValues().get(iter.next());
//			log.debug("saving its metavalue element ? "+metaValue.getTitle());		
//			if(metaValue.getId() == 0){			
//				log.debug("was unsaved. save"+metaValue.getTitle());
//				getHibernateTemplate().saveOrUpdate(metaValue);
//			}
//			log.debug("done");
//		}
		
		System.out.println("now set Associations : ");
		for (Iterator iter = t.getAssociations().iterator(); iter.hasNext();) {			
			Association assoc = (Association) iter.next();
			System.out.println("assoc "+assoc+" size: "+assoc.getMembers().size());
			System.out.println("assocDetail "+assoc.getTitle()+" "+assoc.getId());
			
			for (Iterator iterator = assoc.getTypes().iterator(); iterator.hasNext();) {
				Topic type = (Topic) iterator.next();
				assoc.setUser(t.getUser());
				//Why singleton?
				//See description and Test Code in TopicDAOHibernateImplTest.testToMakeSureWeDontCreateTooManyObjects()
				if(type instanceof MetaSeeAlso){
					MetaSeeAlso singleton = (MetaSeeAlso) DataAccessUtils.uniqueResult(getHibernateTemplate().find("from MetaSeeAlso"));
					if(singleton == null){
						getHibernateTemplate().saveOrUpdate(type);
					}else{
						assoc.getTypes().remove(type);
						assoc.getTypes().add(singleton);
					}
				}else{
					getHibernateTemplate().saveOrUpdate(type);
				}
			}
			getHibernateTemplate().saveOrUpdate(assoc);			
		}
	
		
		System.out.println("and middle Save me "+t.getTitle()+" type: "+t.getClass());
		//and save me
		//
		getHibernateTemplate().saveOrUpdate(t);		
		
		
		
		System.out.println("now TYPES "+t.getTypes().size());
		for (Iterator iter = t.getTypes().iterator(); iter.hasNext();) {
			Topic type = (Topic) iter.next();
			log.debug("saving its ? "+type.getTitle());

			//this let's us save a topic and make it's tag save too. 
			//needs to happen AFTER the save of the topic, or it will 
			//have a reference to the non-persisted topic.
			//
			//NOTE: having this after means, that all of a topic's tags
			//must be saved before this method is called or we'll get the 
			//reverse problem. This is different than metas which can be unsaved
			//and then will be saved above if new.
			//
			log.debug("save "+type.getTitle()+" "+type.getId());
			getHibernateTemplate().saveOrUpdate(type);
			log.debug("done");
		}
		
		
		
//		if (t instanceof Association) {			
//			Association assoc = (Association) t;
//			System.out.println("ASSOC ");
//			//
//			//key's aren't topic anymore. just "TO" "FROM" etc
//			//
//			for (Iterator iter = assoc.getMembers().values().iterator(); iter.hasNext();) {
//				Topic e = (Topic) iter.next();
//				System.out.println("e "+e);
//				System.out.println("id "+e.getId());
//				Topic realEntry = (Topic) getHibernateTemplate().get(Topic.class, e.getId());
//				System.out.println("real "+realEntry);
//				realEntry.getAssociations().add(assoc);				
//				getHibernateTemplate().saveOrUpdate(realEntry);				
//			}
//			
//			getHibernateTemplate().saveOrUpdate(assoc);
//		}
		return t;		
	}

	public List<TopicIdentifier> getLinksTo(Topic topic,User user) {
		Object[] params = {topic.getId(),user};
		System.out.println("----------------------------");
		System.out.println("------------"+topic+"-------");
		List<Object[]> list = getHibernateTemplate().find(""+
				"select title, id from Topic top "+		
				//"join top.associations "+
				"where ? in elements(top.associations.members) "+
				"and user is ? "
				,params);
//		
//		List<Object[]> list = getHibernateTemplate().find(""+
//				"select id from Association ass "+		
//				"where ? in elements(members) "+
//				"and user is ? "
//				,params);
//	
		
//		List<Object[]> ass = getHibernateTemplate().find(""+
//				"select title, id from Association ass "+		
//				"where ? in elements(members) "+
//				"and user is ? "
//				,params);
		
//		List<Association> l2 = getHibernateTemplate().find(""+
//				"from Association ass "+		
//				"where ? in elements(members) "+
//				"and user is ? "
//				,params);
//		
//		System.out.println("---------L2 "+l2.size());
//		for (Association association : l2) {
//			System.out.println("ass "+association+" ass "+association.getId());
//		}
		
		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}
		return rtn;		
	}

	public List<TopicIdentifier> getTopicIdsWithTag(Tag tag,User user) {			

		Object[] params = {tag.getId(),user};				
		List<Object[]> list = getHibernateTemplate().find(""+
				"select title, id from Topic top "+
				"where top.types.id is ? "+
				"and user is ? "
				,params);


		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO Genericize: http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}

		return rtn;		 		
	}

	/**
	 * Utility to set the projection properties for TopicIdentifier
	 * @return
	 */
	private Projection getTopicIdentifier(){
		return Projections.projectionList()
		.add(Property.forName("title"))
		.add(Property.forName("id"));
	}
	

	/**
	 * TODO replace string concatenations! 
	 */	
	public List<TopicIdentifier> getAllTopicIdentifiers(User user) {

		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.addOrder( Order.asc("title") )
		.setProjection(getTopicIdentifier());

		List<Object[]> list = getHibernateTemplate().findByCriteria(crit);

		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}

		return rtn;
	}

	public Topic getForID(User user, long topicID) {
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.add(Expression.eq("id", topicID)));
					
		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));			
	}

	public void tester() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * TESTING ONLY
	 * Should not be exposed to Service layer.
	 * 
	 * for when you really want the whole DB
	 * 
	 */
	public List<Topic> getAllTopics() {
		DetachedCriteria crit  = loadEmAll(DetachedCriteria.forClass(Topic.class));
		return getHibernateTemplate().findByCriteria(crit);
	}

}
