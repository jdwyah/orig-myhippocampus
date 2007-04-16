package com.aavu.server.web.controllers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.aavu.client.domain.Topic;
import com.aavu.client.domain.dto.FullTopicIdentifier;
import com.aavu.server.service.TopicService;
import com.aavu.server.web.domain.SearchCommand;

public class ShowTopicController extends BasicController {
	private TopicService topicService;

	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse arg1) throws Exception {
		
		long id = Long.parseLong(req.getParameter("id"));
		
		Topic result = topicService.getForID(id);
		List<FullTopicIdentifier> onThisIsland = topicService.getTopicIdsWithTag(id);
		
		Map<String,Object> model = getDefaultModel(req);
		model.put("topic", result);
		model.put("onThisIsland",onThisIsland);
		model.put("command",new SearchCommand());
		
		
		return new ModelAndView(getView(), model);
	}

}
