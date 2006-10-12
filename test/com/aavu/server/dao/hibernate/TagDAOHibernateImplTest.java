package com.aavu.server.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.TagStat;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.TopicDAO;
import com.aavu.server.dao.UserDAO;

public class TagDAOHibernateImplTest extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(TagDAOHibernateImplTest.class);

	private TagDAO tagDAO;
	private UserDAO userDAO;
	private TopicDAO topicDAO;
	
	private User u;	

	private String A = "VBXCXSS";
	private String B = "XVNSDF*(D";
	private String B2 = "XVN_DFSD";
	private String C = "41234HSAD@##";
	
	private int publicTagNumber;

	
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	public void setTopicDAO(TopicDAO topicDAO) {
		this.topicDAO = topicDAO;
	}

	
	@Override
	protected void onSetUpInTransaction() throws Exception {

		super.onSetUpInTransaction();

		u = userDAO.getUserByUsername("junit");

		publicTagNumber = tagDAO.getPublicTags().size();

	}

	private Tag[] add3(){

		Tag t1 = new Tag();
		t1.setName(A);
		t1.setUser(u);

		topicDAO.save(t1);

		Tag t2 = new Tag();
		t2.setName(B);
		t2.setUser(u);

		topicDAO.save(t2);

		Tag t3 = new Tag();
		t3.setName(B2);
		t3.setUser(u);

		topicDAO.save(t3);
		
		return new Tag[] {t1,t2,t3};
	}

	public void testGetAllTags() {
		add3();

		List<Tag> list = tagDAO.getAllTags(u);

		assertEquals(3 + publicTagNumber, list.size());
		
				
	}

	/**
	 * TODO MED
	 * test doesn't really test anything it works w/ or w/o the fix
	 * attempting to prove that:
	 * .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	 * really does something. It does (otherwise, I get dupes from 
	 * getAllTags() in live use, but I can't replicate in this test.
	 */
	public void testGetAllTagsNoDupes() {
		Tag[] tags = add3();

		Topic t1 = new Topic();
		t1.setUser(u);
		t1.tagTopic(tags[0]);		
		topicDAO.save(t1);
		
		topicDAO.save(t1);
		
		
		List<Tag> list = tagDAO.getAllTags(u);
		assertEquals(3 + publicTagNumber, list.size());
		
		Tag savedTag = tagDAO.getTag(u, A);
		
		Topic t2 = new Topic();
		t2.setUser(u);
		t2.tagTopic(savedTag);		
		topicDAO.save(t2);
		
		Topic t3 = new Topic();
		t3.setUser(u);
		t3.tagTopic(tags[1]);		
		topicDAO.save(t2);
		
		Topic t4 = new Topic();
		t4.setUser(u);
		t4.tagTopic(tags[1]);		
		topicDAO.save(t2);
		
		Topic t5 = new Topic();
		t5.setUser(u);
		t5.tagTopic(savedTag);		
		topicDAO.save(t2);
		
		list = tagDAO.getAllTags(u);
		assertEquals(3 + publicTagNumber, list.size());
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			System.out.println("r_tag "+tag.getId()+" "+tag.getTitle());
		}
		
		
	}
	
	public void testGetTag() {
		add3();
		Tag t = tagDAO.getTag(u, A);
		assertEquals(A,t.getName());
	}

	public void testGetTagsStarting() {

		add3();

		List<String> l1 = tagDAO.getTagsStarting(u, "Z");
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

		Meta meta = new MetaTopic();
		meta.setTitle(name);

		t2.addMeta(meta);


		System.out.println("before: "+t2.getId());
		topicDAO.save(t2);

		System.out.println("after: "+t2.getId());

		List<Tag> tagL = tagDAO.getAllTags(u);

		assertEquals(1+publicTagNumber, tagL.size());

		Tag saved = tagDAO.getTag(u, B);

		assertEquals(1,saved.getMetas().size());

		Meta savedM = (Meta) saved.getMetas().iterator().next();

		assertEquals(name, savedM.getName());

		System.out.println(savedM.getName());

		//now add another meta
		//
		Meta meta2 = new MetaTopic();
		meta2.setTitle(name2);

		saved.addMeta(meta2);

		topicDAO.save(saved);

		tagL = tagDAO.getAllTags(u);

		Tag savedNumber2 = tagL.get(0);
		assertEquals(2,savedNumber2.getMetas().size());

	}

	public void testTagStat(){
		Tag[] three = add3();

		//test
		//tag stats shoudl all be 0
		//
		List<TagStat> stats = tagDAO.getTagStats(u);
		for (TagStat ts : stats){
			assertEquals(0,ts.getNumberOfTopics());
			log.debug("stat "+ts.getTagId()+" "+ts.getNumberOfTopics());		
		}		
		
		Topic t = new Topic();
		t.setData(B);
		t.setTitle(C);
		t.setUser(u);
		t.tagTopic(three[0]);
					
		topicDAO.save(t);
		
		//test
		//tag 0 should have one topic
		//
		stats = tagDAO.getTagStats(u);
		for (TagStat ts : stats){
			if(ts.getTagId() == three[0].getId()){
				assertEquals(1,ts.getNumberOfTopics());
			}else{
				assertEquals(0,ts.getNumberOfTopics());
			}
			log.debug("stat "+ts.getTagId()+" "+ts.getNumberOfTopics());		
		}		
		
		//Add a new topic with tags 0 & 1
		//
		Topic t2 = new Topic();
		t2.setData(C);
		t2.setTitle(B2);
		t2.setUser(u);				
		t2.tagTopic(three[1]);				
		t2.tagTopic(three[0]);
		topicDAO.save(t2);
		
		//add tag 2 to topic 1
		t.tagTopic(three[2]);
		topicDAO.save(t);
		
		//test 
		//
		stats = tagDAO.getTagStats(u);
		for (TagStat ts : stats){
			if(ts.getTagId() == three[0].getId()){
				assertEquals(2,ts.getNumberOfTopics());
			}else if(ts.getTagId() == three[1].getId()){			
				assertEquals(1,ts.getNumberOfTopics());
			}
			else if(ts.getTagId() == three[2].getId()){			
				assertEquals(1,ts.getNumberOfTopics());
			}
			log.debug("stat "+ts.getTagId()+" "+ts.getNumberOfTopics());		
		}		
		
		
	}

}
