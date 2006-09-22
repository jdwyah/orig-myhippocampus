package com.aavu.server.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.springframework.dao.support.DataAccessUtils;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.User;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.UserDAO;

public class TagDAOHibernateImplTest extends HibernateTransactionalTest {

	private TagDAO tagDAO;
	private UserDAO userDAO;
	private User u;	

	private String A = "VBXCXSS";
	private String B = "XVNSDF*(D";
	private String B2 = "XVN_DFSD";
	private int publicTagNumber;

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

		publicTagNumber = tagDAO.getPublicTags().size();

	}

	private void add3(){

		Tag t1 = new Tag();
		t1.setName(A);
		t1.setUser(u);

		tagDAO.save(t1);

		Tag t2 = new Tag();
		t2.setName(B);
		t2.setUser(u);

		tagDAO.save(t2);

		Tag t3 = new Tag();
		t3.setName(B2);
		t3.setUser(u);

		tagDAO.save(t3);
	}

	public void testGetAllTags() {
		add3();

		List<Tag> list = tagDAO.getAllTags(u);

		assertEquals(3 + publicTagNumber, list.size());
	}

	public void testGetTag() {
		add3();
		Tag t = tagDAO.getTag(u, A);
		assertEquals(A,t.getName());
	}

	public void testGetTagsStarting() {

		add3();

		List<Tag> l1 = tagDAO.getTagsStarting(u, "Z");
		assertEquals(0, l1.size());

		l1 = tagDAO.getTagsStarting(u, "X");
		assertEquals(2, l1.size());

		l1 = tagDAO.getTagsStarting(u, "XVN");
		assertEquals(2, l1.size());

		l1 = tagDAO.getTagsStarting(u, "XVNS");
		assertEquals(1, l1.size());

	}

	public void testRemoveTag() throws PermissionDeniedException {

		add3();

		Tag t = tagDAO.getTag(u, A);
		assertEquals(A,t.getName());

		tagDAO.removeTag(u, t);

		List<Tag> list = tagDAO.getAllTags(u);		
		assertEquals(2+publicTagNumber, list.size());
	}

	public void testSave() {
		String name = "metaname";
		String name2 = "mname2";

		Tag t2 = new Tag();
		t2.setName(B);
		t2.setUser(u);

		Meta meta = new MetaText();
		meta.setName(name);

		t2.getMetas().add(meta);


		System.out.println("before: "+t2.getId());
		tagDAO.save(t2);

		System.out.println("after: "+t2.getId());

		List<Tag> tagL = tagDAO.getAllTags(u);

		assertEquals(1+publicTagNumber, tagL.size());

		Tag saved = tagDAO.getTag(u, B);

		assertEquals(1,saved.getMetas().size());

		Meta savedM = (Meta) saved.getMetas().get(0);

		assertEquals(name, savedM.getName());

		System.out.println(savedM.getName());

		//now add another meta
		//
		Meta meta2 = new MetaText();
		meta2.setName(name2);

		saved.getMetas().add(meta2);

		tagDAO.save(saved);

		tagL = tagDAO.getAllTags(u);

		Tag savedNumber2 = tagL.get(0);
		assertEquals(2,savedNumber2.getMetas().size());

	}

	public void testTagStat(){
		add3();

		List<TagStat> stats = tagDAO.getTagStats(u);

		for (TagStat ts : stats){
			assertEquals(0,ts.getNumberOfTopics().intValue());
			System.out.println("stat "+ts.getTagId()+" "+ts.getNumberOfTopics());		
		}		
		
		
		
	}

}
