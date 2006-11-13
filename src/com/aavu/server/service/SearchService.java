package com.aavu.server.service;

import java.util.List;

import com.aavu.client.domain.SearchResult;

public interface SearchService {

	List<SearchResult> search(String searchString);
	
}
