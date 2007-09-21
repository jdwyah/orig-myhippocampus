package com.aavu.server.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;

import com.aavu.server.service.BrowseService;

public class IndexController extends BasicController {
	private static final Logger log = Logger.getLogger(IndexController.class);


	private BrowseService browseService;


	@Required
	public void setBrowseService(BrowseService browse) {
		this.browseService = browse;
	}



	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest req, HttpServletResponse arg1)
			throws Exception {

		log.debug("SERVLET PATH: " + req.getServletPath() + " " + req.getPathInfo() + " "
				+ req.getQueryString());

		Map model = getDefaultModel(req);

		// parameter may be on param line if we're redirect:ed here (createUserController)
		model.put("message", req.getParameter("message"));


		model.put("topTopics", browseService.getTopTopics());

		model.put("topWeblinks", browseService.getTopWeblinks());

		return new ModelAndView(getView(), model);

	}


}
