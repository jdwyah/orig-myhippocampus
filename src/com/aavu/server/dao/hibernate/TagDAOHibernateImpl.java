package com.aavu.server.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.TopicIdentifier;
import com.aavu.client.domain.User;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.TagDAO;

public class TagDAOHibernateImpl extends HibernateDaoSupport implements TagDAO {

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
						
//		List<Object[]> list = getHibernateTemplate().find(""+
//				"select tag.id, tag.name, count(tag.topics) from Tag tag "+
//				"where user is ? "
//				,user);
		List<Object[]> list = getHibernateTemplate().find(""+
				"select tag.id, tag.topics.size from Tag tag "+
				//"left join topics "+
				"where user is ? "
				,user);
		
		List<TagStat> rtn = new ArrayList<TagStat>(list.size());

		//TODO http://sourceforge.net/forum/forum.php?forum_id=459719
		//
		for (Object[] o : list){
			System.out.println("o "+o);
			for (int i = 0; i < o.length; i++) {
				Object object = o[i];
				System.out.println(object);
			}
			rtn.add(new TagStat((Long)o[0],(Integer)o[1]));			
		}
		
		return rtn;		 		
		
	}

}
