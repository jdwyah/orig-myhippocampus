package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aavu.client.domain.User;
import com.aavu.server.service.TopicService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.UserPageBean;

public class IndexController extends BasicController {
	private static final Logger log = Logger.getLogger(IndexController.class);
	
	private String loggedInView;	
	
	private TopicService topicService;

	public void setLoggedInView(String loggedInView) {
		this.loggedInView = loggedInView;
	}
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req,
			HttpServletResponse arg1) throws Exception {

		log.debug("SERVLET PATH: "+req.getServletPath());

		User su = null;
		try{
			su = userService.getCurrentUser();	
			
			UserPageBean bean = topicService.getUserPageBean(su);
		
			
			return new ModelAndView(loggedInView,"bean",bean);
		}catch(UsernameNotFoundException e){
			log.debug("No user logged in.");
		}

		return new ModelAndView(getView());
		
	}


}
