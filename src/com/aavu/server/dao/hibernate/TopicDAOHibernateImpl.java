package com.aavu.server.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
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

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TimeLineObj;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TopicDAO;

public class TopicDAOHibernateImpl extends HibernateDaoSupport implements TopicDAO{
	private static final Logger log = Logger.getLogger(TopicDAOHibernateImpl.class);

	public List<Topic> getAllTopics(User user) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.addOrder( Order.asc("title") )
		.setFetchMode("user", FetchMode.JOIN);



		return getHibernateTemplate().findByCriteria(crit);


		//left join fetch topic.metaValues
		//List<Topic> list =getHibernateTemplate().findByNamedParam("from Topic topic where topic.user = :user ", "user", user);

//		for(Topic t: list){
//		for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
//		Meta element = (Meta) iter.next();
//		Meta m2 = getMeta(element);
//		t.getMetaValues().put(m2, t.getMetaValues().get(m2));
//		}

//		}
//		return list;

		//return getHibernateTemplate().findByNamedParam("from Topic where user = :user", "user", user);
	}

	public List<Topic> getBlogTopics(User user, int start, int numberPerScreen) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.addOrder( Order.desc("lastUpdated") )
		.setFetchMode("user", FetchMode.JOIN);

		return getHibernateTemplate().findByCriteria(crit);

		//return getHibernateTemplate().findByNamedParam("from Topic where user = :user order by lastUpdated", "user", user);
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

		
		List<Meta> usersMetaDates = getHibernateTemplate().find("from Topic topic where "+
				"5 in elements (topic.children.parents.user.id) ");
				//"and meta.parents.user.id > 0");
//				"and (meta.parents.user = :user OR "+ 
//				"meta.parents.publicVisible = true)",
//				"user",user);		
		
		StringBuffer sb = new StringBuffer("where ");
		boolean first = true;
		for (Meta meta2 : usersMetaDates) {			
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
				

		//
		//#3 Create and return 
		//
		List<TimeLineObj> rtn = new ArrayList<TimeLineObj>();
		for (Topic topic : alltopics) {
			for(Meta m : usersMetaDates){				
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

		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.add(Expression.eq("title", string))
		.setFetchMode("user", FetchMode.JOIN)
		.setFetchMode("metaValues", FetchMode.JOIN)
		.setFetchMode("parents", FetchMode.JOIN)
		//.setFetchMode("children", FetchMode.JOIN)
		.setFetchMode("metas", FetchMode.JOIN)
		.setFetchMode("occurrences", FetchMode.JOIN)
		.setFetchMode("associations", FetchMode.JOIN);

		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));

		//return getHibernateTemplate().findByNamedParam("from Topic where user = :user and title = :title", "user", user);
	}

	public List<Topic> getTopicsStarting(User user, String match) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)		
		.add(Expression.eq("user", user))
		.add(Expression.ilike("title", match, MatchMode.ANYWHERE))
		.addOrder( Order.asc("title") )
		.setFetchMode("user", FetchMode.JOIN);

		return getHibernateTemplate().findByCriteria(crit);
	}

	public Topic save(Topic t) {
		System.out.println("a");
//		for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
//		System.out.println("b");
//		Meta element = (Meta) iter.next();
//		getHibernateTemplate().saveOrUpdate(element);
//		System.out.println("c");
//		MetaValue mv = (MetaValue) t.getMetaValues().get(element);
//		getHibernateTemplate().saveOrUpdate(mv);
//		System.out.println("d");
//		}

		getHibernateTemplate().saveOrUpdate(t);
		return t;
	}

	public void tester() {
		// TODO Auto-generated method stub

	}

	public List<TopicIdentifier> getTopicIdsWithTag(Tag tag,User user) {			

		Object[] params = {tag.getId(),user};				
		List<Object[]> list = getHibernateTemplate().find(""+
				"select title, id from Topic top "+
				"where top.parents.id is ? "+
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

	public Topic getForID(User currentUser, long topicID) {					
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", currentUser))
		.add(Expression.eq("id", topicID))
		.setFetchMode("user", FetchMode.JOIN);			
		return (Topic) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));			
	}

}
