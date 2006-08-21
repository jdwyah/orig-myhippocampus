package com.aavu.server.dao.hibernate;

import java.util.List;

import junit.framework.TestCase;

import com.aavu.client.domain.Tag;
import com.aavu.client.domain.User;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.UserDAO;

public class TagDAOHibernateImplTest extends HibernateTransactionalTest {

	private TagDAO tagDAO;
	private UserDAO userDAO;
	private User u;	

	private String A = "VBXCXSS";
	private String B = "SDNSDF*(D";
	private String B2 = "SDN_DFSD";
	
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	
	
	@Override
	protected void onSetUpInTransaction() throws Exception {
	
		super.onSetUpInTransaction();
	
		u = userDAO.getUserByUsername("vpech");
		
		
		Tag t1 = new Tag();
		t1.setName(A);
		t1.setUser(u);
		
		tagDAO.save(t1);
		
		Tag t2 = new Tag();
		t2.setName(B);
		t2.setUser(u);
		
		tagDAO.save(t2);
		
		Tag t3 = new Tag();
		t3.setName(B);
		t3.setUser(u);
		
		tagDAO.save(t3);
		
	}
	public void testGetAllTags() {
		
		List<Tag> list = tagDAO.getAllTags(u);
		
		assertEquals(3, list.size());
	}

	public void testGetTag() {
		
		Tag t = tagDAO.getTag(u, A);
		assertEquals(A,t.getName());
	}

	public void testGetTagsStarting() {
		
		List<Tag> l1 = tagDAO.getTagsStarting(u, "X");
		assertEquals(0, l1.size());
		
		l1 = tagDAO.getTagsStarting(u, "S");
		assertEquals(2, l1.size());
		
		l1 = tagDAO.getTagsStarting(u, "SDN");
		assertEquals(2, l1.size());
		
		l1 = tagDAO.getTagsStarting(u, "SDNS");
		assertEquals(1, l1.size());
		
	}

	public void testRemoveTag() {
		
		Tag t = tagDAO.getTag(u, A);
		assertEquals(A,t.getName());
		
		tagDAO.removeTag(u, t);
		
		List<Tag> list = tagDAO.getAllTags(u);		
		assertEquals(2, list.size());
	}

	public void testSave() {
		fail("Not yet implemented");
	}

}
