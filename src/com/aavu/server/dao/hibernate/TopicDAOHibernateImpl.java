package com.aavu.server.dao.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TopicDAO;

public class TopicDAOHibernateImpl extends HibernateDaoSupport implements TopicDAO{
	

	public List<Topic> getAllTopics(User user) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.addOrder( Order.asc("title") )
		.setFetchMode("user", FetchMode.JOIN);
		
		
				
		return getHibernateTemplate().findByCriteria(crit);
		
		
		//left join fetch topic.metaValues
		//List<Topic> list =getHibernateTemplate().findByNamedParam("from Topic topic where topic.user = :user ", "user", user);
		
//		for(Topic t: list){
//			for (Iterator iter = t.getMetaValues().keySet().iterator(); iter.hasNext();) {
//				Meta element = (Meta) iter.next();
//				Meta m2 = getMeta(element);
//				t.getMetaValues().put(m2, t.getMetaValues().get(m2));
//			}
//			
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

	public Topic getForName(User user, String string) {
		
		DetachedCriteria crit  = DetachedCriteria.forClass(Topic.class)
		.add(Expression.eq("user", user))
		.add(Expression.eq("title", string));
				
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
//			System.out.println("b");
//			Meta element = (Meta) iter.next();
//			getHibernateTemplate().saveOrUpdate(element);
//			System.out.println("c");
//			MetaValue mv = (MetaValue) t.getMetaValues().get(element);
//			getHibernateTemplate().saveOrUpdate(mv);
//			System.out.println("d");
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
				"where top.tags.id is ? "+
				"and user is ? "
				,params);
				
		
		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}
		
		return rtn;		 		
	}

	/**
	 * TODO replace string concatenations! 
	 */	
	public List<TopicIdentifier> getAllTopicIdentifiers(User user) {
		List<Object[]> list = getHibernateTemplate().findByNamedParam(""+
				"select title, id from Topic top "+				
				"where user is :user "+
				"order by title asc "
				,"user",user);
		
		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}
		
		return rtn;
	}

}
