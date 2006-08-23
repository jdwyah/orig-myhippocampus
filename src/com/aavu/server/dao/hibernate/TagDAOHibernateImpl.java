package com.aavu.server.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TagDAO;

public class TagDAOHibernateImpl extends HibernateDaoSupport implements TagDAO {

	public List<Tag> getAllTags(User user) {
		return getHibernateTemplate().findByNamedParam("from Tag where user = :user", "user", user);
	}

	public Tag getTag(User user, String tagName) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Tag.class)		
		.add(Expression.eq("user", user))
		.add(Expression.eq("name", tagName));
				
		return (Tag) DataAccessUtils.uniqueResult(getHibernateTemplate().findByCriteria(crit));
	}

	public List<Tag> getTagsStarting(User user, String match) {
		DetachedCriteria crit  = DetachedCriteria.forClass(Tag.class)		
		.add(Expression.eq("user", user))
		.add(Expression.ilike("name", match, MatchMode.START));
		
		return getHibernateTemplate().findByCriteria(crit);
	}

	public void removeTag(User user, Tag selectedTag) {
		//TODO check rights to delete this tag
		getHibernateTemplate().delete(selectedTag);
	}

	public void save(Tag selectedTag) {
		getHibernateTemplate().saveOrUpdate(selectedTag);
	}

}