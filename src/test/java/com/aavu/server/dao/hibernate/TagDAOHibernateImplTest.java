package com.aavu.server.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.aavu.client.domain.Meta;
import com.aavu.client.domain.MetaTopic;
import com.aavu.client.domain.Tag;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.domain.dto.TagStat;
import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.client.exception.PermissionDeniedException;
import com.aavu.server.dao.EditDAO;
import com.aavu.server.dao.SelectDAO;
import com.aavu.server.dao.TagDAO;
import com.aavu.server.dao.UserDAO;

public class TagDAOHibernateImplTest extends HibernateTransactionalTest {
	private static final Logger log = Logger.getLogger(TagDAOHibernateImplTest.class);

	private TagDAO tagDAO;
	private UserDAO userDAO;
	private SelectDAO selectDAO;
	private EditDAO editDAO;
	
	private User u;	

	private String A = "VBXCXSS";
	private String B = "XVNSDF*(D";
	private String B2 = "XVN_DFSD";
	private String B3 = "#378942425494  944;;;435 45;;45 34534&*^&%^@#(@*@(";
	private String C = "41234HSAD@##";
	private String S = "SUBJECT Paris";
	private String G = "*(DY8932kl";
	
	
	public void setTagDAO(TagDAO tagDAO) {
		this.tagDAO = tagDAO;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	public void setSelectDAO(SelectDAO selectDAO) {
		this.selectDAO = selectDAO;
	}
	public void setEditDAO(EditDAO editDAO) {
		this.editDAO = editDAO;
	}
	
	@Override
	protected void onSetUpInTransaction() throws Exception {

		super.onSetUpInTransaction();

		u = userDAO.getUserByUsername("junit");
		
	}

	private Tag[] add3() throws HippoBusinessException{

		Tag t1 = new Tag();
		t1.setName(A);
		t1.setUser(u);

		t1 = (Tag) editDAO.save(t1);

		Tag t2 = new Tag();
		t2.setName(B);
		t2.setUser(u);

		t2 = (Tag) editDAO.save(t2);

		Tag t3 = new Tag();
		t3.setName(B2);
		t3.setUser(u);

		t3 = (Tag) editDAO.save(t3);
		
		return new Tag[] {t1,t2,t3};
	}

	public void testGetAllTags() throws HippoBusinessException {
		add3();

		List<Tag> list = tagDAO.getAllTags(u);

		assertEquals(3, list.size());
		
				
	}

	/**
	 * TODO MED
	 * test doesn't really test anything it works w/ or w/o the fix
	 * attempting to prove that:
	 * .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
	 * really does something. It does (otherwise, I get dupes from 
	 * getAllTags() in live use, but I can't replicate in this test.
	 * @throws HippoBusinessException 
	 */
	public void testGetAllTagsNoDupes() throws HippoBusinessException {
		Tag[] tags = add3();

		Topic t1 = new Topic(u,"FOO");		
		t1.tagTopic(tags[0]);		
		editDAO.save(t1);		
		
		List<Tag> list = tagDAO.getAllTags(u);
		assertEquals(3, list.size());
		
		Tag savedTag = tagDAO.getTag(u, A);
		
		Topic t2 = new Topic(u,"BAR");		
		t2.tagTopic(savedTag);		
		editDAO.save(t2);
		
		Topic t3 = new Topic(u,"SHMEE");		
		t3.tagTopic(tags[1]);		
		editDAO.save(t3);
		
		Topic t4 = new Topic(u,"foobee");
		t4.setUser(u);
		t4.tagTopic(tags[1]);		
		editDAO.save(t4);
		
		Topic t5 = new Topic(u,"fee");
		t5.setUser(u);
		t5.tagTopic(savedTag);		
		editDAO.save(t5);
		
		list = tagDAO.getAllTags(u);
		assertEquals(3, list.size());
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Tag tag = (Tag) iter.next();
			System.out.println("r_tag "+tag.getId()+" "+tag.getTitle());
		}
		
		
	}
	
	public void testGetTag() throws HippoBusinessException {
		add3();
		Tag t = tagDAO.getTag(u, A);
		assertEquals(A,t.getName());
	}

	public void testGetTagsStarting() throws HippoBusinessException {

		add3();

		List<TopicIdentifier> l1 = tagDAO.getTagsStarting(u, "Z");
		assertEquals(0, l1.size());

		l1 = tagDAO.getTagsStarting(u, "X");
		assertEquals(2, l1.size());

		l1 = tagDAO.getTagsStarting(u, "XVN");
		assertEquals(2, l1.size());

		l1 = tagDAO.getTagsStarting(u, "XVNS");
		assertEquals(1, l1.size());

	}

	public void testRemoveTag() throws PermissionDeniedException, HippoBusinessException {

		add3();

		Tag t = tagDAO.getTag(u, A);
		assertEquals(A,t.getName());

		editDAO.delete(t);

		List<Tag> list = tagDAO.getAllTags(u);		
		
		for (Tag tag : list) {
			if(tag.getName().equals(A)){
				System.out.println("A");	
			}
			else if(tag.getName().equals(B)){
				System.out.println("B");	
			}
			else if(tag.getName().equals(B2)){
				System.out.println("B2");	
			}
			else{
				System.out.println("Other "+tag.getName());
			}
		}
		
		assertEquals(2, list.size());
	}

	public void testSave() throws HippoBusinessException {
		String name = "metaname";
		String name2 = "mname2";

		Tag t2 = new Tag();
		t2.setName(B);
		t2.setUser(u);

		Meta meta = new MetaTopic();
		meta.setTitle(name);

		t2.addTagProperty(meta);


		System.out.println("before: "+t2.getId()+" "+t2);
		editDAO.save(t2);

		System.out.println("after: "+t2.getId()+" "+t2);

		List<Tag> tagL = tagDAO.getAllTags(u);

		assertEquals(1, tagL.size());

		Tag saved = tagDAO.getTag(u, B);

		assertEquals(1,saved.getTagProperties().size());

		Meta savedM = (Meta) saved.getTagProperties().iterator().next();

		assertEquals(name, savedM.getName());

		System.out.println(savedM.getName());

		//now add another meta
		//
		Meta meta2 = new MetaTopic();
		meta2.setTitle(name2);

		saved.addTagProperty(meta2);

		editDAO.save(saved);

		tagL = tagDAO.getAllTags(u);

		assertEquals(1, tagL.size());
		
		Tag savedNumber2 = tagL.get(0);
				
		System.out.println(savedNumber2.toPrettyString());
		assertEquals(2,savedNumber2.getTagProperties().size());

	}

	public void testTagStat() throws HippoBusinessException{
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
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);
		t.tagTopic(three[0]);
					
		t = editDAO.save(t);
		
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
		t2.getLatestEntry().setData(C);
		t2.setTitle(B3);
		t2.setUser(u);				
		t2.tagTopic(three[1]);				
		t2.tagTopic(three[0]);
		t2 = editDAO.save(t2);
		
		//add tag 2 to topic 1
		t.tagTopic(three[2]);
		t = editDAO.save(t);
		
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
	
	/**
	 * NOTE: changed my mind about whether subjects shoudl be tags. Decided that they're 
	 * not. Instead the SubjectGui is doing some auto-tagging, but that's beyond our concern here.
	 * These tests now show that subjects are irrelevant. 
	 * @throws HippoBusinessException
	 */
	public void testTagStatWithSubjects() throws HippoBusinessException{
		Tag[] three = add3();

		//test
		//tag stats shoudl all be 0
		//
		List<TagStat> stats = tagDAO.getTagStats(u);
		for (TagStat ts : stats){
			assertEquals(0,ts.getNumberOfTopics());
			log.debug("stat "+ts.getTagId()+" "+ts.getNumberOfTopics()+" lat "+ts.getLatitude()+" long "+ts.getLongitude());		
		}		
		
		Subject subj1 = new AmazonBook();
		subj1.setForeignID(A);
		subj1.setName(S);
		
		Topic t = new Topic();
		t.getLatestEntry().setData(B);
		t.setTitle(C);
		t.setUser(u);
		t.tagTopic(three[0]);
		t.setSubject(subj1);
					
		t = editDAO.save(t);
		
		//test
		//tag 0 should have one topic
		//
		stats = tagDAO.getTagStats(u);
		
		for (TagStat ts : stats){
			log.debug("stat "+ts.getTagId()+" "+ts.getNumberOfTopics()+" "+ts.getTagName());
			
			if(ts.getTagId() == three[0].getId()){
				assertEquals(1,ts.getNumberOfTopics());
			}			
			else{							
				assertEquals(0,ts.getNumberOfTopics());
			}					
		}		
		assertEquals(3, stats.size());
		
				
		Subject subj2 = new AmazonBook();
		subj2.setForeignID(B);
		subj2.setName(A);
		
	
		//Add a new topic with tags 0 & 1
		//and subject 1
		//
		Topic t2 = new Topic();
		t2.getLatestEntry().setData(C);
		t2.setTitle(B3);
		t2.setUser(u);				
		t2.tagTopic(three[1]);				
		t2.tagTopic(three[0]);
		t2.setSubject(subj1);
		editDAO.save(t2);
		
		Topic topic3 = new Topic(u, G);
		//add tag 2 to topic 1
		//and subject 2
		System.out.println("-----------------");
		System.out.println("T: "+t+" ID: "+t.getId());
		System.out.println("S "+subj2+" ID: "+subj2.getId());
		System.out.println("ST "+topic3+" ID "+t.getId());
		
		topic3.tagTopic(three[2]);
		topic3.setSubject(subj2);
		
		System.out.println("subj "+topic3.getSubject()+"  ID "+topic3.getSubject().getId());
		editDAO.save(topic3);
		
		//test 
		//
		stats = tagDAO.getTagStats(u);
		
		////tag 1,2,3 & 2*AmazonBook makes 4. (Not 5!)
		//tag 1,2,3 & 2*AmazonBook makes 3!. (Subjects don't count anymore!)
		assertEquals(3, stats.size());
		
		for (TagStat ts : stats){
			if(ts.getTagId() == three[0].getId()){
				assertEquals(2,ts.getNumberOfTopics());
			}else if(ts.getTagId() == three[1].getId()){			
				assertEquals(1,ts.getNumberOfTopics());
			}
			else if(ts.getTagId() == three[2].getId()){			
				assertEquals(1,ts.getNumberOfTopics());
			}
			log.debug("stat "+ts.getTagId()+" "+ts.getNumberOfTopics()+" lat "+ts.getLatitude()+" long "+ts.getLongitude());		
		}		
		
		
	}

}
