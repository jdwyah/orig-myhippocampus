package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.server.service.DeliciousService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.DeliciousCommand;

public class DeliciousBookmarkController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(DeliciousBookmarkController.class);

	private UserService userService;
	private DeliciousService deliciousService;

	public DeliciousBookmarkController() {
		setCommandClass(DeliciousCommand.class);
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		DeliciousCommand comm = (DeliciousCommand) command;

		int found = deliciousService.newLinksForUser(comm.getDeliciousName(), comm
				.getDeliciousPass());

		String successStr = "Success importing " + found + " bookmarks.";

		return new ModelAndView(getSuccessView(), "message", successStr);


	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Required
	public void setDeliciousService(DeliciousService deliciousService) {
		this.deliciousService = deliciousService;
	}



}
