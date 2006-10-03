package com.aavu.server.dao.hibernate;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.server.dao.NewTopicDAO;

public class NewTopicDAOHibernateImpl extends HibernateDaoSupport implements NewTopicDAO{
	private static final Logger log = Logger.getLogger(NewTopicDAOHibernateImpl.class);


	public Topic save(Topic topic){		
		getHibernateTemplate().saveOrUpdate(topic);
		return topic;
	}
	public Topic get(long id){
		return (Topic) getHibernateTemplate().get(Topic.class,id);
	}
	
	public void test(User user) {
		
		//Topic TEXT_META_VALUE = new Topic();		 
		
		
		Topic book = new Topic();
		book.setTitle("Book");
		
		Topic author = new Topic();
		author.setTitle("Author");
		
			
		book.getMetas().add(author);
					
		Topic paris19 = new Topic();
		paris19.setTitle("Paris 1919");
	
		
		paris19.tagTopic(book);
				
		Topic macmillan = new Topic();
		macmillan.setTitle("MacMillan");
		
		
		System.out.println(paris19+"size "+paris19.getMetaValues());
		paris19.addMetaValue(author,macmillan);			
		System.out.println(paris19+"size "+paris19.getMetaValues());
		
//		MetaLink authorLink = new MetaLink();
//				
//		authorLink.setFrom(author);
//		authorLink.setTo(macmillan);
		
		paris19.getMetaList();
	}
	
	

}
