package com.aavu.server.service.impl;

import java.util.List;

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
	protected void onSetUpInTransaction() throws Exception {

		super.onSetUpInTransaction();

		u = userService.getCurrentUser();
		
	}
	
	private static final String A = "Coffee Roasting";
	private static final String A2 = "Coffee";
	private static final String A3 = "roasting";
	private static final String B = "Some other long title";
	private static final String B2 = "long some";
		
	public void testSearch() throws HippoBusinessException, InterruptedException{
		
		Topic t = new Topic(u,A);		
		t = topicService.save(t);
		
		Topic t2 = new Topic(u,B);		
		t2 = topicService.save(t);
		
		Topic t3 = topicService.getForName(A);
		
	//	searchService.indexNow();		
		
		List<SearchResult> rtn = searchService.search(A);
		assertEquals(1, rtn.size());
		
		rtn = searchService.search(A2);
		assertEquals(1, rtn.size());
		
		rtn = searchService.search(A3);
		assertEquals(1, rtn.size());
		
		rtn = searchService.search(B2);
		assertEquals(1, rtn.size());
		
		
		
		
//		rtn = searchService.search("Coffee");
//		
//		rtn = searchService.search("Shah");
//
//		rtn = searchService.search("tom");
//
//		rtn = searchService.search("full");
//
//		//it is currently searching 
//		rtn = searchService.search("contentEditable=true");
//
//		rtn = searchService.search("body");
//
//		rtn = searchService.search("crack second");
	}	

}
