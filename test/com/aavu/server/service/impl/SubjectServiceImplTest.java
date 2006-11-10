package com.aavu.server.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.dom4j.DocumentException;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.client.domain.subjects.WikiSubject;
import com.aavu.client.exception.HippoException;
import com.aavu.server.domain.DeliciousPost;
import com.aavu.server.service.ExternalServicesService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;

public class SubjectServiceImplTest extends BaseTestWithTransaction {

	private ExternalServicesService subjectService;

	private TopicService topicService;
	
	public void setSubjectService(ExternalServicesService subjectService) {
		this.subjectService = subjectService;
	}
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	public void testFoo() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException{
		String s = "http://www.google.com/";
		String s2 = "something with spaces";

		String ss = URLEncoder.encode(s2,"UTF-8");
		
		URL url = new URL(s+ss);
		
		assertFalse(url.toString().contains(" "));


	}
	public void testSearch(){
		List<Topic> rtn = topicService.search("yirgacheffe");
		
		rtn = topicService.search("Coffee");
		
		rtn = topicService.search("Shah");
		
	}

	public void testCountryLookup() throws HippoException {
		List<? extends Subject> list = subjectService.lookup(HippoCountry.class, "Chad");

		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
	}
	public void testAmazonBookLookup() throws HippoException {
		List<? extends Subject> list = subjectService.lookup(AmazonBook.class, "Roots of Violence: A History of the War in Chad");

		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
		
		
		list = subjectService.lookup(AmazonBook.class, "Chad Roots Violence ");

		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
	}
	public void testWikiLookup() throws HippoException {
		List<? extends Subject> list = subjectService.lookup(WikiSubject.class, "Roots of Violence: A History of the War in Chad");

		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
		
		System.out.println("__________");
		list = subjectService.lookup(WikiSubject.class, "Chad");
		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
		
		System.out.println("__________");
		list = subjectService.lookup(WikiSubject.class, "Bill Clnton");
		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
	}
	public void testDeliciousReq() throws IOException, DocumentException, HippoException{
		List<DeliciousPost> posts = subjectService.deliciousReq("jdwyah","internet.com");

		
	}
	
	public void testDeliciousAdd() throws IOException, DocumentException, HippoException{
		
		assertEquals(0, topicService.getAllTopicIdentifiers().size());		
		subjectService.addDeliciousTags("jdwyah", "internet.com");
		
		assertTrue(topicService.getAllTopicIdentifiers().size() > 40);
	}
}
