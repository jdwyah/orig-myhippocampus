package com.aavu.server.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.Meta;
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
		.setFetchMode("metaValues", FetchMode.JOIN)
		.setFetchMode("instances", FetchMode.JOIN)
		.setFetchMode("types", FetchMode.JOIN)
		.setFetchMode("types.metas", FetchMode.JOIN)
		.setFetchMode("metas", FetchMode.JOIN)
		.setFetchMode("occurences", FetchMode.JOIN)
		.setFetchMode("associations", FetchMode.JOIN)
		.setFetchMode("associations.members", FetchMode.JOIN);					
	}
	
	/**
	 * remember, MetaDate objects only exist for one Tag ie a Book's ReadDate is not a 
	 * play's ReadDate.  We search on Tag, because we could be timeline-ing a Tag:
	 * RomanceBook extends Book, with Book's ReadDate
	 * 
	 */
	public List<TimeLineObj> getTimeline(User user) {
		//
		//#1 select all Meta's that are displayable on a Timeline
		//
//		List<Meta> usersMetaDates = getHibernateTemplate().findByNamedParam("from Topic meta where "+
//				"meta.class = com.aavu.client.domain.MetaDate "+
//				"and (meta.parents.user = :user OR "+ 
//				"meta.parents.publicVisible = true)",
//				"user",user);		

//		List<Meta> usersMetaDates = getHibernateTemplate().find("from Topic meta where "+
//				"meta.class = com.aavu.client.domain.MetaDate "+
//				"and meta.parents.user.id > 0");
//				"and (meta.parents.user = :user OR "+ 
//				"meta.parents.publicVisible = true)",
//				"user",user);		

		
//		List<Meta> usersMetaDates = getHibernateTemplate().find("from Topic topic where "+
//				"5 in elements (topic.children.parents.user.id) ");
				//"and meta.parents.user.id > 0");
//				"and (meta.parents.user = :user OR "+ 
//				"meta.parents.publicVisible = true)",
//				"user",user);		
		
		List<Meta> dates = getHibernateTemplate().find("from MetaDate md where "+
		"");
		
		
		StringBuffer sb = new StringBuffer("where ");
		boolean first = true;
		for (Meta meta2 : dates) {			
			if(!first){
				sb.append("or ");							
			}
			sb.append(meta2.getId());
			sb.append(" in indices(top.metaValues) ");
			
			first = false;
		}
		
		log.debug("Metas Query "+sb.toString());
	
		//
		//#2 Get all the Topics with Meta's as described in #1
		//
		List<Topic> alltopics = getHibernateTemplate().findByNamedParam("from Topic top "+
				sb.toString()+
				"and user = :user ","user",user);
				
		
		
		//"and meta.parents.user.id > 0");
//		"and (meta.parents.user = :user OR "+ 
//		"meta.parents.publicVisible = true)",
//		"user",user);		
		
		
		//
		//#3 Create and return 
		//
		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();
		for (Topic topic : alltopics) {
			for(Meta m : dates){				
				if(topic.getMetaValues().containsKey(m.getId()+"")){
					String dateStr = (String) topic.getMetaValues().get(m.getId()+"");
					Date start = new Date(Long.parseLong(dateStr));
					TopicIdentifier ti = topic.getIdentifier(); 												
					TimeLineObj to = new TimeLineObj(ti,start,null);												
					rtn.add(to);					
				}
			}				
		}

		if(log.isDebugEnabled())
			for (TimeLineObj obj : rtn) {
				log.debug("TIMELINE ");
				log.debug(obj);
			}

		
		return rtn;
		
//		return getHibernateTemplate().findByNamedParam("from Tag tag where (user = :user OR publicVisible = true) "+
//				"AND tag.", "user", user);
				
//		String[] names = {"tag","dateID"};
//		Object[] vals = {tag.getId(),metaDate.getId()};

//		return getHibernateTemplate().findByNamedParam("from Topic top "+
//				"join fetch top.metaValueStrs as metav where "+
//				"top.tags.id is :tag "+ 
//				"and :dateID in indices(top.metaValueStrs) ",names,vals);

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
		
		

		System.out.println("METAS "+t.getMetas().size());
		for (Iterator iter = t.getMetas().iterator(); iter.hasNext();) {
			Meta meta = (Meta) iter.next();
			log.debug("saving its metas ? "+meta.getTitle());		
			if(meta.getId() == 0){			
				log.debug("was unsaved. save"+meta.getTitle());
				getHibernateTemplate().saveOrUpdate(meta);
			}
			log.debug("done");
		}
		
		System.out.println("METAValues "+t.getMetaValues().entrySet().size());
		for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
			Topic metaValue = (Topic) t.getMetaValues().get(iter.next());
			log.debug("saving its metavalue element ? "+metaValue.getTitle());		
			if(metaValue.getId() == 0){			
				log.debug("was unsaved. save"+metaValue.getTitle());
				getHibernateTemplate().saveOrUpdate(metaValue);
			}
			log.debug("done");
		}
		
		System.out.println("now set Associations if it's a see also: ");
		for (Iterator iter = t.getAssociations().iterator(); iter.hasNext();) {			
			Association assoc = (Association) iter.next();
			System.out.println("assoc "+assoc+" size: "+assoc.getMembers().size());
			getHibernateTemplate().saveOrUpdate(assoc);
		}
		
		System.out.println("and middle Save me "+t.getTitle());
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

}
