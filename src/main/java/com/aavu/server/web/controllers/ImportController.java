package com.aavu.server.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.aavu.server.service.DeliciousService;
import com.aavu.server.service.TheGoogleService;
import com.aavu.server.service.UserService;
import com.aavu.server.web.domain.ImportCommand;

public class ImportController extends SimpleFormController {
	private static final Logger log = Logger.getLogger(ImportController.class);

	private UserService userService;
	private DeliciousService deliciousService;
	private TheGoogleService googleService;

	public ImportController() {
		setCommandClass(ImportCommand.class);
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors) throws Exception {

		String type = request.getParameter("type");

		ImportCommand comm = (ImportCommand) command;

		if (type.equals("delicious")) {
			int found = deliciousService.newLinksForUser(comm.getDeliciousName(), comm
					.getDeliciousPass());

			String successStr = "Success importing " + found + " bookmarks.";

			return new ModelAndView(getSuccessView(), "message", successStr);
		} else if (type.equals("google")) {

			int found = googleService.getDocsForUser(comm.getGoogleName(), comm.getGooglePass());

			String successStr = "Success importing " + found + " documents.";

			return new ModelAndView(getSuccessView(), "message", successStr);
		} else {
			throw new RuntimeException("No Import Type Specified");
		}

	}

	@Required
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Required
	public void setDeliciousService(DeliciousService deliciousService) {
		this.deliciousService = deliciousService;
	}

	@Required
	public void setGoogleService(TheGoogleService googleService) {
		this.googleService = googleService;
	}



}
