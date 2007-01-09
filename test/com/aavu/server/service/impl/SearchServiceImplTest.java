package com.aavu.server.service.impl;

import java.util.Iterator;
import java.util.List;

import org.compass.gps.device.hibernate.Hibernate3GpsDevice;
import org.compass.spring.device.hibernate.SpringHibernate3GpsDevice;

import com.aavu.client.domain.SearchResult;
import com.aavu.client.domain.Topic;
import com.aavu.client.domain.User;
import com.aavu.client.exception.HippoBusinessException;
import com.aavu.server.service.SearchService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;

public class SearchServiceImplTest extends BaseTestWithTransaction {

	private SearchService searchService;
	private TopicService topicService;
	private UserService userService;
	private User u;
		
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	protected void onSetUpBeforeTransaction() throws Exception {		
		super.onSetUpBeforeTransaction();
		setUsername("test");
	}
	@Override
	protected void onSetUpInTransaction() throws Exception {

		super.onSetUpInTransaction();

		u = userService.getCurrentUser();
		
	}
	
	private static final String A = "Coffee Roasting";
	private static final String A2 = "Coffee";
	private static final String A3 = "roasting";
	private static final String B = "Some other long title";
	private static final String B2 = "long some";
		
	public void testEZ() {

		List<SearchResult> rtn = null;//
		rtn = searchService.search("Coffee");
		assertEquals(2, rtn.size());
		
//		rtn = searchService.search("Shah");
//
		rtn = searchService.search("tom");
		assertEquals(2, rtn.size());
		
//		rtn = searchService.search("full");

		//it is currently searching 
		rtn = searchService.search("contentEditable=true");
		assertEquals(0, rtn.size());

		rtn = searchService.search("body");
		//dbg("body",rtn);		
		assertEquals(2, rtn.size());
		
		rtn = searchService.search("crack second");
		assertEquals(1, rtn.size());
	}
	
	private void dbg(String string, List<SearchResult> rtn) {
		System.out.println("\n\n---------"+string+"-------");
		for (SearchResult result : rtn) {
			System.out.println("rtn "+result);
		}
		System.out.println("----------------");
	}
	public void testSearch() throws HippoBusinessException, InterruptedException{
		
		searchService.indexNow();
		
		Topic t = new Topic(u,A);		
		t = topicService.save(t);
		
		Topic t2 = new Topic(u,B);		
		t2 = topicService.save(t);
		
		Topic t3 = topicService.getForName(A);		
		
		List<SearchResult> rtn = searchService.search(A);
		assertEquals(1, rtn.size());
		
		rtn = searchService.search(A2);
		assertEquals(1, rtn.size());
		
		rtn = searchService.search(A3);
		assertEquals(1, rtn.size());
		
		rtn = searchService.search(B2);
		assertEquals(1, rtn.size());
		
				
	}	

}
