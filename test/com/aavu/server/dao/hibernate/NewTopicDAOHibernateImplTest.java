package com.aavu.server.dao.hibernate;

import com.aavu.client.domain.Topic;
import com.aavu.server.dao.NewTopicDAO;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.UserDAO;

public class NewTopicDAOHibernateImplTest extends HibernateTransactionalTest {

	private NewTopicDAO topicDAO;
	private TagDAO tagDAO;
	
	
	
	private UserDAO userDAO;
	public static Topic DATE_TYPE;



	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	
	public void testTest(){
		
	//	User u = userDAO.getUserByUsername("test");		
		
		topicDAO.test(null);
		
		System.out.println("done");
	}
	
	public void testSave(){
		
		
		Topic book = new Topic();
		book.setTitle("Book");
		
		Topic author = new Topic();
		author.setTitle("Author");
		
		//AUTHOR
		//
		topicDAO.save(author);
		
		book.addMeta(author);		
		
		//BOOK
		//
		topicDAO.save(book);
		
					
		Topic paris19 = new Topic();
		paris19.setTitle("Paris 1919");
	
		
		paris19.tagTopic(book);
		
		Topic macmillan = new Topic();
		macmillan.setTitle("MacMillan");
		
		
		System.out.println(paris19+"size "+paris19.getMetaValues());
		paris19.addMetaValue(author,macmillan);			
		System.out.println(paris19+"size "+paris19.getMetaValues());
		
		paris19.getMetaList();
		
		
		Topic savedParis = topicDAO.save(paris19);
		
		System.out.println("saved");
		System.out.println(savedParis);
		savedParis.getMetaList();
	}
	
	public void testSaveMetaDates(){
		
		DATE_TYPE = new Topic();
		DATE_TYPE.setTitle("DATE");		
		topicDAO.save(DATE_TYPE);
		
		
		Topic book = new Topic();
		book.setTitle("Book");
		
		Topic author = new Topic();
		author.setTitle("Author");
		
		Topic dateRead = new Topic();
		dateRead.setTitle("Date read");		
		dateRead.addParent(DATE_TYPE);
		
		
		//AUTHOR
		//
		topicDAO.save(author);
		
			
		book.addMeta(author);
		book.addMeta(dateRead);
		
		
		
		//BOOK
		//
		topicDAO.save(book);
		
					
		Topic paris19 = new Topic();
		paris19.setTitle("Paris 1919");
	
		
		paris19.tagTopic(book);
		
		Topic macmillan = new Topic();
		macmillan.setTitle("MacMillan");
		
		
		System.out.println(paris19+"size "+paris19.getMetaValues());
		paris19.addMetaValue(author,macmillan);			
		System.out.println(paris19+"size "+paris19.getMetaValues());
		
		Topic nineteenNinety8 = new Topic();
		nineteenNinety8.setTitle("1998");
		
		paris19.addMetaValue(dateRead, nineteenNinety8);
		
		
		System.out.println("----------------");
		paris19.getMetaList();
		System.out.println("----------------");
		
		Topic savedParis = topicDAO.save(paris19);
		
		Topic loaded = topicDAO.get(savedParis.getId());
		
		System.out.println("---------saved------------");
		System.out.println(loaded);
		loaded.getMetaList();
		System.out.println("----------------");
	}
	
	
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}

	public NewTopicDAO getTopicDAO() {
		return topicDAO;
	}

	public void setTopicDAO(NewTopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}

}
