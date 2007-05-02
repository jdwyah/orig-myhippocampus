package com.aavu.server.web.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.aavu.client.domain.User;
import com.aavu.server.service.TopicService;
import com.aavu.server.web.domain.MailingListCommand;
import com.aavu.server.web.domain.SearchCommand;
import com.aavu.server.web.domain.UserPageBean;

public class UserPageController extends BasicController {
	private static final Logger log = Logger.getLogger(UserPageController.class);
		
	private TopicService topicService;
	
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse arg1) throws Exception {

		
		log.debug("SERVLET PATH: "+req.getServletPath()+" "+req.getPathInfo()+" "+req.getQueryString());
		
		Map model = getDefaultModel(req);
		

		User su = null;
		try{
			//TODO already have this from getDefaultModel()
			su = userService.getCurrentUser();	
			
			UserPageBean bean = topicService.getUserPageBean(su);
		
			model.put("bean", bean);
			model.put("command",new SearchCommand());
			
			return new ModelAndView(getView(),model);
			
		}catch(UsernameNotFoundException e){
			throw new Exception("No User Logged In.");
		}
		
	}


}
