package com.aavu.server.service.gwt;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.log4j.PropertyConfigurator;
import org.hibernate.collection.PersistentSet;

import com.aavu.client.domain.Association;
import com.aavu.client.domain.MetaText;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;

import junit.framework.TestCase;

public class NewConverterTest extends TestCase {

	public static final String A = "t";
	public static final String B = "bob beemer";
	public static final String C = "t2";
	public static final String D = "t3";
	public static final String E = "tyjt";
	public static final String F = "wertre";
	public static final String G = "452345";
	public static final String H = "3453";
	public static final String I = "1435345";
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		String path = "tomcat/webapps/HippoTest/WEB-INF/";
		String pathc = path+"classes/";
		PropertyConfigurator.configure(pathc+"log4j.properties");
	}

	public void testConvert(){
		Association association = new Association();
		
		NewConverter.convertInPlace(association);
		
	}
	public void testConvert2(){
		
		User u = new User();
		u.setUsername(B);
		Topic t = new Topic(u,A);
		Topic t2 = new Topic(u,C);
				
		t.addSeeAlso(t2.getIdentifier());
		
		NewConverter.convertInPlace(t);
		
		
		assertEquals(u,t.getUser());
		assertEquals(A,t.getTitle());
		assertEquals(C,((Topic)t.getSeeAlsoAssociation().getMembers().iterator().next()).getTitle());
	}
	
	public void testConvertLazies(){
		
		User u = new User();
		u.setUsername(B);
		Topic t = new Topic(u,A);
		t.setCreated(new java.sql.Date(2004,5,6));
		Topic t2 = new Topic(u,C);
		
		t2.setTypes(new PersistentSet());
		
		t.addSeeAlso(t2.getIdentifier());
		
		NewConverter.convertInPlace(t);
		
		
		assertEquals(u,t.getUser());
		assertEquals(A,t.getTitle());
		Topic t2_p = (Topic)t.getSeeAlsoAssociation().getMembers().iterator().next();
		assertEquals(C,t2_p.getTitle());
		
		
		assertEquals(HashSet.class,t2_p.getTypes().getClass());
		assertEquals(java.util.Date.class, t.getCreated().getClass());
	}
	
	public void testConvertLazies2(){
		
		User u = new User();
		u.setUsername(B);
		Topic t = new Topic(u,A);
		t.setCreated(new java.sql.Date(2004,5,6));
		
		Topic t2 = new Topic(u,C);		
		t2.setAssociations(new PersistentSet());
		
		Topic t3 = new Topic(u,D);
		t3.setOccurences(new PersistentSet());
		

		t2.tagTopic(t3);
		
		assertEquals(1, t2.getTags().size());
		
		t.tagTopic(t2);
		//t.addType(t)(t2.getIdentifier());
		
		
		NewConverter.convertInPlace(t);
		
		
		assertEquals(u,t.getUser());
		assertEquals(A,t.getTitle());
		Topic t2_p = (Topic)t.getTags().iterator().next();
		assertEquals(C,t2_p.getTitle());
		
		
		assertEquals(HashSet.class,t2_p.getAssociations().getClass());
		assertEquals(java.util.Date.class, t.getCreated().getClass());
		
		assertEquals(1, t2_p.getTags().size());
		
		Topic t3_p = (Topic) t2_p.getTags().iterator().next();
		assertEquals(HashSet.class,t3_p.getOccurences().getClass());
	}
	
	
	public void testConvertCGLIB(){
		
		User u = new User();
		u.setUsername(B);
		Topic t = new Topic(u,A);
		t.setCreated(new java.sql.Date(2004,5,6));
		
		Topic t2 = new Topic(u,C);		
		t2.setAssociations(new PersistentSet());
				
		TopicCGLIB t4 = new TopicCGLIB(u,E);
		

		t2.tagTopic(t4);
		
		assertEquals(1, t2.getTags().size());
		
		t.tagTopic(t2);
		//t.addType(t)(t2.getIdentifier());
		
		
		NewConverter.convertInPlace(t);
		
		
		assertEquals(u,t.getUser());
		assertEquals(A,t.getTitle());
		Topic t2_p = (Topic)t.getTags().iterator().next();
		assertEquals(C,t2_p.getTitle());
		
		
		assertEquals(HashSet.class,t2_p.getAssociations().getClass());
		assertEquals(java.util.Date.class, t.getCreated().getClass());
		
		assertEquals(1, t2_p.getTags().size());
		
		Iterator i = t2_p.getTags().iterator();
		Topic t4_p = (Topic) i.next();
		
		assertEquals(Topic.class, t4_p.getClass());
		
	}
	
	
}
