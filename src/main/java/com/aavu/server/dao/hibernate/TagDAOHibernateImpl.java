package com.aavu.server.dao.hibernate;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.server.dao.TagDAO;

public class TagDAOHibernateImpl extends HibernateDaoSupport implements TagDAO {
	private static final Logger log = Logger.getLogger(TagDAOHibernateImpl.class);
	private static final int DEFAULT_TAG_AUTOCOMPLETE_MAX = 7;
	
	public List<Tag> getAllTags(User user) {
		DetachedCriteria crit  = TopicDAOHibernateImpl.loadEmAll(DetachedCriteria.forClass(Tag.class)
		.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.add(Expression.eq("user", user)));
		
		return getHibernateTemplate().findByCriteria(crit);	
	}

	/**
	 * 
	 */
	public Tag getTag(User user, String tagName) {
		DetachedCriteria crit  =  TopicDAOHibernateImpl.loadEmAll(DetachedCriteria.forClass(Tag.class)
		.add(Expression.and(Expression.eq("title", tagName),				
				Expression.eq("user", user))));
		
		return (Tag) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));
	}

	public List<TopicIdentifier> getTagsStarting(User user, String match) {
		return getTagsStarting(user, match, DEFAULT_TAG_AUTOCOMPLETE_MAX);		
	}
	public List<TopicIdentifier> getTagsStarting(User user, String match,int max) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Tag.class)		
		.add(Expression.and(Expression.ilike("title", match, MatchMode.START),				
				Expression.eq("user", user)))
		.setProjection(Projections.projectionList()
		.add(Property.forName("title"))
		.add(Property.forName("id")));			
		log.debug("USER: "+user+" USER ID "+user.getId()+" NAME "+user.getUsername()+" MATCH|"+match+"|");
		
		List<Object[]> list = getHibernateTemplate().findByCriteria(crit,0,max);
		
		List<TopicIdentifier> rtn = new ArrayList<TopicIdentifier>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			rtn.add(new TopicIdentifier((Long)o[1],(String)o[0]));			
		}		
		return rtn;				
	}

	/**
	 * testing only
	 */
	public List<Tag> getPublicTags() {
		return getHibernateTemplate().find("from Tag where publicVisible = true");
	}

	public List<TagStat> getTagStats(User user) {
						
		List<Object[]> list = getHibernateTemplate().find(""+
				"select tag.id, tag.instances.size, tag.title, tag.latitude, tag.longitude from Tag tag "+
				"where  user is ? order by tag.title"
				,user);
		
		//This is the query if we decide to get rid of the instances mapping again.
		//
//		List<Object[]> list = getHibernateTemplate().find(""+
//				"select conn.type.id, count(conn.type), conn.type.title, conn.type.latitude, conn.type.longitude from TopicTypeConnector conn "+
//				//"left join topic "+
//				"where  conn.topic.user is ? and conn.type.class = Tag "+
//				"group by conn.type"
//				,user);
		
		
		
//		List<Object[]> subjectList = getHibernateTemplate().find(""+
//				"select top.subject.class.id, top.subject.class, count(top.subject.class) from Topic top "+
//				"where top.user is ? "+
//				"group by top.subject.class"
//				,user);
		
		log.debug("tagstats size "+list.size());		
//		log.debug("subject list: "+subjectList.size());
		
//		List<TagStat> rtn = new ArrayList<TagStat>(subjectList.size() + list.size());
		List<TagStat> rtn = new ArrayList<TagStat>(list.size());
		
//		for (Object[] o : subjectList){
//			if(log.isDebugEnabled()){
//				log.debug("SUBJECT "+o[0]+" "+o[1]+" "+o[2]);				
//			}
//			rtn.add(new TagStat((Long)o[0],(String)o[1],(Integer)o[2]));			
//		}
		
		
		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
//			if(log.isDebugEnabled()){
//				log.debug("TagStat "+o[0]+" "+o[1]+" "+o[2]+" "+o[3]+" "+o[4]);	
//				//log.debug("TagStat "+o[0].getClass()+" "+o[1].getClass()+" "+o[2].getClass()+" "+o[3].getClass()+" "+o[4].getClass());
//			}			
			
			rtn.add(new TagStat((Long)o[0],(Integer)o[1],(String)o[2],(Integer) o[3],(Integer) o[4]));			
		}
		
		return rtn;		 				
	}

	/**
	 * PEND low, don't _really_ need to return a LoadEmAll tag do we. Could do TagIdentifier..
	 */
	public Tag upgradeToTag(final Topic t) {

		int res = (Integer) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session sess) throws HibernateException, SQLException {
				String hqlUpdate = "update Topic set discriminator = 'tag' where topic_id = :id";
				int updatedEntities = sess.createQuery( hqlUpdate )
				.setLong( "id", t.getId() )	                            
				.executeUpdate();				
				return updatedEntities;				
			}});
		log.debug("res: "+res);
		
		getHibernateTemplate().evict(t);
		
		DetachedCriteria crit  =  TopicDAOHibernateImpl.loadEmAll(DetachedCriteria.forClass(Tag.class)
				.add(Expression.eq("id", t.getId())));
		
		return (Tag) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));		
		
	}

}
