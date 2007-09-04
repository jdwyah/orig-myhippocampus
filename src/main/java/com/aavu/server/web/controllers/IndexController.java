package com.aavu.server.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import com.aavu.client.domain.User;
import com.aavu.server.service.TopicService;

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
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse arg1)
			throws Exception {

		log.debug("SERVLET PATH: " + req.getServletPath() + " " + req.getPathInfo() + " "
				+ req.getQueryString());

		Map model = getDefaultModel(req);

		// parameter may be on param line if we're redirect:ed here (createUserController)
		model.put("message", req.getParameter("message"));

		User su = null;
		try {
			su = userService.getCurrentUser();
			return new ModelAndView(loggedInView);

		} catch (UsernameNotFoundException e) {
			log.debug("No user logged in.");
		}
		return new ModelAndView(getView(), model);

	}


}
