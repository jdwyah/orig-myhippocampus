package com.aavu.server.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TagDAO;

public class TagDAOHibernateImpl extends HibernateDaoSupport implements TagDAO {
	private static final Logger log = Logger.getLogger(TagDAOHibernateImpl.class);
	
	public List<Tag> getAllTags(User user) {
		return getHibernateTemplate().findByNamedParam("from Tag where user = :user OR publicVisible = true", "user", user);
	}

	/**
	 * 
	 */
	public Tag getTag(User user, String tagName) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Tag.class)
		.add(Expression.and(Expression.eq("name", tagName),
				Expression.or(
				Expression.eq("user", user),Expression.eq("publicVisible", true))));
				
		return (Tag) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));
	}

	public List<Tag> getTagsStarting(User user, String match) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Tag.class)		
		.add(Expression.and(Expression.ilike("name", match, MatchMode.START),
				Expression.or(
				Expression.eq("user", user),Expression.eq("publicVisible", true))));
		
		return getHibernateTemplate().findByCriteria(crit);
	}

	public void removeTag(User user, Tag selectedTag){		
		getHibernateTemplate().delete(selectedTag);		
	}

	public void save(Tag selectedTag) {
		getHibernateTemplate().saveOrUpdate(selectedTag);
	}

	public List<Tag> getPublicTags() {
		return getHibernateTemplate().find("from Tag where publicVisible = true");
	}

	public List<TagStat> getTagStats(User user) {
						
		List<Object[]> list = getHibernateTemplate().find(""+
				"select tag.id, tag.name, tag.topics.size from Tag tag "+
				//"left join topics "+
				"where user is ? "
				,user);
		
		List<TagStat> rtn = new ArrayList<TagStat>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			if(log.isDebugEnabled()){
				log.debug("TagStat "+o[0]+" "+o[1]+" "+o[2]);				
			}
			rtn.add(new TagStat((Long)o[0],(String)o[1],(Integer)o[2]));			
		}
		
		return rtn;		 				
	}

}
