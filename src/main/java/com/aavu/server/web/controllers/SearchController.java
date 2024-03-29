package com.aavu.server.web.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.aavu.client.domain.dto.SearchResult;
import com.aavu.server.service.SearchService;
import com.aavu.server.web.domain.SearchCommand;

public class SearchController extends BasicFormController {
	private static final Logger log = Logger.getLogger(SearchController.class);

	private SearchService searchService;

	public SearchController() {
		setCommandClass(SearchCommand.class);
	}



	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		SearchCommand searchCommand = (SearchCommand) command;


		Map<String, Object> model = getDefaultModel(request);

		List<SearchResult> results = searchService.search(searchCommand.getSearchTerm());
		model.put("results", results);
		model.put("command", new SearchCommand());

		return new ModelAndView(getSuccessView(), model);
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

}
