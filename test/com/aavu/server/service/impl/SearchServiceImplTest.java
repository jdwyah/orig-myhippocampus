package com.aavu.server.service.impl;

import java.util.List;

import com.aavu.client.domain.SearchResult;
import com.aavu.server.service.SearchService;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.gwt.BaseTestWithTransaction;

public class SearchServiceImplTest extends BaseTestWithTransaction {

	private SearchService searchService;

	private TopicService topicService;
		
	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	public void testSearch(){
		List<SearchResult> rtn = searchService.search("yirgacheffe");
		
		rtn = searchService.search("Coffee");
		
		rtn = searchService.search("Shah");
		
		rtn = searchService.search("tom");
		
		rtn = searchService.search("full");
		
		//it is currently searching 
		rtn = searchService.search("contentEditable=true");
				 
		rtn = searchService.search("body");
				 
		rtn = searchService.search("crack second");
	}	

}
