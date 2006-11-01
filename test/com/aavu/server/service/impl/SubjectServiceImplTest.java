package com.aavu.server.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.aavu.client.domain.subjects.AmazonBook;
import com.aavu.client.domain.subjects.HippoCountry;
import com.aavu.client.domain.subjects.Subject;
import com.aavu.server.service.SubjectService;
import com.aavu.server.service.gwt.BaseTestNoTransaction;

public class SubjectServiceImplTest extends BaseTestNoTransaction {

	private SubjectService subjectService;


	public void setSubjectService(SubjectService subjectService) {
		this.subjectService = subjectService;
	}

	public void testFoo() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException{
		String s = "http://www.google.com/";
		String s2 = "something with spaces";

		String ss = URLEncoder.encode(s2,"UTF-8");
		
		URL url = new URL(s+ss);
		
		assertFalse(url.toString().contains(" "));


	}

	public void testCountryLookup() {
		List<? extends Subject> list = subjectService.lookup(HippoCountry.class, "Chad");

		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
	}
	public void testAmazonBookLookup() {
		List<? extends Subject> list = subjectService.lookup(AmazonBook.class, "Roots of Violence: A History of the War in Chad");

		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
		
		
		list = subjectService.lookup(AmazonBook.class, "Chad Roots Violence ");

		for (Subject subject : list) {
			System.out.println("list: "+subject.getName()+" "+subject.getForeignID());
		}
	}
}
