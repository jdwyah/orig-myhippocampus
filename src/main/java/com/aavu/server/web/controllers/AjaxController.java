package com.aavu.server.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.aavu.client.domain.dto.TopicIdentifier;
import com.aavu.server.service.TopicService;

public class AjaxController extends MultiActionController {
	private static Logger log = Logger.getLogger(AjaxController.class);
			
	private TopicService	topicService;	
	
	
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}

	public ModelAndView topicSearch(HttpServletRequest request, HttpServletResponse response) {

		String searchString = "";
		
		searchString = request.getParameter("match");
		log.debug("last name start: "+searchString);
		
		if(searchString.contains(",")){
			return new ModelAndView("simpleString","string","<UL></UL>");
		}
		
		List<TopicIdentifier> tags = topicService.getTopicsStarting(searchString);

		return new ModelAndView("simpleString","string",getMatchList(tags));
	}
	
	private String getMatchList(List<TopicIdentifier> list){
		StringBuffer str = new StringBuffer("<UL>");
		for (TopicIdentifier ti : list) {
			str.append("<LI>");
			str.append(ti.getTopicTitle());
			str.append("</LI>");
		}
		str.append("</UL>");
		return str.toString();		
	}
}
